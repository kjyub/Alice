package db;

import java.sql.*;
import java.util.*;

import javax.swing.table.DefaultTableModel;

import game.*;
import libs.ActionTypes;

public class AliceDAO {
	
	private static final String LOGIN = "select pk,id,loggedIn from userTBL where id=? and pw=? ";
	private static final String CHECK_ID = "select loggedIn from userTBL where id=?";
	private static final String SET_LOGIN = "update userTBL set loggedIn=true where pk=? ";
	private static final String SET_LOGOUT = "update userTBL set loggedIn=false where pk=? ";
	private static final String LOGOUT = "update userTBL set loggedIn=false where pk=? ";
	private static final String LOAD_USER = "select * from userTBL where pk=? ";
	private static final String LOAD_GIRAFFES = "select * from giraffeTBL where user_pk=? ";
	private static final String LOAD_TREES = "select * from treeTBL where user_pk=? ";
	private static final String LOAD_HEIGHTS = "select * from heightTBL where user_pk=? ";
	private static final String LOAD_RANK = "select * from rankTBL order by avr desc";
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
	private static final String UPDATE_HEIGHTS = "insert into heightTBL("
			+ "seq,avr,user_pk"
			+ ") values (?,?,?)";
	private static final String UPDATE_RANK = "update rankTBL set "
			+ "avr=?, time=? "
			+ "where user_pk=?";
	private static final String DELETE_GIRAFFES = "delete from giraffeTBL where user_pk=?";
	private static final String DELETE_TREES = "delete from treeTBL where user_pk=?";
	private static final String DELETE_HEIGHTS = "delete from heightTBL where user_pk=?";
	private static final String REGIST = "insert into userTBL("
			+ "timeStamp,timeStopFlag,maxGiraffeID,searchScale,ageRate,breedReadyValue,breedValue,maxIndependence,started,mutantProb,sizeScale,subjectInfo,id,pw"
			+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String INSERT_RANK = "insert into rankTBL("
			+ "id,avr,time,user_pk"
			+ ") values (?,0.0,0,?)";
	
	private static int user_pk = -1;
	private static String user_id = "";
	
	Connection connection=null;
	PreparedStatement stmt = null;
	GameMain gameMain;
	GameField gameField;
	
	public AliceDAO() {
		System.out.println("hi"+ActionTypes.LOAD_FAILED);

	}
	
	public AliceDAO(GameMain gm,GameField gf) {
		this.gameMain = gm;
		this.gameField = gf;
	}
	
	public int getUserPK() {
		return this.user_pk;
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
   	
   	public String getID() {
   		return this.user_id;
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
   	
   	public String login(String id,String pw) {
   		if (id.length()<1 || pw.length()<1) {
   			return ActionTypes.INVALID_INPUT;
   		}
   		dbConnect();
   		ResultSet rs = null;
   		try {
			stmt = connection.prepareStatement(LOGIN);
			stmt.setString(1, id);
			stmt.setString(2, pw);
			rs = stmt.executeQuery();
			if(rs.next()) {
				this.user_pk = rs.getInt("pk");
				this.user_id = rs.getString("id");
				if(rs.getBoolean("loggedIn")) {
			   		dbDisconnect();
					return ActionTypes.LOGGED_IN;
				}
				stmt = connection.prepareStatement(SET_LOGIN);
				stmt.setInt(1, this.user_pk);
				int result = stmt.executeUpdate();
		   		dbDisconnect();
				return ActionTypes.LOGIN_SUCCESS;
			}
			if (checkID(id)) {
		   		dbDisconnect();
				return ActionTypes.WRONG_PASSWORD;
			} else {
				int result = regist(id,pw);
				if (result == 1) {
			   		dbDisconnect();
					return ActionTypes.REGIST_SUCCESS;
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception login : "+e);
			e.printStackTrace();
		}
   		dbDisconnect();
   		return ActionTypes.LOGIN_FAILED;
   	}
   	
   	public boolean setLogout() {
   		dbConnect();
   		try {
			stmt = connection.prepareStatement(SET_LOGOUT);
			stmt.setInt(1, this.user_pk);
			stmt.executeUpdate();
			dbDisconnect();
	   		return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception setLogout : "+e);
			e.printStackTrace();
		}
   		dbDisconnect();
   		return false;
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
   	
   	int regist(String id,String pw) {
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
				this.user_pk = rs.getInt("pk");
				
				stmt = connection.prepareStatement(INSERT_RANK);
				stmt.setString(1, id);
				stmt.setInt(2, this.user_pk);
				stmt.executeUpdate();
				
				stmt = connection.prepareStatement(SET_LOGIN);
				stmt.setInt(1, this.user_pk);
				stmt.executeUpdate();
				return 1;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception login : "+e);
			e.printStackTrace();
			return 0;
		}
   		return 0;
   	}
   	
   	public String loadData() {
   		dbConnect();
   		boolean lu = loadUser();
   		boolean lg = loadGiraffes();
   		boolean lt = loadTrees();
   		boolean lh = loadHeights();
   		dbDisconnect();
   		if (lu && lg && lt && lh) {
   			return ActionTypes.LOAD_SUCCESS;
   		}
   		return ActionTypes.LOAD_FAILED;
   	}
   	
   	public boolean loadUser() {
   		ResultSet rs = null;
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_USER);
			stmt.setInt(1, user_pk);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			if(rs.next()) {
				this.user_pk = rs.getInt("pk");
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
			}
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception loadUser : "+e);
			e.printStackTrace();
		}
   		return false;
   	}
   	
   	public boolean loadGiraffes() {
   		ResultSet rs = null;
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_GIRAFFES);
			stmt.setInt(1, user_pk);
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
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception loadUser : "+e);
			e.printStackTrace();
		}
   		return false;
   	}
   	
