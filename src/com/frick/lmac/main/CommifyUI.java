package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CommifyUI extends JFrame {

	JPanel controllerPanel, analogPanel_1, analogPanel_2, digitalPanel_1, digitalPanel_2, statusPanel, boardPanel,
			boardSelectorPanel, backDrop;

	JComboBox aBoardCount, dBoardCount;
	JButton submitBoardCount;
	NetController net;

	JButton menuButton;

	public CommifyUI(NetController net) {
		this.net = net;
		setGUI();

	}

	public void setGUI() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("Commify");
		this.setSize(screenSize);

		boardPanel = new JPanel();
		controllerPanel = new JPanel();
		statusPanel = new JPanel();
		backDrop = new JPanel();
		boardPanel.setOpaque(false);
		backDrop.setBackground(Color.BLACK);
		backDrop.setOpaque(true);
		backDrop.setLayout(new BorderLayout());
		this.add(backDrop);
		buildControllerPanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		backDrop.add(controllerPanel, BorderLayout.PAGE_START);
		backDrop.add(boardPanel, BorderLayout.CENTER);
		backDrop.add(statusPanel, BorderLayout.PAGE_END);

		this.setVisible(true);

	}

	public void buildControllerPanel() {

		menuButton = new JButton("Menu");

		menuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeBoard(null);
			}

		});
		
		controllerPanel.add(menuButton);
		
		
		DigitalBoard d1 = new DigitalBoard(this, net, 0);
		AnalogBoard a1 = new AnalogBoard(this, net, 0);
		DigitalBoard d2 = new DigitalBoard(this, net, 1);
		AnalogBoard a2 = new AnalogBoard(this, net, 1);
		addBoard(d1);
		addBoard(a1);
		addBoard(d2);
		addBoard(a2);
		System.out.println("Notifying to add board");
		
		d1.notifyBoardAddRequest();
		d2.notifyBoardAddRequest();
		a1.notifyBoardAddRequest();
		a2.notifyBoardAddRequest();
		
	}

	
	public void removeBoard(IOBoard b) {
		
		controllerPanel.remove(b.getUIButton());
		changeBoard(null);
		
	}
	public void addBoard(IOBoard b) {

		controllerPanel.add(b.getBoardButton());

	}

	public void changeBoard(IOBoard b) {
		if (b == null) {

			boardPanel.removeAll();
			boardPanel.add(new JPanel());
			//add menu
		} else {
			System.out.println("Changing board");
			boardPanel.removeAll();
			boardPanel.add(b);
		}
		validate();
		repaint();

	};
}
