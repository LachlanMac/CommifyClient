package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DigitalChannel extends IOChannel {

	private int channelID;
	private boolean value;
	private TYPE type;
	private int height, width, moduleHeight, moduleWidth;
	private Dimension moduleDim, ioSelectorDim, ledDim;
	JButton module, ioSelector;
	JLabel led;
	JLabel channel, moduleType, fuse;
	ImageIcon offLED, onLED, outputModule, inputModule, ioIn, ioOut;
	JSlider typeSelector;

	public DigitalChannel(int channelID, IOBoard board, boolean value, TYPE type) {
		super(channelID, board);

		this.value = value;
		this.type = type;

		this.height = (int) (board.getCenterPanel().getPreferredSize().getHeight() / 1.5) - 1;
		this.width = (int) board.getCenterPanel().getPreferredSize().getWidth() / 24 - 5;
		this.setPreferredSize(new Dimension(width, height));
		this.moduleHeight = (int) (this.getPreferredSize().getHeight());
		this.moduleWidth = (int) (this.getPreferredSize().getWidth());
		this.setBackground(ResourceLoader.digitalGreen);
		this.setOpaque(true);

		this.setLayout(new BorderLayout());

		moduleDim = new Dimension((int) (moduleWidth - 1), (int) (moduleHeight * .60f));
		ioSelectorDim = new Dimension((int) (moduleWidth - 5), (int) ((moduleWidth - 5) * 1.5));
		ledDim = new Dimension((int) (moduleWidth - 5), (int) (moduleWidth - 5));

		led = new JLabel(onLED);

		module = new JButton();
		module.setPreferredSize(moduleDim);
		module.setBackground(ResourceLoader.digitalGreen);
		module.setOpaque(false);
		module.setBorderPainted(true);
		module.setMargin(new Insets(0, 0, 0, 0));
		module.setBorder(null);
		module.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeState();
			}
		});

		ioSelector = new JButton();
		ioSelector.setOpaque(true);
		ioSelector.setBackground(ResourceLoader.digitalGreen);
		ioSelector.setBorderPainted(true);
		ioSelector.setMargin(new Insets(0, 0, 0, 0));
		ioSelector.setBorder(null);
		ioSelector.setPreferredSize(ioSelectorDim);
		ioSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				changeType();
			}

		});
		loadImages();
		drawUI();
	}

	public void changeType() {

		if (this.type == TYPE.output) {
			// change to input
			this.type = TYPE.input;
			board.notifyServer();
			drawModule();

		} else if (this.type == TYPE.input) {
			// change to output
			this.type = TYPE.output;
			board.notifyServer();
			drawModule();

		}
		this.validate();
		this.repaint();

	}

	public void changeState() {
		if (this.type == TYPE.output) {
			return;
		}
		if (this.value == false) {
			// turn on
			setOn(true);
		} else {
			setOff(true);
		}

	}

	public void drawModule() {
		if (this.type == TYPE.input) {

			moduleType.setIcon(inputModule);
			fuse.setIcon(ioIn);

		} else {

			moduleType.setIcon(outputModule);
			fuse.setIcon(ioOut);
		}

		validate();
		repaint();
	}

	public void setOn(boolean notify) {

		led.setIcon(onLED);
		this.value = true;
		if (notify)
			board.notifyServer();
		validate();
		repaint();

	}

	public void setOff(boolean notify) {

		led.setIcon(offLED);
		this.value = false;
		if (notify)
			board.notifyServer();
		validate();
		repaint();
	}

	public void drawUI() {
		moduleType = new JLabel(inputModule);
		moduleType.setBackground(ResourceLoader.digitalGreen);
		moduleType.setOpaque(true);
		fuse = new JLabel(ioIn);

		fuse.setPreferredSize(ioSelectorDim);
		moduleType.setPreferredSize(moduleDim);

		if (this.type == TYPE.input) {
			module.setBackground(Color.yellow);
			moduleType.setIcon(inputModule);
			fuse.setIcon(ioIn);
			module.add(moduleType);
			ioSelector.add(fuse);

		} else {
			module.setBackground(Color.BLACK);
			moduleType.setIcon(outputModule);
			fuse.setIcon(ioOut);
			module.add(moduleType);
			ioSelector.add(fuse);

		}
		if (value == true) {
			led = new JLabel(onLED);
		} else {
			led = new JLabel(offLED);
		}
		channel = new JLabel("C:" + Integer.toString(channelID));

		this.add(module, BorderLayout.CENTER);
		this.add(ioSelector, BorderLayout.PAGE_END);
		this.add(led, BorderLayout.PAGE_START);

	}

	private int getBinaryPosition() {
		if (this.type == TYPE.input) {
			typeSelector.setValue(1);
			return 1;

		} else {
			typeSelector.setValue(0);
			return 0;
		}
	}

	public void loadImages() {

		ioIn = new ImageIcon(ResourceLoader.ioSelectorIn.getScaledInstance((int) ioSelectorDim.getWidth(),
				(int) ioSelectorDim.getHeight(), Image.SCALE_SMOOTH));
		ioOut = new ImageIcon(ResourceLoader.ioSelectorOut.getScaledInstance((int) ioSelectorDim.getWidth(),
				(int) ioSelectorDim.getHeight(), Image.SCALE_SMOOTH));

		offLED = new ImageIcon(ResourceLoader.digital_offLED.getScaledInstance((int) ledDim.getWidth(),
				(int) ledDim.getWidth(), Image.SCALE_SMOOTH));
		onLED = new ImageIcon(ResourceLoader.digital_onLED.getScaledInstance((int) ledDim.getWidth(),
				(int) ledDim.getWidth(), Image.SCALE_SMOOTH));

		outputModule = new ImageIcon(ResourceLoader.outputModule.getScaledInstance((int) moduleDim.getWidth(),
				(int) moduleDim.getHeight(), Image.SCALE_SMOOTH));
		inputModule = new ImageIcon(ResourceLoader.inputModule.getScaledInstance((int) moduleDim.getWidth(),
				(int) moduleDim.getHeight(), Image.SCALE_SMOOTH));

	}

	public char getType() {
		if (this.type == TYPE.input) {
			return 'I';
		} else {
			return 'O';
		}

	}

	public char getStatus() {
		if (this.value == true) {
			return '1';
		} else {
			return '0';
		}

	}

	public void setState(char val) {
		if (val == '1' && this.value == false) {
			setOn(false);
		} else if (val == '0' && this.value == true) {
			setOff(false);
		}

	}

	public void setType(char val) {

		if (val == 'I' && this.type == TYPE.output) {
			this.type = TYPE.input;
			this.value = false;

			drawModule();
		} else if (val == 'O' && this.type == TYPE.input) {
			this.type = TYPE.output;
			this.value = false;
			drawModule();
		}

	}
}
