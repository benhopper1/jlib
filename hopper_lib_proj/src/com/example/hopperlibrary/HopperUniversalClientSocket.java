package com.example.hopperlibrary;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;


public class HopperUniversalClientSocket {
	private String host;
	private int port;
	public Socket sock;
	
	private OutputStream outStream;	
	private DataOutputStream byteArrayOutputStream;
	private InputStream inStream;
	private DataInputStream byteArrayInputStream;
	
	private PrintWriter printWriterOut;
	private BufferedReader bufferReaderIn;
	
	private HopperCommuncationStack currentHCS;
	private int unsecureHeader_payLength;
	
	public HopperCommuncationStack receiveHeader(){
		byte[] xx;
		currentHCS =new HopperCommuncationStack();		
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])
				
				int len;
				try {//------get header 					
					
					HopperUniversalClientSocket.this.currentHCS.protoId = (int) HopperUniversalClientSocket.this.byteArrayInputStream.readChar();
					HopperUniversalClientSocket.this.currentHCS.security_0 = HopperUniversalClientSocket.this.byteArrayInputStream.readLong();
					HopperUniversalClientSocket.this.currentHCS.security_1 = HopperUniversalClientSocket.this.byteArrayInputStream.readLong();
					HopperUniversalClientSocket.this.currentHCS.security_2 = HopperUniversalClientSocket.this.byteArrayInputStream.readLong();
					HopperUniversalClientSocket.this.currentHCS.security_3 = HopperUniversalClientSocket.this.byteArrayInputStream.readLong();
					HopperUniversalClientSocket.this.currentHCS.payLength =  HopperUniversalClientSocket.this.byteArrayInputStream.readInt();
					Log.v("MyActivity","receiveHeader protoId:"+HopperUniversalClientSocket.this.currentHCS.protoId);
					Log.v("MyActivity","receiveHeader security_0:"+HopperUniversalClientSocket.this.currentHCS.security_0);	
					Log.v("MyActivity","receiveHeader security_1:"+HopperUniversalClientSocket.this.currentHCS.security_1);
					Log.v("MyActivity","receiveHeader security_2:"+HopperUniversalClientSocket.this.currentHCS.security_2);
					Log.v("MyActivity","receiveHeader security_3:"+HopperUniversalClientSocket.this.currentHCS.security_3);
					Log.v("MyActivity","receiveHeader payLength:"+HopperUniversalClientSocket.this.currentHCS.payLength);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("MyActivity","receiveHeader received ");
			    return null;
			}
		};
		xx = has.run(null);		
		
		return HopperUniversalClientSocket.this.currentHCS;
	}
	public byte[] receiveByteArray(){
		byte[] retString =null;	
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])
				
				int len;
				try {
					len = HopperUniversalClientSocket.this.currentHCS.payLength;
				    byte[] data = new byte[len];
				    if (len > 0) {
				    	HopperUniversalClientSocket.this.byteArrayInputStream.readFully(data);
				    }
				    Log.v("MyActivity","HopperClientSocketByteArray receiveByteArray Receive complete");
				    HopperUniversalClientSocket.this.currentHCS.data_byteArray=data;
				    return data;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray receiveByteArray no data to Receive ");
			    return null;
			}
		};				
		retString = has.run(null);		
		return retString;
	}
	
	public String convertByteArrayToString(byte[] inByteArray){
		String retString="";
		for (int i=0;i<inByteArray.length;i++){
			retString = retString+String.valueOf((char)inByteArray[i]);
			
		}
		Log.v("MyActivity",":"+retString);
		return retString;
	}
	
	
	public String receiveString(){
		String retString =null;	
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				byte[] data=null ;
				int len;
				try {
					len = HopperUniversalClientSocket.this.currentHCS.payLength;
				    data = new byte[len];
				    if (len > 0) {
				    	HopperUniversalClientSocket.this.byteArrayInputStream.readFully(data);
				    }
				    Log.v("MyActivity","HopperClientSocketByteArray Receive complete");
				    HopperUniversalClientSocket.this.currentHCS.data_byteArray=data;
				    return convertByteArrayToString(data);
				    //convertByteArrayToString();
				   // return data;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray in receiveString no data to Receive ");
				return "";
			}
		};				
		retString = has.run("");		
		return retString;
	}

	public void sendUnsecureHeader(final int inProtoId, final int inPayLength){
		currentHCS =new HopperCommuncationStack();
		byte[] retString =null;	
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				try {
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeShort((short)inProtoId);
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeInt( inPayLength);
				} catch (IOException e) {
					Log.v("MyActivity","ERROR HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperUniversalClientSocket sendHeader() HEADER SEND complete");
			    return null;
			}
		};				
		retString = has.run(null);		
	}
	public int receiveUnsecureHeader(){
		byte[] xx;
		int payLength;
		currentHCS =new HopperCommuncationStack();		
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])				
				int len;
				try {//------get header 					
					
					HopperUniversalClientSocket.this.currentHCS.protoId = (int) HopperUniversalClientSocket.this.byteArrayInputStream.readShort();					
					HopperUniversalClientSocket.this.currentHCS.payLength =  HopperUniversalClientSocket.this.byteArrayInputStream.readInt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("MyActivity","receiveHeader received ");
			    return null;
			}
		};
		xx = has.run(null);		
		
		return 0; //HopperUniversalClientSocket.this.currentHCS;
	}
	
	
	
	
	
	public void sendHeader(int inProtoId,long inSecurity_0,long inSecurity_1,long inSecurity_2,long inSecurity_3,int inPayLength){
		currentHCS =new HopperCommuncationStack();
		currentHCS.protoId=inProtoId;
		currentHCS.security_0=inSecurity_0;
		currentHCS.security_1=inSecurity_1;
		currentHCS.security_2=inSecurity_2;
		currentHCS.security_3=inSecurity_3;
		currentHCS.payLength=inPayLength;		
		
		byte[] retString =null;	
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])
				try {					
					
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeChar((char)HopperUniversalClientSocket.this.currentHCS.protoId);
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeLong( HopperUniversalClientSocket.this.currentHCS.security_0);
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeLong( HopperUniversalClientSocket.this.currentHCS.security_1);
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeLong( HopperUniversalClientSocket.this.currentHCS.security_2);
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeLong( HopperUniversalClientSocket.this.currentHCS.security_3);
					HopperUniversalClientSocket.this.byteArrayOutputStream.writeInt( HopperUniversalClientSocket.this.currentHCS.payLength);
					
				} catch (IOException e) {
					Log.v("MyActivity","ERROR HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperUniversalClientSocket sendHeader() HEADER SEND complete");
			    return null;
			}
		};				
		retString = has.run(null);		
	}
	
	public void sendByteArray(byte[] inString){
		byte[] retString =null;	
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){					
				//send to server--------- length, data   (int,byte[])
				try {
					if(inString[0].length>0){
						Log.v("MyActivity", "length of buff HopperClientSocketByteArray sendByteArray() :"+inString[0].length);	
						HopperUniversalClientSocket.this.byteArrayOutputStream.write(inString[0], 0, inString[0].length);
					}
				} catch (IOException e) {
					Log.v("MyActivity","ERROR HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray SEND complete");				
			    return null;
			}
		};				
		retString = has.run(inString);		
	}
	
	public void sendString(String inString){
		String retString =null;	
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				String newThreadRetString =null;				
				HopperUniversalClientSocket.this.printWriterOut.println(inString[0]);
				HopperUniversalClientSocket.this.printWriterOut.flush();
				BufferedReader input = null;
				try {
					input = new BufferedReader(new InputStreamReader(HopperUniversalClientSocket.this.sock.getInputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				return null;
			}
		};				
		retString = has.run(inString);		
	}
	
	public void sendString_2(String inString){
		String retString =null;	
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				try {
					if(inString[0].length()>0){
						Log.v("MyActivity", "length of buff HopperClientSocketByteArray sendString_2:"+inString[0].length());						
						HopperUniversalClientSocket.this.byteArrayOutputStream.writeBytes(inString[0]);    //.writeChars(inString[0], 0, inString[0].length);
					}
				} catch (IOException e) {
					Log.v("MyActivity","ERROR sendString_2 HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray sendString_2 SEND complete");
				return null;
			}
		};				
		retString = has.run(inString);		
	}
	
	
	
	
	public HopperUniversalClientSocket(Socket inSocket){
		this.sock=inSocket;
		try {
			HopperUniversalClientSocket.this.outStream = sock.getOutputStream();
			Log.v("MyActivity","outStream CREATED");
		} catch (IOException e) {
			Log.v("MyActivity","outStream ERROR");
			e.printStackTrace();
		}
		//added for byte array--
		HopperUniversalClientSocket.this.byteArrayOutputStream = new DataOutputStream(HopperUniversalClientSocket.this.outStream);
		Log.v("MyActivity","byteArrayOutputStream CREATED");
		//-----input
		try {
			HopperUniversalClientSocket.this.inStream = sock.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HopperUniversalClientSocket.this.byteArrayInputStream = new DataInputStream(HopperUniversalClientSocket.this.inStream);		
	}
	
	
	
	public HopperUniversalClientSocket(String inHost, int inPort){
		this.host=inHost;
		this.port = inPort;			
		HopperAsyncByteArray has = new HopperAsyncByteArray(HopperAsyncByteArray.EthreadType.WaitOnBackgroundThread){
			@Override
			public byte[] onNewThread(byte[] ... inString){
				Log.v("MyActivity","HopperClientSocket CREATED");
				
				try {
					HopperUniversalClientSocket.this.sock = new Socket(HopperUniversalClientSocket.this.host, HopperUniversalClientSocket.this.port);
					Log.v("MyActivity","Sock CREATED");
				} catch (UnknownHostException e) {
					Log.v("MyActivity","Sock Error");
					e.printStackTrace();
				} catch (IOException e) {
					Log.v("MyActivity","Sock Error");
					e.printStackTrace();
				}
				try {
					HopperUniversalClientSocket.this.outStream = sock.getOutputStream();
					Log.v("MyActivity","outStream CREATED");
				} catch (IOException e) {
					Log.v("MyActivity","outStream ERROR");
					e.printStackTrace();
				}
				//added for byte array--
				HopperUniversalClientSocket.this.byteArrayOutputStream = new DataOutputStream(HopperUniversalClientSocket.this.outStream);
				Log.v("MyActivity","byteArrayOutputStream CREATED");
				//-----input
				try {
					HopperUniversalClientSocket.this.inStream = sock.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HopperUniversalClientSocket.this.byteArrayInputStream = new DataInputStream(HopperUniversalClientSocket.this.inStream);
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
						HopperUniversalClientSocket.this.byteArrayOutputStream.writeInt(inString[0].length);
						HopperUniversalClientSocket.this.byteArrayOutputStream.write(inString[0], 0, inString[0].length);
					}
				} catch (IOException e) {
					Log.v("MyActivity","ERROR HopperClientSocketByteArray.this.byteArrayOutputStream.writeInt(inString.length)");
					e.printStackTrace();
				}
				Log.v("MyActivity","HopperClientSocketByteArray SEND complete");
				
				int len;
				try {
					len = HopperUniversalClientSocket.this.byteArrayInputStream.readInt(); //Reads a big-endian 32-bit integer value.
				    byte[] data = new byte[len];
				    if (len > 0) {
				    	HopperUniversalClientSocket.this.byteArrayInputStream.readFully(data);
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
						HopperUniversalClientSocket.this.byteArrayOutputStream.writeInt(inString[0].length);
						HopperUniversalClientSocket.this.byteArrayOutputStream.write(inString[0], 0, inString[0].length);
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
	
	
	


