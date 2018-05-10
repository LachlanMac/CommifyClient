package com.frick.lmac.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnalogSelector extends JButton {

	private boolean selected = false;
	private int channelID;
	AnalogChannel channel;
	AnalogBoard board;
	JPanel infoPanel, sliderPanel, valuePanel, titlePanel;
	JSlider channelSlider;
	JLabel min, max, value;

	public AnalogSelector(int channelID, AnalogBoard board) {
		this.setFont(this.getFont().deriveFont(8f));
		this.setFont(this.getFont().deriveFont(Font.BOLD));
		this.channelID = channelID;
		this.board = board;
		this.channel = board.getChannelByID(channelID);

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension((int) (board.getCenterPanel().getPreferredSize().getWidth() * 0.8f),
				(int) board.getCenterPanel().getPreferredSize().getHeight() / 2));
		buildPanel();

		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setBorder(null);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.setBackground(new Color(200, 200, 200, 0));

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (selected == false) {
					select();
				} else {
					deselect();
				}

			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public void select() {
		board.unselectAll();
		board.getCenterPanel().add(infoPanel);
		this.setOpaque(false);
		this.selected = true;
		this.setBackground(new Color(200, 200, 200, 80));

		System.out.println("Channel " + channelID + " selected");
		board.updateCenter();
	}

	public void deselect() {

		board.getCenterPanel().removeAll();
		this.setOpaque(false);
		this.selected = false;
		this.setBackground(new Color(200, 200, 200, 0));
		this.validate();
		this.repaint();
		board.updateCenter();

	}

	public boolean isSelected() {
		return selected;
	}

	public void buildPanel() {
		infoPanel.setLayout(new BorderLayout());
		String channelType;
		if (channel.getType() == 'O') {
			channelType = "Output";
		} else {
			channelType = "Input";
		}
		sliderPanel = new JPanel();
		valuePanel = new JPanel();
		titlePanel = new JPanel();
		valuePanel.setPreferredSize(new Dimension((int) infoPanel.getPreferredSize().getWidth(),
				(int) (infoPanel.getPreferredSize().getHeight() / 2.5)));
		sliderPanel.setPreferredSize(new Dimension((int) infoPanel.getPreferredSize().getWidth(),
				(int) infoPanel.getPreferredSize().getHeight() / 2));
		channelSlider = new JSlider(JSlider.HORIZONTAL);
		channelSlider.setMaximum(4095);
		channelSlider.setMinimum(0);
		channelSlider.setPaintTicks(true);
		channelSlider.setPreferredSize(new Dimension((int) sliderPanel.getPreferredSize().getWidth() / 2,
				(int) sliderPanel.getPreferredSize().getHeight() / 2));

		min = new JLabel();
		max = new JLabel();

		min.setText("0 %");
		max.setText("100 %");

		sliderPanel.add(min);
		sliderPanel.add(channelSlider);
		sliderPanel.add(max);

		channelSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				updateValue();

			}

		});

		value = new JLabel();
		updateValue();

		valuePanel.add(value);

		titlePanel.add(new JLabel(channelType + " Channel " + channelID));

		infoPanel.add(titlePanel, BorderLayout.NORTH);
		infoPanel.add(sliderPanel, BorderLayout.CENTER);
		infoPanel.add(valuePanel, BorderLayout.SOUTH);

	}

	public void updateValue() {
		int channelValue = channelSlider.getValue();
		float sliderPercent = (float) channelValue / (float) channelSlider.getMaximum();
		int val = (int) (100 * sliderPercent);
		value.setText("Current Value: " + Integer.toString(val) + "%");
		if (!channelSlider.getValueIsAdjusting()) {
			if (channelValue != channel.getStatus()) {
				channel.setValue(channelValue);
				board.notifyServer();
				this.setText("V:" + Integer.toString(channelValue));
			}
		}

	}

}
