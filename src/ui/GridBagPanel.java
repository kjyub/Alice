package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class GridBagPanel extends JPanel{
	GridBagLayout gb;
	GridBagPanel(){
		gb = new GridBagLayout();
		this.setLayout(gb);
	}

	void insert(Component c, int x, int y, int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gb.setConstraints(c, gbc);
		this.add(c);
	}
	void insert(Component c, int x, int y, int w, int h,Insets inset) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = inset;
		gb.setConstraints(c, gbc);
		this.add(c);
	}
}
