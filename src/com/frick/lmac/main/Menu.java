package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Menu extends JPanel {

	private int width, height;

	private JTextField ipField, serverPortField, clientPortField;
	JPanel title, center, bottom, boardConfig, addBoardPopup, boardDisplay, statusPanel;
	JButton addBoardBtn, loadBoardBtn, connectBtn;
	JLabel connectionStatus;
	NetController net;
	CommifyUI ui;
	Thread connectionListener;

	public Menu(NetController net, CommifyUI ui) {
		this.net = net;
		this.ui = ui;
		buildUI();
	}

	public void buildUI() {

		height = 800;
		width = (int) (height);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder());

		title = new JPanel();
		title.setBackground(ResourceLoader.analogRed);
		JLabel titleLabel = new JLabel("Commify Client Set-Up");
		titleLabel.setForeground(Color.WHITE);
		title.add(titleLabel);
		title.setPreferredSize(new Dimension(width, (int) (height * 0.1f)));
		title.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

		center = new JPanel();
		center.setLayout(new GridLayout(5, 1));
		center.setPreferredSize(new Dimension(width, (int) (height * 0.3f)));
		center.setBorder(BorderFactory.createLineBorder(Color.black));

		buildCenter();

		bottom = new JPanel();
		bottom.setLayout(new GridLayout(5, 1));
		bottom.setPreferredSize(new Dimension(width, (int) (height * 0.6f)));
		bottom.setBorder(BorderFactory.createLineBorder(Color.black));

		buildBottom();

		this.add(title, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);

		startListener();
	}

	public void buildCenter() {

		JPanel netSettingTitle = new JPanel();
		netSettingTitle.setBackground(Color.black);
		netSettingTitle.setPreferredSize(new Dimension((int) title.getPreferredSize().getWidth(),
				(int) title.getPreferredSize().getHeight() / 2));
		JLabel netSettingTitleString = new JLabel("Network Settings");
		netSettingTitleString.setFont(new Font(netSettingTitleString.getFont().getName(), Font.BOLD, 14));
		netSettingTitleString.setForeground(Color.WHITE);
		netSettingTitle.add(netSettingTitleString);

		JPanel ipPanel = new JPanel();
		ipPanel.add(new JLabel("Server Address"));
		ipField = new JTextField(20);
		ipPanel.add(ipField);

		JPanel portPanel = new JPanel();
		portPanel.add(new JLabel("Server Port"));
		serverPortField = new JTextField(4);
		portPanel.add(serverPortField);

		portPanel.add(new JLabel("Client Port"));
		clientPortField = new JTextField(4);
		portPanel.add(clientPortField);

		JPanel connectPanel = new JPanel();

		connectBtn = new JButton("Connect");
		connectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (net.isAlive()) {

				} else {
					net.start();
				}

			}

		});
		connectPanel.add(connectBtn);

		statusPanel = new JPanel();
		connectionStatus = new JLabel();
		connectionStatus.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

		connectionStatus.setText("Status : Disconnected");
		statusPanel.setBackground(Color.red);
		connectionStatus.setForeground(Color.white);
		statusPanel.add(connectionStatus);

		center.add(netSettingTitle);
		center.add(ipPanel);
		center.add(portPanel);
		center.add(connectPanel);
		center.add(statusPanel);

		loadSettings();

	}

	public void buildBottom() {

		JPanel boardSettingsTitle = new JPanel();
		boardSettingsTitle.setBackground(Color.black);
		boardSettingsTitle.setPreferredSize(new Dimension((int) title.getPreferredSize().getWidth(),
				(int) title.getPreferredSize().getHeight() / 2));
		JLabel boardSettingsTitleString = new JLabel("I/O Board Configuration");
		boardSettingsTitleString.setFont(new Font(boardSettingsTitleString.getFont().getName(), Font.BOLD, 14));
		boardSettingsTitleString.setForeground(Color.WHITE);
		boardSettingsTitle.add(boardSettingsTitleString);

		bottom.add(boardSettingsTitle);

		buildBoardConfigurator();

	}

	public void loadSettings() {
		ipField.setText(SettingsLoader.SETTINGS[0]);
		serverPortField.setText(SettingsLoader.SETTINGS[1]);
		clientPortField.setText(SettingsLoader.SETTINGS[2]);

	}

	public void buildBoardConfigurator() {

		boardConfig = new JPanel();
		addBoardBtn = new JButton("Add New Board");

		addBoardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				popUpAdd();
			}

		});

		loadBoardBtn = new JButton("Load Board");

		loadBoardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				popUpLoad();

			}

		});

		boardDisplay = new JPanel();
		boardDisplay.setLayout(new GridLayout(2, 4, 5, 5));

		boardConfig.add(addBoardBtn);
		boardConfig.add(loadBoardBtn);
		bottom.add(boardConfig);
		bottom.add(boardDisplay);

	}

	public void popUpLoad() {

		FileNameExtensionFilter filter = new FileNameExtensionFilter("BDX Files", "bdx");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(boardConfig);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

		} else {

		}

	}

	public void popUpAdd() {
		// addBoardPopup = new JPanel();

		Object[] possibilities = { "Analog", "Digital" };
		String s = (String) JOptionPane.showInputDialog(bottom, "Select Board Type", "Board Configuration",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "WHAT");

		// If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			boolean[] digitalIDs = new boolean[] { false, false, false, false };
			boolean[] analogIDs = new boolean[] { false, false, false, false };
			if (s.equals("Analog")) {
				int boardID = 0;
				ArrayList<IOBoard> boards = ui.getAnalogBoards();
				if (boards.size() == 0) {

					boardID = 0;
				} else {

					for (IOBoard b : boards) {
						int currentID = b.getBoardID();
						analogIDs[currentID] = true;
					}
					for (int i = 0; i < analogIDs.length; i++) {
						if (analogIDs[i] == false) {
							boardID = i;
							break;
						}
					}

				}

				AnalogBoard b = new AnalogBoard(ui, net, this, boardID);
				ui.addBoard(b);

				JPanel aBoardPanel = new JPanel();
				aBoardPanel.setLayout(new BorderLayout());
				BoardButton aButton = b.getBoardMenuButton(aBoardPanel);

				aBoardPanel.add(aButton.getXButton(), BorderLayout.WEST);

				aBoardPanel.add(b.getBoardMenuButton(aBoardPanel), BorderLayout.CENTER);

				boardDisplay.add(aBoardPanel);

				boardDisplay.validate();
				boardDisplay.repaint();

			} else if (s.equals("Digital")) {
				int boardID = 0;
				ArrayList<IOBoard> dboards = ui.getDigitalBoards();
				if (dboards.size() == 0) {
					System.out.println("SIZE = 0");
					boardID = 0;
				} else {

					for (IOBoard b : dboards) {
						int currentID = b.getBoardID();
						digitalIDs[currentID] = true;
					}
					for (int i = 0; i < digitalIDs.length; i++) {
						if (digitalIDs[i] == false) {
							boardID = i;
							break;
						}
					}

				}

				DigitalBoard b = new DigitalBoard(ui, net, this, boardID);
				ui.addBoard(b);

				JPanel dBoardPanel = new JPanel();
				dBoardPanel.setLayout(new BorderLayout());
				BoardButton dButton = b.getBoardMenuButton(dBoardPanel);

				dBoardPanel.add(dButton.getXButton(), BorderLayout.WEST);
				dBoardPanel.add(b.getBoardMenuButton(dBoardPanel), BorderLayout.CENTER);
				boardDisplay.add(dBoardPanel);
				boardDisplay.validate();
				boardDisplay.repaint();

			}
			return;
		}
	}

	public void removeFromBoardDisplay(JPanel p) {

		boardDisplay.remove(p);
		boardDisplay.validate();
		boardDisplay.repaint();

	}

	public void setConnection(boolean connected) {

		if (connected) {

			connectionStatus.setText("Status : Connected");
			statusPanel.setBackground(Color.GREEN);
			statusPanel.validate();
			statusPanel.repaint();
			addBoardBtn.setEnabled(true);
			loadBoardBtn.setEnabled(true);
			connectBtn.setEnabled(false);

		}
		if (!connected) {

			connectionStatus.setText("Status : Disconnected");
			statusPanel.setBackground(Color.RED);
			statusPanel.validate();
			statusPanel.repaint();
			addBoardBtn.setEnabled(false);
			loadBoardBtn.setEnabled(false);
			connectBtn.setEnabled(true);
		}
	}

	public void startListener() {

		connectionListener = new Thread() {
			public void run() {

				while (true) {

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (net.getConnectionStatus()) {
						setConnection(true);
					} else {
						setConnection(false);
					}

				}

			}
		};
		connectionListener.start();
	}
}
