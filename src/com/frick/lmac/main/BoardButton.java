package com.frick.lmac.main;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BoardButton extends JButton {
	IOBoard b;
	JPanel p;
	CommifyUI ui;
	Menu menu;

	public BoardButton(JPanel p, IOBoard b, CommifyUI ui, Menu menu) {

		this.ui = ui;
		this.p = p;
		this.b = b;
		this.menu = menu;

		if (b.getBoardType() == 1) {
			this.setBackground(ResourceLoader.digitalGreen);
			this.setOpaque(true);
			this.setBorderPainted(true);
			this.setMargin(new Insets(0, 0, 0, 0));
			this.setBorder(null);
			// this.setForeground(Color.white);
			this.setText("Digital Board " + (b.getBoardID() + 1));

			this.validate();
			this.repaint();

		} else {
			this.setBackground(ResourceLoader.analogRed);
			this.setOpaque(true);
			this.setBorderPainted(true);
			this.setMargin(new Insets(0, 0, 0, 0));
			this.setBorder(null);
			// this.setForeground(Color.white);
			this.setText("Analog Board " + (b.getBoardID() + 1));

			this.validate();
			this.repaint();
		}

	}

	public JPanel getParentPanel() {
		return p;
	}

	public JButton getXButton() {

		JButton button = new JButton("X");
		this.setBackground(Color.RED);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("CALLED IN XBUTTON");
				ui.removeBoardRequest(b);
				menu.removeFromBoardDisplay(p);
			}

		});
		return button;

	}

}
