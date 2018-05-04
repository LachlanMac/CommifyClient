package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class DigitalBoard extends IOBoard {

	private JButton uiButton;
	private static final int BOARD_TYPE = 1;
	private static final long serialVersionUID = 1L;
	private static final float widthMultiple = 2.4f;
	DigitalChannel[] channels;
	ImageIcon boardImg, onLED, offLED, module;
	int width, height, id;
	JPanel top, center, bottom, rxtx;
	JLabel rx, tx;

	public DigitalBoard(CommifyUI ui, NetController net, int id) {
		super(ui, net, id);
		boardType = BOARD_TYPE;

		buildUI();
		
		buildChannels();

		net.addBoard(this);

		startTransmit();
	}

	public void buildUI() {

		center = new JPanel();
		top = new JPanel();
		bottom = new JPanel();

		height = 400;
		width = (int) (height * widthMultiple);
		this.setLayout(new BorderLayout());

		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder());

		ImageIcon dbImg = new ImageIcon(
				ResourceLoader.digitalBoard.getScaledInstance((int) this.getPreferredSize().getWidth(),
						(int) this.getPreferredSize().getHeight(), Image.SCALE_SMOOTH));
		top.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .30f)));
		bottom.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .15)));
		center.setPreferredSize(new Dimension(
				(int) this.getPreferredSize().getWidth() - ((int) (this.getPreferredSize().getWidth() * .12f)),
				(int) (this.getPreferredSize().getHeight() * .65f)));
		bottom.setOpaque(false);

		center.setLayout(new FlowLayout());
		rxtx = new JPanel();
		rxtx.setBackground(new Color(0, 64, 0));

		rxtx.setLayout(new FlowLayout());
		rxtx.setPreferredSize(new Dimension((int) (top.getPreferredSize().getWidth() * .14f),
				(int) (top.getPreferredSize().getHeight() * 0.3f)));
		rxtx.setOpaque(false);

		JLabel rxLabel = new JLabel("RX");
		JLabel txLabel = new JLabel("TX");
		rxLabel.setForeground(Color.WHITE);
		txLabel.setForeground(Color.WHITE);
		rxLabel.setFont(new Font(rxLabel.getFont().getName(), Font.PLAIN, 7));

		txLabel.setFont(new Font(txLabel.getFont().getName(), Font.PLAIN, 7));

		rxLabel.setPreferredSize(new Dimension((int) (rxtx.getPreferredSize().getWidth() / 8),
				(int) (rxtx.getPreferredSize().getWidth() / 8)));
		txLabel.setPreferredSize(new Dimension((int) (rxtx.getPreferredSize().getWidth() / 8),
				(int) (rxtx.getPreferredSize().getWidth() / 8)));

		rxtx.setOpaque(true);
		onLED = new ImageIcon(ResourceLoader.digital_onLED.getScaledInstance((int) (rxtx.getPreferredSize().getWidth() / 7 + 5),
				(int) (rxtx.getPreferredSize().getWidth() / 7 + 5), Image.SCALE_SMOOTH));
		offLED = new ImageIcon(
				ResourceLoader.digital_offLED.getScaledInstance((int) (rxtx.getPreferredSize().getWidth() / 7 + 5),
						(int) (rxtx.getPreferredSize().getWidth() / 7 + 5), Image.SCALE_SMOOTH));

		tx = new JLabel(offLED);

		rx = new JLabel(offLED);

		rxtx.add(txLabel);
		rxtx.add(tx);
		rxtx.add(rxLabel);
		rxtx.add(rx);
		
		center.setOpaque(false);
		top.setOpaque(false);
		SpringLayout sLayout = new SpringLayout();
		top.setLayout(sLayout);

		sLayout.putConstraint(SpringLayout.WEST, rxtx, (int) (top.getPreferredSize().getWidth() * .3f),
				SpringLayout.WEST, top);
		sLayout.putConstraint(SpringLayout.NORTH, rxtx, (int) (top.getPreferredSize().getHeight() * .05f),
				SpringLayout.NORTH, top);
		top.add(rxtx);
		
		//System.out.println("SIZE" + bottom.getPreferredSize());
		
	
		
		
		this.setIcon(dbImg);
		this.add(top, BorderLayout.PAGE_START);
		this.add(center, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.PAGE_END);

	}

	public void notifyServer() {

		String state = null;
		// types: (cmd)(board id)(type)
		// get == g00
		// set == s11
		// add == a01
		// remove == r02
		// "3=s01=O:0-I:0-O:0-I:0-O:0-O:0-O:0-O:0-O:0-O:0-O:0-O:0-I:0-I:0-O:0-O:0-I:0-I:0-I:0-I:0-O:0-O:1-O:0-O:0"

		StringBuilder sb = new StringBuilder();

		sb.append(CommPacket.getPacketCount() + "=s" + this.boardID + "1=");

		for (int i = 0; i < channels.length - 1; i++) {

			sb.append(channels[i].getType() + ":" + channels[i].getStatus() + "-");
		}
		sb.append(channels[channels.length - 1].getType() + ":" + channels[channels.length - 1].getStatus());

		state = new String(sb);
		net.sendState(state, this);
	}

	public void notifyBoardAddRequest() {

		net.addBoardRequest(this);

	}

	@Override
	public void notifyBoardDeleteRequest() {
		net.deleteBoardRequest(this);
	}

	public void updateState(String updateString) {

		String goodString = updateString.substring(1, updateString.length() - 1);

		String[] channelData = goodString.split("-");

		for (int i = 0; i < channels.length; i++) {
			if (channelData.length != channels.length) {
				return;
			}
			channels[i].setState(channelData[i].charAt(2));
			channels[i].setType(channelData[i].charAt(0));

		}

	}

	public void startTransmit() {

		transmissionThread = new Thread() {
			public void run() {

				while (true) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (txOn == true) {
						txCounter = 0;
						txOn = false;
						setTXOff();
					}
					if (rxOn == true) {
						rxCounter = 0;
						rxOn = false;
						setRXOff();
					}

				}

			}
		};

		transmissionThread.start();

	}

	public void setRXOff() {
		rx.setIcon(offLED);
		validate();
		repaint();
	}

	public void setTXOff() {
		tx.setIcon(offLED);
		validate();
		repaint();
	}

	public void setRXOn() {
		rxOn = true;
		rx.setIcon(onLED);
		validate();
		repaint();

	}

	public void setTXOn() {

		txOn = true;
		tx.setIcon(onLED);
		validate();
		repaint();
	}

	public JPanel getCenterPanel() {
		return center;
	}

	@Override
	public JButton getBoardButton() {
		uiButton = new JButton("Digital Board: " + (boardID + 1));

		uiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeBoard();
			}

		});

		return uiButton;
	}

	public void changeBoard() {
		ui.changeBoard(this);
	}

	@Override
	public void decodePacket(byte[] incomingData) {

		String data[] = new String(incomingData).split("=");
		// returnData = MessageTypes.ADD + MessageTypes.SUCCESS + "=Board " + boardID +
		// " added successfully";
		// returnData = "aZ" + boardID + b.getBoardType() + "=added successfully";
		// azXX
		String cmdString = data[0];
		char[] cmdData = cmdString.toCharArray();

		char cmd = cmdData[0];
		char mType = cmdData[1];

		if (cmd == 'a' && mType == 'Z') {
			System.out.println("Digital Board added");
			// ADD BOARD
		} else if (cmd == 'a' && mType == 'X') {
			// ERROR ADDING BOARD
			System.out.println("Could not add board");
		} else if (cmd == 'd' && mType == 'Z') {
			// REMOVE BOARD
			System.out.println("Board deleted");
			ui.removeBoard(this);
			net.removeBoard(this);

		} else if (cmd == 'd' && mType == 'X') {
			// ERROR REMOVING BOARD
			System.out.println("Could not delete board");
		}

	}

	public JButton getUIButton() {
		return uiButton;
	}

	@Override
	public void buildChannels() {
		channels = new DigitalChannel[24];

		for (int i = 0; i < channels.length; i++) {

			channels[i] = new DigitalChannel(i, this, false, DigitalChannel.TYPE.output);
			center.add(channels[i]);
		}

	}

}
