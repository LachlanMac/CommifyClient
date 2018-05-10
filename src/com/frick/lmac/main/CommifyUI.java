package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CommifyUI extends JFrame {

	JPanel controllerPanel, analogPanel_1, analogPanel_2, digitalPanel_1, digitalPanel_2, statusPanel, boardPanel,
			boardSelectorPanel, backDrop;
	ArrayList<IOBoard> boardList;
	JComboBox aBoardCount, dBoardCount;
	JButton submitBoardCount;
	NetController net;

	JButton menuButton;
	Menu menu;

	public CommifyUI(NetController net) {
		this.net = net;
		this.menu = new Menu(net, this);
		boardList = new ArrayList<IOBoard>();
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
		boardPanel.add(menu);

		// addTestBoards();

	}

	public void addTestBoards() {

	}

	public void removeBoardRequest(IOBoard b) {
		System.out.println("Making request to remove board");
		b.notifyBoardDeleteRequest();
		System.out.println("Removing Board");
		boardList.remove(b);
		
		if(b.getUIButton() == null) {
			System.out.println("WTF WHY IS GUIBUTTON NUL?");
		}
		controllerPanel.remove(b.getUIButton());
		changeBoard(null);

		controllerPanel.validate();
		controllerPanel.repaint();
		
	}

	

	public void addBoard(IOBoard b) {
		boardList.add(b);
		controllerPanel.add(b.getBoardButton());
		b.notifyBoardAddRequest();

		controllerPanel.validate();
		controllerPanel.repaint();

	}

	public void changeBoard(IOBoard b) {
		if (b == null) {

			boardPanel.removeAll();
			boardPanel.add(menu);
			// add menu
		} else {
			System.out.println("Changing board");
			boardPanel.removeAll();
			boardPanel.add(b);
		}
		validate();
		repaint();

	};

	public ArrayList<IOBoard> getBoards() {
		return boardList;
	}

	public ArrayList<IOBoard> getAnalogBoards() {

		ArrayList<IOBoard> aBoards = new ArrayList<IOBoard>();

		for (IOBoard b : boardList) {
			if (b.getBoardType() == 2) {
				aBoards.add(b);
			}
		}
		return aBoards;
	}

	public ArrayList<IOBoard> getDigitalBoards() {

		ArrayList<IOBoard> dBoards = new ArrayList<IOBoard>();

		for (IOBoard b : boardList) {
			if (b.getBoardType() == 1) {
				System.out.println("RETURNING BAORD " + b.getBoardID());
				dBoards.add(b);
			}
		}
		return dBoards;
	}
}
