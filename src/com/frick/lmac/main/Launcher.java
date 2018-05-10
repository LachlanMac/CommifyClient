package com.frick.lmac.main;

public class Launcher {

	public static String serverPort = "8888";
	public static String serverIP = "192.168.0.178";
	public static int clientPort = 8889;
	public static String resPath = "res";

	public static void main(String[] args) {

		if (args.length == 2) {
			serverIP = args[0];
			serverPort = args[1];
		}

		SettingsLoader.loadSettings();

		ResourceLoader.loadResources();

	
		
		NetController net = new NetController();

		CommifyUI commify = new CommifyUI(net);

	}

}
