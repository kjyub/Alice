package db;

import java.sql.*;
import java.util.*;

import ui.GameField;
import ui.GameMain;
import ui.Giraffe;
import ui.GiraffeVO;
import ui.Tree;
import ui.TreeVO;

public class AliceDAO {
	
	private static final String LOGIN = "select pk,loggedIn from userTBL where id=? and pw=? ";
	private static final String CHECK_ID = "select loggedIn from userTBL where id=?";
	private static final String SET_LOGIN = "update userTBL set loggedIn=true where pk=? ";
	private static final String LOGOUT = "update userTBL set loggedIn=false where pk=? ";
	private static final String LOAD_USER = "select * from userTBL where pk=? ";
	private static final String LOAD_GIRAFFES = "select * from giraffeTBL where user_pk=? ";
	private static final String LOAD_TREES = "select * from treeTBL where user_pk=? ";
	private static final String UPDATE_USER = "update userTBL set "
			+ "timeStamp=?, timeStopFlag=?, maxGiraffeID=?, searchScale=?, ageRate=?, breedReadyValue=?, "
			+ "breedValue=?, maxIndependence=?, started=?, mutantProb=?, sizeScale=?, subjectInfo=? "
			+ "where pk=? ";
	private static final String UPDATE_GIRAFFES = "insert into giraffeTBL("
			+ "id, neck, birthDate, lastDirection, lastHeadDirection, hungry, breed, independence, "
			+ "x, y, isMove, isEating, isBreeded, isReflected, isDetected, died,user_pk"
			+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_TREES = "insert into treeTBL("
			+ "length,leaf0,leaf1,leaf2,x,y,user_pk"
			+ ") values (?,?,?,?,?,?,?)";
	private static final String DELETE_GIRAFFES = "delete from giraffeTBL where user_pk=?";
	private static final String DELETE_TREES = "delete from treeTBL where user_pk=?";
	private static final String REGIST = "insert into userTBL("
			+ "timeStamp,timeStopFlag,maxGiraffeID,searchScale,ageRate,breedReadyValue,breedValue,maxIndependence,started,mutantProb,sizeScale,subjectInfo,id,pw"
			+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	Connection connection=null;
	PreparedStatement stmt = null;
	GameMain gameMain;
	GameField gameField;
	
	
	public AliceDAO(GameMain gm,GameField gf) {
		this.gameMain = gm;
		this.gameField = gf;
	}
	
   	void dbConnect() {
   		connection = null;
   		try {
   			Class.forName("com.mysql.cj.jdbc.Driver");
   			System.out.println("Driver loaded");
   		} catch(ClassNotFoundException e) {
   			System.out.println("Driver NotFound : "+e);
   		}
   		try {
   			DBConnectData dbProperties = new DBConnectData();
   			connection = DriverManager.getConnection(dbProperties.dbURL, dbProperties.properties);
   			System.out.println("Driver Connected");
   		} catch (SQLException e) {
   			e.printStackTrace();
   		}
   	}
   	
   	void dbDisconnect() {
   		try {
   			if (connection != null) {
   				connection.close();
   			}
   			if (stmt != null) {
   				stmt.close();
   			}
   			System.out.println("DB Disconnected");
   		} catch (SQLException e) {
   			System.out.println("DB disconnect failed : "+e);
			e.printStackTrace();
   		}
   	}
   	
