package com.frick.lmac.main;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceLoader {

	public static Image analogBoard, digitalBoard, inputModule, outputModule, analog_offLED, analog_onLED,
			digital_offLED, digital_onLED, ioSelectorIn, ioSelectorOut, active_offLED, active_onLED;
	public static Image chan1_2, chan3_4, chan5_6, chan7_8, chan9_10, chan11_12, chan13_14, chan15_16, chan17_18,
			chan19_20, chan21_22, chan23_24, oChan5_8, oChan1_4;

	public static Color analogRed;
	public static Color digitalGreen;

	public static void loadResources() {

		try {

			analogRed = new Color(126, 16, 16);
			digitalGreen = new Color(0, 75, 47);

			ioSelectorIn = ImageIO.read(new File(Launcher.resPath + "/ioSelector_in_HD.png"));
			ioSelectorOut = ImageIO.read(new File(Launcher.resPath + "/ioSelector_out_HD.png"));
			digital_offLED = ImageIO.read(new File(Launcher.resPath + "/LED_OFF_DIGITAL.png"));
			digital_onLED = ImageIO.read(new File(Launcher.resPath + "/LED_ON_DIGITAL.png"));
			analog_offLED = ImageIO.read(new File(Launcher.resPath + "/LED_OFF_ANALOG.png"));
			analog_onLED = ImageIO.read(new File(Launcher.resPath + "/LED_ON_ANALOG.png"));
			active_offLED = ImageIO.read(new File(Launcher.resPath + "/LED_OFF_ACTIVE.png"));
			active_onLED = ImageIO.read(new File(Launcher.resPath + "/LED_ON_ACTIVE.png"));
	
			outputModule = ImageIO.read(new File(Launcher.resPath + "/outputModule.png"));
			inputModule = ImageIO.read(new File(Launcher.resPath + "/inputModule.png"));
			digitalBoard = ImageIO.read(new File(Launcher.resPath + "/digitalboard_HD.png"));
			analogBoard = ImageIO.read(new File(Launcher.resPath + "/analogBoard.png"));

			chan1_2 = ImageIO.read(new File(Launcher.resPath + "/chan1-2.png"));
			chan3_4 = ImageIO.read(new File(Launcher.resPath + "/chan3-4.png"));
			chan5_6 = ImageIO.read(new File(Launcher.resPath + "/chan5-6.png"));
			chan7_8 = ImageIO.read(new File(Launcher.resPath + "/chan7-8.png"));
			chan9_10 = ImageIO.read(new File(Launcher.resPath + "/chan9-10.png"));
			chan11_12 = ImageIO.read(new File(Launcher.resPath + "/chan11-12.png"));
			chan13_14 = ImageIO.read(new File(Launcher.resPath + "/chan13-14.png"));
			chan15_16 = ImageIO.read(new File(Launcher.resPath + "/chan15-16.png"));
			chan17_18 = ImageIO.read(new File(Launcher.resPath + "/chan17-18.png"));
			chan19_20 = ImageIO.read(new File(Launcher.resPath + "/chan19-20.png"));
			chan21_22 = ImageIO.read(new File(Launcher.resPath + "/chan21-22.png"));
			chan23_24 = ImageIO.read(new File(Launcher.resPath + "/chan23-24.png"));

			oChan1_4 = ImageIO.read(new File(Launcher.resPath + "/oChan1_4.png"));
			oChan5_8 = ImageIO.read(new File(Launcher.resPath + "/oChan5_8.png"));

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
