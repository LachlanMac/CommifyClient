package com.frick.lmac.main;

import javax.swing.JPanel;

public class IOChannel extends JPanel {
	protected enum TYPE {
		input, output
	};

	protected int channelID;
	protected IOBoard board;

	public IOChannel(int channelID, IOBoard board) {
		this.channelID = channelID;
		this.board = board;
	}

}
