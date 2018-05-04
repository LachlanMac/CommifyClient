package com.frick.lmac.main;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class IOBoard extends JLabel {

	protected int boardType;
	protected int boardID;
	protected boolean isNewBoard = true;
	protected Thread transmissionThread;
	protected boolean txOn = false, rxOn = false;
	protected int txCounter = 0, rxCounter = 0;
	protected int ledTimeOut = 1000;

	protected NetController net;
	protected CommifyUI ui;

	public IOBoard(CommifyUI ui, NetController net, int boardID) {
		this.ui = ui;
		this.boardID = boardID;
		this.net = net;
		System.out.println("Board created with ID " + boardID);
	}

	public int getBoardType() {
		return boardType;
	}

	public int getBoardID() {
		return boardID;
	}

	public abstract void decodePacket(byte[] data);

	public abstract void buildUI();

	public abstract void notifyBoardAddRequest();

	public abstract void notifyBoardDeleteRequest();

	public abstract void updateState(String updateString);

	public abstract void notifyServer();

	public abstract JPanel getCenterPanel();

	public abstract JButton getBoardButton();

	public abstract void setRXOff();

	public abstract void setTXOff();

	public abstract void setRXOn();

	public abstract void setTXOn();

	public abstract JButton getUIButton();

	public void activateBoard() {
		this.isNewBoard = false;
	}

	public boolean isActivated() {
		return this.isNewBoard;
	}

	public abstract void buildChannels();

}