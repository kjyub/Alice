package game;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import components.RoundedButton;

import java.awt.*;
import java.awt.event.*;

import db.AliceDAO;
import libs.ActionTypes;
import ui.LoginFrame;

public class RankFrame extends JFrame {
	DefaultTableModel rankTableModel;
	AliceDAO dao = null;
	RankFrame(AliceDAO dao) {
		this.dao = dao;
		this.setTitle("ALICE - RANK");
		this.setSize(400,600);
		Container c = this.getContentPane();
		
		String[] rankTableHeaders = {"순위","아이디","목 길이 평균","플레이 시간"};
		rankTableModel = new DefaultTableModel(null,rankTableHeaders);
		dao.loadRank(rankTableModel);
		JTable rankTable = new JTable(rankTableModel);
		c.add(new JScrollPane(rankTable));
		
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