   	void refreshStmt() {
   		if(stmt != null) {
   			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("STMT close failed : "+e);
				e.printStackTrace();
			}
   			stmt = null;
   		}
   	}
   	
   	public void login(String id,String pw) {
   		dbConnect();
   		ResultSet rs = null;
   		try {
			stmt = connection.prepareStatement(LOGIN);
			stmt.setString(1, id);
			stmt.setString(2, pw);
			rs = stmt.executeQuery();
			if(rs.next()) {
				if(rs.getBoolean("loggedIn")) {
					System.out.println("is logged in");
					return;
				}
				// setLoggedIn
				GameMain.user_pk = rs.getInt("pk");
				stmt = connection.prepareStatement(SET_LOGIN);
				stmt.setInt(1, GameMain.user_pk);
				int result = stmt.executeUpdate();
				System.out.println("Login Result : "+result);
				loadUser(GameMain.user_pk);
				loadGiraffes(GameMain.user_pk);
				loadTrees(GameMain.user_pk);
				return;
			}
			System.out.println("REGIST!");
			if (checkID(id)) {				
				System.out.println("Wrong password");
			} else {
				regist(id,pw);
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception login : "+e);
			e.printStackTrace();
		}
   		dbDisconnect();
   	}
   	
   	boolean checkID(String id) {
   		ResultSet rs = null;
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(CHECK_ID);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if(rs.next()) {
				// setLoggedIn
				return true;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception login : "+e);
			e.printStackTrace();
		}
   		return false;
   	}
   	
   	void regist(String id,String pw) {
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(REGIST);
			stmt.setLong(1, GameField.timeStamp);
			stmt.setBoolean(2, GameField.timeStopFlag);
			stmt.setInt(3, GameField.maxGiraffeID);
			stmt.setInt(4, GameField.searchScale);
			stmt.setInt(5, GameField.ageRate);
			stmt.setInt(6, GameField.breedReadyValue);
			stmt.setInt(7, GameField.breedValue);
			stmt.setInt(8, GameField.maxIndependence);
			stmt.setBoolean(9, GameField.started);
			stmt.setInt(10, GameMain.mutantProb);
			stmt.setFloat(11, GameMain.sizeScale);
			stmt.setBoolean(12, GameMain.subjectInfo);
			
			stmt.setString(13, id);
			stmt.setString(14, pw);
			
			stmt.executeUpdate();
			
			ResultSet rs = null;
			
			stmt = connection.prepareStatement(LOGIN);
			stmt.setString(1, id);
			stmt.setString(2, pw);
			rs = stmt.executeQuery();
			if(rs.next()) {
				stmt = connection.prepareStatement(SET_LOGIN);
				stmt.setInt(1, GameMain.user_pk);
				stmt.executeUpdate();
				return;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception login : "+e);
			e.printStackTrace();
		}
   	}
   	
   	void loadUser(int user_pk) {
   		ResultSet rs = null;
		System.out.println("hi");
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_USER);
			stmt.setInt(1, user_pk);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			if(rs.next()) {
				GameMain.user_pk = rs.getInt("pk");
				GameField.timeStamp = rs.getLong("timeStamp");
				GameField.timeStopFlag = rs.getBoolean("timeStopFlag");
				GameField.maxGiraffeID = rs.getInt("maxGiraffeID");
				GameField.searchScale = rs.getInt("searchScale");
				GameField.ageRate = rs.getInt("ageRate");
				GameField.breedReadyValue = rs.getInt("breedReadyValue");
				GameField.breedValue = rs.getInt("breedValue");
				GameField.maxIndependence = rs.getInt("maxIndependence");
				GameField.started = rs.getBoolean("started");
				
				GameMain.mutantProb = rs.getInt("mutantProb");
				GameMain.sizeScale = rs.getFloat("sizeScale");
				GameMain.subjectInfo = rs.getBoolean("subjectInfo");
				System.out.println("user loaded");
				GameMain.updatePanel();
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception loadUser : "+e);
			e.printStackTrace();
		}
   	}
   	
   	void loadGiraffes(int user_pk) {
   		ResultSet rs = null;
		System.out.println("hi");
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_GIRAFFES);
			stmt.setInt(1, user_pk);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			ArrayList<GiraffeVO> grfs = new ArrayList<GiraffeVO>();
			while(rs.next()) {
				GiraffeVO grf = new GiraffeVO();
				grf.setId(rs.getInt("id"));
				grf.setNeck(rs.getInt("neck"));
				grf.setBirthDate(rs.getString("birthDate"));
				grf.setLastDirection(rs.getInt("lastDirection"));
				grf.setLastHeadDirection(rs.getInt("lastHeadDirection"));
				grf.setHungry(rs.getInt("hungry"));
				grf.setBreed(rs.getInt("breed"));
				grf.setIndependence(rs.getInt("independence"));
				grf.setX(rs.getInt("x"));
				grf.setY(rs.getInt("y"));
				grf.setMove(rs.getBoolean("isMove"));
				grf.setEating(rs.getBoolean("isEating"));
				grf.setBreeded(rs.getBoolean("isBreeded"));
				grf.setReflected(rs.getBoolean("isReflected"));
				grf.setDetected(rs.getBoolean("isDetected"));
				grf.setDied(rs.getBoolean("died"));
				grfs.add(grf);
			}
			gameField.summon(grfs);
		} catch (SQLException e) {
			System.out.println("SQL Exception loadUser : "+e);
			e.printStackTrace();
		}
   	}
   	
   	void loadTrees(int user_pk) {
   		ResultSet rs = null;
		System.out.println("hi");
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_TREES);
			stmt.setInt(1, user_pk);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			ArrayList<TreeVO> trees = new ArrayList<TreeVO>();
			while(rs.next()) {
				TreeVO vo = new TreeVO();
				vo.setLength(rs.getInt("length"));
				vo.setLeaf0(rs.getInt("leaf0"));
				vo.setLeaf1(rs.getInt("leaf1"));
				vo.setLeaf2(rs.getInt("leaf2"));
				vo.setX(rs.getInt("x"));
				vo.setY(rs.getInt("y"));
				trees.add(vo);
			}
			gameField.treeSummon(trees);
		} catch (SQLException e) {
			System.out.println("SQL Exception loadTree : "+e);
			e.printStackTrace();
		}
   	}
   	
   	void updateUser(int user_pk) {
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(UPDATE_USER);
			stmt.setLong(1, GameField.timeStamp);
			stmt.setBoolean(2, GameField.timeStopFlag);
			stmt.setInt(3, GameField.maxGiraffeID);
			stmt.setInt(4, GameField.searchScale);
			stmt.setInt(5, GameField.ageRate);
			stmt.setInt(6, GameField.breedReadyValue);
			stmt.setInt(7, GameField.breedValue);
			stmt.setInt(8, GameField.maxIndependence);
			stmt.setBoolean(9, GameField.started);
			stmt.setInt(10, GameMain.mutantProb);
			stmt.setFloat(11, GameMain.sizeScale);
			stmt.setBoolean(12, GameMain.subjectInfo);
			stmt.setInt(13, user_pk);
			int result = stmt.executeUpdate();
			System.out.println("Update User Result : "+result);
		} catch (SQLException e) {
			System.out.println("SQL Exception updateUser : "+e);
			e.printStackTrace();
		}
   	}
   	
   	void updateGiraffes(int user_pk) {
   		try {
			refreshStmt();
			stmt = connection.prepareStatement(DELETE_GIRAFFES);
			stmt.setInt(1, GameMain.user_pk);
			stmt.executeUpdate();
			for (Giraffe grf : GameField.giraffes) {		
				refreshStmt();
				stmt = connection.prepareStatement(UPDATE_GIRAFFES);
				GiraffeVO vo = grf.parseVO();
				stmt.setInt(1, vo.getId());
				stmt.setInt(2, vo.getNeck());
				stmt.setString(3, vo.getBirthDate());
				stmt.setInt(4, vo.getLastDirection());
				stmt.setInt(5, vo.getLastHeadDirection());
				stmt.setInt(6, vo.getHungry());
				stmt.setInt(7, vo.getBreed());
				stmt.setInt(8, vo.getIndependence());
				stmt.setInt(9, vo.getX());
				stmt.setInt(10, vo.getY());
				stmt.setBoolean(11, vo.isMove());
				stmt.setBoolean(12, vo.isEating());
				stmt.setBoolean(13, vo.isBreeded());
				stmt.setBoolean(14, vo.isReflected());
				stmt.setBoolean(15, vo.isDetected());
				stmt.setBoolean(16, vo.isDied());
				stmt.setInt(17, user_pk);
				stmt.executeUpdate();
			}
			System.out.println("Update User Result : ");
		} catch (SQLException e) {
			System.out.println("SQL Exception updateGiraffes : "+e);
			e.printStackTrace();
		}
   	}
   	
   	void updateTrees(int user_pk) {
   		try {
			refreshStmt();
			stmt = connection.prepareStatement(DELETE_TREES);
			stmt.setInt(1, GameMain.user_pk);
			stmt.executeUpdate();
			int successResult = 0;
			for (Tree tree : GameField.trees) {		
				refreshStmt();
				stmt = connection.prepareStatement(UPDATE_TREES);
				TreeVO vo = tree.parseVO();
				stmt.setInt(1, vo.getLength());
				stmt.setInt(2, vo.getLeaf0());
				stmt.setInt(3, vo.getLeaf1());
				stmt.setInt(4, vo.getLeaf2());
				stmt.setInt(5, vo.getX());
				stmt.setInt(6, vo.getY());
				stmt.setInt(7, user_pk);
				successResult += stmt.executeUpdate();
			}
			System.out.println("Update Tree Result : "+successResult);
		} catch (SQLException e) {
			System.out.println("SQL Exception updateGiraffes : "+e);
			e.printStackTrace();
		}
   	}
   	
   	public void logout() {
   		dbConnect();
   		updateUser(GameMain.user_pk);
   		updateGiraffes(GameMain.user_pk);
   		updateTrees(GameMain.user_pk);
   		try {
			stmt = connection.prepareStatement(LOGOUT);
			stmt.setInt(1, GameMain.user_pk);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Exception logout : "+e);
			e.printStackTrace();
		}
   		dbDisconnect();
   	}
}
