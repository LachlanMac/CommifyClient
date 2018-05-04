package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.frick.lmac.main.IOChannel.TYPE;

public class AnalogBoard extends IOBoard {

	private static final int BOARD_TYPE = 2;
	private static final long serialVersionUID = 1L;
	private static final float widthMultiple = 2.5f;
	AnalogChannel[] channels;
	ImageIcon boardImg, onLED, offLED;
	int width, height, id;
	JPanel top, center, bottom, rxtx, input1, input2, output, rpm, bottom_buffer;
	JLabel rx, tx;
	JButton uiButton;
	JLabel chan1_2, chan3_4, chan5_6, chan7_8, chan9_10, chan11_12, chan13_14, chan15_16, chan17_18, chan19_20,
			chan21_22, chan23_24, oChan1_4, oChan5_8;
	JLabel[] input1Channels, input2Channels;
	ImageIcon chan1_2_Icon, chan3_4_Icon, chan5_6_Icon, chan7_8_Icon, chan9_10_Icon, chan11_12_Icon, chan13_14_Icon,
			chan15_16_Icon, chan17_18_Icon, chan19_20_Icon, chan21_22_Icon, chan23_24_Icon, oChan1_4_Icon,
			oChan5_8_Icon;

	ArrayList<AnalogSelector> selectors;

	Dimension input1ChannelDimension, input2ChannelDimension, outputChannelDimension;

	public AnalogBoard(CommifyUI ui, NetController net, int boardID) {
		super(ui, net, boardID);
		this.boardType = BOARD_TYPE;
		selectors = new ArrayList<AnalogSelector>();

		buildUI();
		loadImages();
		buildChannelSelectors();
		net.addBoard(this);

		startTransmit();

	}

	@Override
	public void buildUI() {

		center = new JPanel();
		top = new JPanel();
		bottom = new JPanel();
		input1 = new JPanel();
		input2 = new JPanel();
		output = new JPanel();
		bottom_buffer = new JPanel();
		rpm = new JPanel();

		height = 500;
		width = (int) (height * widthMultiple);
		this.setLayout(new BorderLayout());

		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder());

