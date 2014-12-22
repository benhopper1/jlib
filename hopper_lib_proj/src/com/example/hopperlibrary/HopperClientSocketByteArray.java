package com.example.hopperlibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class HopperClientSocketByteArray {
	private String host;
	private int port;
	public Socket sock;
	private OutputStream outStream;	
	private DataOutputStream byteArrayOutputStream;
	private InputStream inStream;
	private DataInputStream byteArrayInputStream;
	
	
	
	public HopperClientSocketByteArray(Socket inSocket){
		this.sock=inSocket;
		try {
			HopperClientSocketByteArray.this.outStream = sock.getOutputStream();
			Log.v("MyActivity","outStream CREATED");
		} catch (IOException e) {
			Log.v("MyActivity","outStream ERROR");
			e.printStackTrace();
		}
		//added for byte array--
		HopperClientSocketByteArray.this.byteArrayOutputStream = new DataOutputStream(HopperClientSocketByteArray.this.outStream);
		Log.v("MyActivity","byteArrayOutputStream CREATED");
		//-----input
		try {
			HopperClientSocketByteArray.this.inStream = sock.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HopperClientSocketByteArray.this.byteArrayInputStream = new DataInputStream(HopperClientSocketByteArray.this.inStream);		
	}
	
	
	
	public HopperClientSocketByteArray(String inHost, int inPort){
		this.host=inHost;
		this.port = inPort;			
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){
				Log.v("MyActivity","HopperClientSocket CREATED");
				
				try {
					HopperClientSocketByteArray.this.sock = new Socket(HopperClientSocketByteArray.this.host, HopperClientSocketByteArray.this.port);
					Log.v("MyActivity","Sock CREATED");
				} catch (UnknownHostException e) {
					Log.v("MyActivity","Sock Error");
					e.printStackTrace();
				} catch (IOException e) {
					Log.v("MyActivity","Sock Error");
					e.printStackTrace();
				}
				try {
					HopperClientSocketByteArray.this.outStream = sock.getOutputStream();
					Log.v("MyActivity","outStream CREATED");
				} catch (IOException e) {
					Log.v("MyActivity","outStream ERROR");
					e.printStackTrace();
				}
				//added for byte array--
				HopperClientSocketByteArray.this.byteArrayOutputStream = new DataOutputStream(HopperClientSocketByteArray.this.outStream);
				Log.v("MyActivity","byteArrayOutputStream CREATED");
				//-----input
				try {
					HopperClientSocketByteArray.this.inStream = sock.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    HopperClientSocketByteArray.this.byteArrayInputStream = new DataInputStream(HopperClientSocketByteArray.this.inStream);
				return null;
			}
		};
		byte[] retString = has.run(null);		
				
	}
	public byte[] sendWaitOnReceive(byte[] inString){
		byte[] retString =null;	
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])
				try {
					if(inString[0].length>0){
						Log.v("MyActivity", "length of buff HopperClientSocketByteArray :"+inString[0].length);
						HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString[0].length);
						HopperClientSocketByteArray.this.byteArrayOutputStream.write(inString[0], 0, inString[0].length);
					}
				} catch (IOException e) {
					Log.v("MyActivity","ERROR HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray SEND complete");
				
				int len;
				try {
					len = HopperClientSocketByteArray.this.byteArrayInputStream.readInt(); //Reads a big-endian 32-bit integer value.
				    byte[] data = new byte[len];
				    if (len > 0) {
				    	HopperClientSocketByteArray.this.byteArrayInputStream.readFully(data);
				    }
				    Log.v("MyActivity","HopperClientSocketByteArray Receive complete");
				    return data;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray no data to Receive ");
			    return null;
			}
		};				
		retString = has.run(inString);		
		return retString;
	}
	public void send(byte[] inByteArray){
		byte[] retString =null;	
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])
				try {
					if(inString[0].length>0){
						Log.v("MyActivity", "length of buff HopperClientSocketByteArray :"+inString[0].length);
						HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString[0].length);
						HopperClientSocketByteArray.this.byteArrayOutputStream.write(inString[0], 0, inString[0].length);
					}
				} catch (IOException e) {
					Log.v("MyActivity","ERROR HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray SEND complete");				
			    return null;
			}
		};				
		retString = has.run(inByteArray);	
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
