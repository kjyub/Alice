package components;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class GridBagPanel extends JPanel{
	GridBagLayout gb;
	public GridBagPanel(){
		gb = new GridBagLayout();
		this.setLayout(gb);
	}

	public void insert(Component c, int x, int y, int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gb.setConstraints(c, gbc);
		this.add(c);
	}
	public void insert(Component c, int x, int y, int w, int h,Insets inset) {
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