   	public boolean loadTrees() {
   		ResultSet rs = null;
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_TREES);
			stmt.setInt(1, user_pk);
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
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception loadTree : "+e);
			e.printStackTrace();
		}
		return false;
   	}
   	
   	public boolean loadHeights() {
   		ResultSet rs = null;
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_HEIGHTS);
			stmt.setInt(1, user_pk);
			rs = stmt.executeQuery();
			Vector<Float> heights = new Vector<Float>();
			while(rs.next()) {
				heights.add(rs.getFloat("avr"));
			}
			gameField.giraffeAverages = heights;
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception loadTree : "+e);
			e.printStackTrace();
		}
		return false;
   	}
   	
   	public void loadRank(DefaultTableModel model) {
   		dbConnect();
   		ResultSet rs = null;
   		try {
   			refreshStmt();
			stmt = connection.prepareStatement(LOAD_RANK);
			rs = stmt.executeQuery();
			model.setNumRows(0);
			int rank = 1;
			while(rs.next()) {
				Vector<String> row = new Vector<String>();
				row.add(Integer.toString(rank++));
				row.add(rs.getString("id"));
				row.add(Float.toString(rs.getFloat("avr")));
				row.add(GameField.getTimeStampToString(rs.getInt("time")));
				model.addRow(row);
			}
	   		dbDisconnect();
			return;
		} catch (SQLException e) {
			System.out.println("SQL Exception loadRank : "+e);
			e.printStackTrace();
		}
   		dbDisconnect();
		return;
   	}
   	
   	public boolean updateUser() {
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
			stmt.setInt(13, this.user_pk);
			int result = stmt.executeUpdate();
			System.out.println("Update User Result : "+result);
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception updateUser : "+e);
			e.printStackTrace();
		}
		return false;
   	}
   	
   	public boolean updateGiraffes(Vector<Giraffe> copiedGiraffes) {
   		try {
			refreshStmt();
			stmt = connection.prepareStatement(DELETE_GIRAFFES);
			stmt.setInt(1, this.user_pk);
			stmt.executeUpdate();
			for (Giraffe grf : copiedGiraffes) {		
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
				stmt.setInt(17, this.user_pk);
				stmt.executeUpdate();
			}
			System.out.println("Update User Result : ");
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception updateGiraffes : "+e);
			e.printStackTrace();
		}
		return false;
   	}
   	
   	public boolean updateTrees(Vector<Tree> copiedTrees) {
   		try {
			refreshStmt();
			stmt = connection.prepareStatement(DELETE_TREES);
			stmt.setInt(1, this.user_pk);
			stmt.executeUpdate();
			int successResult = 0;
			for (Tree tree : copiedTrees) {		
				refreshStmt();
				stmt = connection.prepareStatement(UPDATE_TREES);
				TreeVO vo = tree.parseVO();
				stmt.setInt(1, vo.getLength());
				stmt.setInt(2, vo.getLeaf0());
				stmt.setInt(3, vo.getLeaf1());
				stmt.setInt(4, vo.getLeaf2());
				stmt.setInt(5, vo.getX());
				stmt.setInt(6, vo.getY());
				stmt.setInt(7, this.user_pk);
				successResult += stmt.executeUpdate();
			}
			System.out.println("Update Tree Result : "+successResult);
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception updateTrees : "+e);
			e.printStackTrace();
		}
		return false;
   	}
   	
   	public boolean updateHeights(Vector<Float> copiedAverages) {
   		try {
			refreshStmt();
			stmt = connection.prepareStatement(DELETE_HEIGHTS);
			stmt.setInt(1, this.user_pk);
			int result = stmt.executeUpdate();
			for (int i=0; i<copiedAverages.size(); i++) {		
				refreshStmt();
				stmt = connection.prepareStatement(UPDATE_HEIGHTS);
				stmt.setInt(1, i);
				stmt.setFloat(2, copiedAverages.get(i));
				stmt.setInt(3, this.user_pk);
				stmt.executeUpdate();
			}
			System.out.println("Update Tree Result : ");
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception updateHeights : "+e);
			e.printStackTrace();
		}
		return false;
   	}
   	
   	public boolean updateRank() {
   		dbConnect();
   		try {
			refreshStmt();
			stmt = connection.prepareStatement(UPDATE_RANK);
			stmt.setFloat(1, gameField.giraffeAverage);
			stmt.setInt(2, (int) gameField.timeStamp);
			stmt.setInt(3, this.user_pk);
			stmt.executeUpdate();
			System.out.println(stmt);
			dbDisconnect();
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception updateRank : "+e);
			e.printStackTrace();
		}
   		dbDisconnect();
		return false;
   	}
   	
   	public boolean logout() {
   		dbConnect();
   		Vector<Giraffe> copiedGiraffes = new Vector<Giraffe>(GameField.giraffes);
   		Vector<Tree> copiedTrees = new Vector<Tree>(GameField.trees);
   		Vector<Float> copiedAverages = new Vector<Float>(GameField.giraffeAverages);
   		boolean uu = updateUser();
   		boolean ug = updateGiraffes(copiedGiraffes);
   		boolean ut = updateTrees(copiedTrees);
   		boolean uh = updateHeights(copiedAverages);
   		if (uu && ug && ut && uh) {   			
   			try {
   				stmt = connection.prepareStatement(LOGOUT);
   				stmt.setInt(1, this.user_pk);
   				stmt.executeUpdate();
   		   		dbDisconnect();
   				return true;
   			} catch (SQLException e) {
   				System.out.println("SQL Exception logout : "+e);
   				e.printStackTrace();
   			}
   		}
   		dbDisconnect();
   		return false;
   	}
}
