// speak to be spoken to.

package com.example.hopperlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class HopperClientSocket {
	private String host;
	private int port;
	public Socket sock;
	private OutputStream outStream;
	private PrintWriter printWriterOut;
	private BufferedReader bufferReaderIn;
	public HopperClientSocket(Socket inSock){
		this.sock=inSock;
		try {
			HopperClientSocket.this.outStream = sock.getOutputStream();
			Log.v("MyActivity","outStream CREATED");
		} catch (IOException e) {
			Log.v("MyActivity","outStream ERROR");
			e.printStackTrace();
		}
		HopperClientSocket.this.printWriterOut = new PrintWriter(HopperClientSocket.this.outStream);
		Log.v("MyActivity","printWriterOut CREATED");		
	}
	
	public HopperClientSocket(String inHost, int inPort){
		this.host=inHost;
		this.port = inPort;			
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				Log.v("MyActivity","HopperClientSocket CREATED");
				
				try {
					HopperClientSocket.this.sock = new Socket(HopperClientSocket.this.host, HopperClientSocket.this.port);
					Log.v("MyActivity","Sock CREATED");
				} catch (UnknownHostException e) {
					Log.v("MyActivity","Sock Error");
					e.printStackTrace();
				} catch (IOException e) {
					Log.v("MyActivity","Sock Error");
					e.printStackTrace();
				}
				try {
					HopperClientSocket.this.outStream = sock.getOutputStream();
					Log.v("MyActivity","outStream CREATED");
				} catch (IOException e) {
					Log.v("MyActivity","outStream ERROR");
					e.printStackTrace();
				}
				HopperClientSocket.this.printWriterOut = new PrintWriter(HopperClientSocket.this.outStream);
				Log.v("MyActivity","printWriterOut CREATED");
				return "";
			}
		};
		String retString = has.run("");		
				
	}
	public String sendWaitOnReceive(String inString){
		String retString =null;	
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				String newThreadRetString =null;				
				HopperClientSocket.this.printWriterOut.println(inString[0]);
				HopperClientSocket.this.printWriterOut.flush();
				BufferedReader input = null;
				try {
					input = new BufferedReader(new InputStreamReader(HopperClientSocket.this.sock.getInputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				try {
					newThreadRetString = input.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  //TODO handle multi lines
				return newThreadRetString;
			}
		};				
		retString = has.run(inString);		
		return retString;
	}
	public void close(){
		try {
			this.sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}





