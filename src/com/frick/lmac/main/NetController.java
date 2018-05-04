package com.frick.lmac.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NetController extends Thread {
	private static final int LOOP_DELAY_MS = 100;
	InetAddress serverAddress;
	int serverPort, startAttempts = 0;
	DatagramSocket socket;
	private BlockingQueue<CommPacket> sendQueue;
	private ArrayList<IOBoard> boardList;

	public NetController() {
		boardList = new ArrayList<IOBoard>();
		sendQueue = new ArrayBlockingQueue<CommPacket>(1024);
		startSocket();

	}

	@Override
	public void run() {

		byte[] requestData;
		byte[] responseData;
		DatagramPacket requestPacket;
		try {
			socket.setSoTimeout(5000);

			sendStartPacket();

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {

			for (IOBoard b : boardList) {

				responseData = new byte[256];
				// skip if this board has not been activated yet
				if (!b.isActivated()) {
					return;
				}

				requestData = getStatePacket(b);

				requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
				try {

					socket.send(requestPacket);
					b.setTXOn();
				} catch (IOException e) {
					e.printStackTrace();
				}
				DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);

				try {

					socket.receive(responsePacket);
					b.setRXOn();
					String updateString = new String(responsePacket.getData()).trim();
					b.updateState(updateString);
				} catch (SocketTimeoutException e) {
					System.out.print("Time out");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(LOOP_DELAY_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			while (!sendQueue.isEmpty()) {

				responseData = new byte[256];

				CommPacket p = sendQueue.poll();

				requestData = p.encode();

				requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);

				try {

					p.getBoard().setTXOn();
					socket.send(requestPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}

				DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);

				try {

					socket.receive(responsePacket);

					p.getBoard().decodePacket(responsePacket.getData());

					p.getBoard().setRXOn();
				} catch (SocketTimeoutException e) {

					System.out.println("TIMEOUT");

				} catch (IOException e) {

					e.printStackTrace();
				}
				p.getBoard().setRXOn();

			}

		}

	}

	public void startSocket() {

		try {
			serverPort = Integer.parseInt(Launcher.serverPort);
			serverAddress = InetAddress.getByName(Launcher.serverIP);
		} catch (UnknownHostException e1) {

			e1.printStackTrace();
		}

		try {
			socket = new DatagramSocket(Launcher.clientPort);

		} catch (SocketException e) {

			e.printStackTrace();
			socket.close();
		}

	}

	public void sendState(String state, IOBoard b) {
	
		sendQueue.add(new CommPacket(state, b));

	}

	public void deleteBoardRequest(IOBoard b) {

		StringBuilder sb = new StringBuilder();
		sb.append(CommPacket.getPacketCount() + "=d" + b.getBoardID() + b.getBoardType() + "=RemoveRequest");
		sendQueue.add(new CommPacket(new String(sb), b));

	}

	public void addBoardRequest(IOBoard b) {
		StringBuilder sb = new StringBuilder();
		sb.append(CommPacket.getPacketCount() + "=a" + b.getBoardID() + b.getBoardType() + "=AddRequest");
		sendQueue.add(new CommPacket(new String(sb), b));

	}

	public byte[] getStatePacket(IOBoard b) {

		StringBuilder sb = new StringBuilder();
		sb.append(CommPacket.getPacketCount() + "=g" + b.getBoardID() + b.getBoardType() + "=StatusRequest");

		return new String(sb).getBytes();
	}

	public void addBoard(IOBoard b) {
		boardList.add(b);
	}

	public void removeBoard(IOBoard b) {
		boardList.remove(b);
	}

	public void sendStartPacket() {
		if (startAttempts > 5) {
			return;
		}
		byte[] startPacket = "0=f00=startpacket".getBytes();
		byte[] returnData = new byte[256];
		DatagramPacket receive = new DatagramPacket(returnData, returnData.length);
		try {
			socket.send(new DatagramPacket(startPacket, startPacket.length, serverAddress, serverPort));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			socket.receive(receive);
		} catch (SocketTimeoutException e1) {
			System.out.println("Time out sending start packet");
			sendStartPacket();

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

}
