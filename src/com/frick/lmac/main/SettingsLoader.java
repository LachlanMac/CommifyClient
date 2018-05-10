package com.frick.lmac.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SettingsLoader {

	public static String SETTINGS[] = new String[3];

	public static void loadSettings() {

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("res/settings.txt")));

			String[] ip = br.readLine().split("=");
			SETTINGS[0] = ip[1];

			String[] serverPort = br.readLine().split("=");
			SETTINGS[1] = serverPort[1];

			String[] clientPort = br.readLine().split("=");
			SETTINGS[2] = clientPort[1];

		} catch (FileNotFoundException e) {
			SETTINGS[0] = "192.168.0.2";
			SETTINGS[1] = "8888";
			SETTINGS[2] = "8889";
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

}
