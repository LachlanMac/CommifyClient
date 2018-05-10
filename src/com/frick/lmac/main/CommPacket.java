package com.frick.lmac.main;

public class CommPacket {
	public static int packet_count = 0;
	public static final int ANALOG = 2;
	public static final int DIGITAL = 1;

	private IOBoard b;
	private String data;
	private byte[] packetData;

	public CommPacket(String data, IOBoard b) {
		this.data = data;
		this.b = b;

	}

	public byte[] encode() {

		packetData = data.getBytes();
		return packetData;
	}
	


	public static int getPacketCount() {
		packet_count++;
		return packet_count;
	}

	public static void sendRX() {
		
		
		
	}
	
	
	public IOBoard getBoard() {
		return b;
	}
	
	

}