		ImageIcon dbImg = new ImageIcon(
				ResourceLoader.analogBoard.getScaledInstance((int) this.getPreferredSize().getWidth(),
						(int) this.getPreferredSize().getHeight(), Image.SCALE_SMOOTH));
		top.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .25f)));
		center.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .40f)));
		bottom.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .30f)));

		input1.setPreferredSize(new Dimension((int) (bottom.getPreferredSize().getWidth() / 2.2),
				(int) bottom.getPreferredSize().getHeight()));
		input2.setPreferredSize(new Dimension((int) (bottom.getPreferredSize().getWidth() / 4.4),
				(int) bottom.getPreferredSize().getHeight()));
		output.setPreferredSize(new Dimension((int) (bottom.getPreferredSize().getWidth() / 7.0),
				(int) bottom.getPreferredSize().getHeight() / 2));
		bottom_buffer.setPreferredSize(new Dimension((int) (bottom.getPreferredSize().getWidth() / 8),
				(int) bottom.getPreferredSize().getHeight()));

		bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
		buildChannels();
		input1.setBackground(ResourceLoader.analogRed);
		input2.setBackground(ResourceLoader.analogRed);
		output.setBackground(ResourceLoader.analogRed);
		bottom_buffer.setBackground(ResourceLoader.analogRed);

		input1ChannelDimension = new Dimension((int) (input1.getPreferredSize().getWidth() / 4 - 2),
				(int) (input1.getPreferredSize().getHeight() / 2));
		input2ChannelDimension = new Dimension((int) (input2.getPreferredSize().getWidth() / 2 - 2),
				(int) (input2.getPreferredSize().getHeight() / 2));
		outputChannelDimension = new Dimension((int) (output.getPreferredSize().getWidth() - 2),
				(int) (input2.getPreferredSize().getHeight() / 2));

		center.setLayout(new FlowLayout());
		rxtx = new JPanel();
		rxtx.setBackground(ResourceLoader.analogRed);

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
		onLED = new ImageIcon(ResourceLoader.analog_onLED.getScaledInstance((int) (rxtx.getPreferredSize().getWidth() / 7 + 5),
				(int) (rxtx.getPreferredSize().getWidth() / 7 + 5), Image.SCALE_SMOOTH));
		offLED = new ImageIcon(
				ResourceLoader.analog_offLED.getScaledInstance((int) (rxtx.getPreferredSize().getWidth() / 7 + 5),
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
		this.setIcon(dbImg);
		this.add(top, BorderLayout.PAGE_START);
		this.add(center, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.PAGE_END);

	}

	public void buildChannelSelectors() {
		input1Channels = new JLabel[] { chan1_2, chan5_6, chan9_10, chan13_14, chan3_4, chan7_8, chan11_12, chan15_16 };
		input2Channels = new JLabel[] { chan17_18, chan21_22, chan19_20, chan23_24 };

		for (int i = 0; i < 12; i += 1) {

			AnalogSelector a1 = new AnalogSelector((i * 2) + 1, this);
			AnalogSelector a2 = new AnalogSelector((i * 2) + 2, this);

			selectors.add(a1);
			selectors.add(a2);

		}

		input1Channels[0].add(selectors.get(0));
		input1Channels[0].add(selectors.get(1));
		input1Channels[1].add(selectors.get(4));
		input1Channels[1].add(selectors.get(5));
		input1Channels[2].add(selectors.get(8));
		input1Channels[2].add(selectors.get(9));
		input1Channels[3].add(selectors.get(12));
		input1Channels[3].add(selectors.get(13));
		input1Channels[4].add(selectors.get(2));
		input1Channels[4].add(selectors.get(3));
		input1Channels[5].add(selectors.get(6));
		input1Channels[5].add(selectors.get(7));
		input1Channels[6].add(selectors.get(10));
		input1Channels[6].add(selectors.get(11));
		input1Channels[7].add(selectors.get(14));
		input1Channels[7].add(selectors.get(15));

		input2Channels[0].add(selectors.get(16));
		input2Channels[0].add(selectors.get(17));
		input2Channels[1].add(selectors.get(20));
		input2Channels[1].add(selectors.get(21));
		input2Channels[2].add(selectors.get(18));
		input2Channels[2].add(selectors.get(19));
		input2Channels[3].add(selectors.get(22));
		input2Channels[3].add(selectors.get(23));

		input1.setLayout(new GridLayout(2, 4, 6, 10));
		input2.setLayout(new GridLayout(2, 2, 6, 10));
		output.setLayout(new GridLayout(2, 1, 6, 10));

		System.out.println(input1Channels.length);
		for (int i = 0; i < input1Channels.length; i++) {

			input1.add(input1Channels[i]);

		}
		for (int i = 0; i < input2Channels.length; i++) {

			input2.add(input2Channels[i]);

		}

		output.add(oChan1_4);
		output.add(oChan5_8);
		bottom.add(input1);
		bottom.add(bottom_buffer);
		bottom.add(input2);
		bottom.add(output);
	}

	@Override
	public void buildChannels() {

		channels = new AnalogChannel[32];
		for (int i = 0; i < 24; i++) {

			channels[i] = new AnalogChannel(i, this, 100, TYPE.input);
			// input.add(channels[i]);
		}

		for (int j = 24; j < channels.length; j++) {
			channels[j] = new AnalogChannel(j, this, 0, TYPE.output);
			// output.add(channels[j]);
		}

	}

	@Override
	public void updateState(String updateString) {
		// System.out.println("ANALOG RECEIVED " + updateString);

		String goodString = updateString.substring(1, updateString.length() - 1);

		String[] dataString = goodString.split("-");

		for (int i = 0; i < channels.length; i++) {
			if (dataString.length != channels.length) {
				return;
			}

			String[] channelData = dataString[i].split(":");

			if (channels[i].getType() == 'O') {
				channels[i].setValue(Integer.parseInt(channelData[1]));
			}

		}

	}

	@Override
	public void notifyServer() {

		String state = null;

		StringBuilder sb = new StringBuilder();

		sb.append(CommPacket.getPacketCount() + "=s" + this.boardID + "2=");

		for (int i = 0; i < 23; i++) {

			sb.append(
					channels[i].getType() + ":" + channels[i].getStatus() + ":" + channels[i].getConfiguration() + "-");
		}
		sb.append(channels[23].getType() + ":" + channels[23].getStatus() + ":" + channels[23].getConfiguration());

		state = new String(sb);
		net.sendState(state, this);


	}

	public JPanel getCenterPanel() {
		return center;
	}

	@Override
	public JButton getBoardButton() {
		uiButton = new JButton("Analog Board: " + (this.boardID + 1));

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
			System.out.println("Analog Board added");
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

	@Override
	public void notifyBoardAddRequest() {
		net.addBoardRequest(this);

	}

	@Override
	public void notifyBoardDeleteRequest() {
		net.deleteBoardRequest(this);
	}

	@Override
	public JButton getUIButton() {
		// TODO Auto-generated method stub
		return null;
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

	public void loadImages() {

		chan1_2_Icon = new ImageIcon(ResourceLoader.chan1_2.getScaledInstance((int) input1ChannelDimension.getWidth(),
				(int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan3_4_Icon = new ImageIcon(ResourceLoader.chan3_4.getScaledInstance((int) input1ChannelDimension.getWidth(),
				(int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan5_6_Icon = new ImageIcon(ResourceLoader.chan5_6.getScaledInstance((int) input1ChannelDimension.getWidth(),
				(int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan7_8_Icon = new ImageIcon(ResourceLoader.chan7_8.getScaledInstance((int) input1ChannelDimension.getWidth(),
				(int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan9_10_Icon = new ImageIcon(ResourceLoader.chan9_10.getScaledInstance((int) input1ChannelDimension.getWidth(),
				(int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan11_12_Icon = new ImageIcon(ResourceLoader.chan11_12.getScaledInstance(
				(int) input1ChannelDimension.getWidth(), (int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan13_14_Icon = new ImageIcon(ResourceLoader.chan13_14.getScaledInstance(
				(int) input1ChannelDimension.getWidth(), (int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan15_16_Icon = new ImageIcon(ResourceLoader.chan15_16.getScaledInstance(
				(int) input1ChannelDimension.getWidth(), (int) input1ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan17_18_Icon = new ImageIcon(ResourceLoader.chan17_18.getScaledInstance(
				(int) input2ChannelDimension.getWidth(), (int) input2ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan19_20_Icon = new ImageIcon(ResourceLoader.chan19_20.getScaledInstance(
				(int) input2ChannelDimension.getWidth(), (int) input2ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan21_22_Icon = new ImageIcon(ResourceLoader.chan21_22.getScaledInstance(
				(int) input2ChannelDimension.getWidth(), (int) input2ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		chan23_24_Icon = new ImageIcon(ResourceLoader.chan23_24.getScaledInstance(
				(int) input2ChannelDimension.getWidth(), (int) input2ChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		oChan1_4_Icon = new ImageIcon(ResourceLoader.oChan1_4.getScaledInstance((int) outputChannelDimension.getWidth(),
				(int) outputChannelDimension.getHeight(), Image.SCALE_SMOOTH));
		oChan5_8_Icon = new ImageIcon(ResourceLoader.oChan5_8.getScaledInstance((int) outputChannelDimension.getWidth(),
				(int) outputChannelDimension.getHeight(), Image.SCALE_SMOOTH));

		chan1_2 = new JLabel(chan1_2_Icon);
		chan1_2.setLayout(new GridLayout(0, 2, 0, 0));
		chan3_4 = new JLabel(chan3_4_Icon);
		chan3_4.setLayout(new GridLayout(0, 2, 0, 0));
		chan5_6 = new JLabel(chan5_6_Icon);
		chan5_6.setLayout(new GridLayout(0, 2, 0, 0));
		chan7_8 = new JLabel(chan7_8_Icon);
		chan7_8.setLayout(new GridLayout(0, 2, 0, 0));
		chan9_10 = new JLabel(chan9_10_Icon);
		chan9_10.setLayout(new GridLayout(0, 2, 0, 0));
		chan11_12 = new JLabel(chan11_12_Icon);
		chan11_12.setLayout(new GridLayout(0, 2, 0, 0));
		chan13_14 = new JLabel(chan13_14_Icon);
		chan13_14.setLayout(new GridLayout(0, 2, 0, 0));
		chan15_16 = new JLabel(chan15_16_Icon);
		chan15_16.setLayout(new GridLayout(0, 2, 0, 0));
		chan17_18 = new JLabel(chan17_18_Icon);
		chan17_18.setLayout(new GridLayout(0, 2, 0, 0));
		chan19_20 = new JLabel(chan19_20_Icon);
		chan19_20.setLayout(new GridLayout(0, 2, 0, 0));
		chan21_22 = new JLabel(chan21_22_Icon);
		chan21_22.setLayout(new GridLayout(0, 2, 0, 0));
		chan23_24 = new JLabel(chan23_24_Icon);
		chan23_24.setLayout(new GridLayout(0, 2, 0, 0));
		oChan1_4 = new JLabel(oChan1_4_Icon);
		oChan5_8 = new JLabel(oChan5_8_Icon);
	}

	public void unselectAll() {
		for (AnalogSelector selector : selectors) {
			selector.deselect();
		}
	}

	public void updateCenter() {
		center.validate();
		center.repaint();
	}

	public AnalogChannel getChannelByID(int id) {
		return channels[id - 1];
	}

}
