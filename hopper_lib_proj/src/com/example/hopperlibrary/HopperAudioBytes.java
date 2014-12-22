package com.example.hopperlibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

// need constructor to set sample etc...

public class HopperAudioBytes {
	public void playBuffer(byte[] inBuffer){
		AudioTrack aPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT, 8000, AudioTrack.MODE_STREAM); 
		aPlayer.play();
		aPlayer.write(inBuffer, 0, inBuffer.length); 
		
		aPlayer.stop();
		aPlayer.release();
		
	}
	@SuppressLint("NewApi")
	public byte[] trimBufferEnd(byte[] inBuffer,float inMinEndingVolume){
		onAnalizeStart();
		final int SAMPLE_BYTE_SIZE=7808;
		float tempFloatBuffer[] = new float[3];
		int numberOfReadBytes   = 0; 
		int tempIndex           = 0;
		//float totalAbsValue = 0.0f;
        //short sample        = 0; 
        byte audioBuffer[]      = new  byte[SAMPLE_BYTE_SIZE];
        int abindex=0; //startindex
        int bufferSizeInBytes = SAMPLE_BYTE_SIZE;
        long lastKnownGoodVolumeMarker=-1;   //actual byte index 
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream( inBuffer);
        
        //numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );
        byte[] retByteArray = null;
        
        while(true){
        	float totalAbsValue = 0.0f;
	        short sample        = 0; 	
	        numberOfReadBytes = inputStream.read(audioBuffer, 0, bufferSizeInBytes);
	        
	        Log.v("MyActivity", "numberOfReadBytes(dynamic) / bufferSizeInBytes(set) "+numberOfReadBytes+" / "+bufferSizeInBytes);
	        if(numberOfReadBytes<=0){break;}
	        // Analyze Sound.
	        Log.v("MyActivity", "("+inBuffer.length+")Analyze Size "+numberOfReadBytes+"/"+bufferSizeInBytes);
	        for( int i=0; i<bufferSizeInBytes; i+=2 ){
	            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
	            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
	        }
	        // Analyze temp buffer.
	        tempFloatBuffer[tempIndex%3] = totalAbsValue;
	        float temp                   = 0.0f;
	        for( int i=0; i<3; ++i ){
	            temp += tempFloatBuffer[i];
	        }
	        Log.v("MyActivity", "::::::::::::::::abindex / analyze# "+abindex+" / "+String.valueOf(temp));
	        abindex++;
	        retByteArray=mergeArraysOfByte(retByteArray,audioBuffer);
	        if(temp>inMinEndingVolume){
	        	lastKnownGoodVolumeMarker=retByteArray.length-1;        
	        }	        
	        
        }
        onAnalizeEnd();
        if((lastKnownGoodVolumeMarker+(SAMPLE_BYTE_SIZE*4))<(inBuffer.length-1)){
        	return Arrays.copyOfRange(retByteArray, 0, ((int) lastKnownGoodVolumeMarker+(SAMPLE_BYTE_SIZE*4)));
        }else{
        	return retByteArray;
        	//return Arrays.copyOfRange(retByteArray, 0, (int) lastKnownGoodVolumeMarker);
        }	
	}
	
	
	@SuppressLint("NewApi")
	public byte[] getBuffer_waitForSound_recordUntilSilence(float inStartVolume,long inWaitMaxDuration,float inStopVolume,long inStopVolumeDuration){
		Log.v("MyActivity","starting pre analize--------------------------------");
		
        final int RECORDER_SAMPLERATE = 44100;
        final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
        final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
        float tempLast = 0;        
        // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
        int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING  ); 
        // Initialize Audio Recorder.
        AudioRecord audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSizeInBytes);
        // Start Recording.
        audioRecorder.startRecording();
	    int numberOfReadBytes   = 0; 
	    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
	    boolean recording       = false;
	    float tempFloatBuffer[] = new float[3];
	    int tempIndex           = 0;
	    int totalReadBytes      = 0;
	    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];
	    long lastKnownMillis=System.currentTimeMillis();
	    // While data come from microphone. 
	    while( true ){
	        float totalAbsValue = 0.0f;
	        short sample        = 0; 	
	        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );	
	        // Analyze Sound.
	        Log.v("MyActivity", "Analyze Size "+numberOfReadBytes+"/"+bufferSizeInBytes);
	        for( int i=0; i<bufferSizeInBytes; i+=2 ){
	            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
	            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
	        }
	        // Analyze temp buffer.
	        tempFloatBuffer[tempIndex%3] = totalAbsValue;
	        float temp                   = 0.0f;
	        for( int i=0; i<3; ++i ){
	            temp += tempFloatBuffer[i];
	        }
	        
        	if(temp!=tempLast){
        		String state="NoRECORD";
        		if(recording == true){state="RECORD";}else{state="NoRECORD";}
	        	Log.v("MyActivity", state+String.valueOf(temp));	        	
        	}
        	tempLast=temp;
        	//----waited too long return NULL---------
        	if(((System.currentTimeMillis()-lastKnownMillis) >= inWaitMaxDuration) && recording== false ){
	        	Log.v("MyActivity", "getBuffer_waitForSound: SILENCE PERIOD TOO LONG : was("+String.valueOf(System.currentTimeMillis()-lastKnownMillis)+") specified ("+String.valueOf(inWaitMaxDuration)+")");
	        	Log.v("MyActivity", "getBuffer_waitForSound: SILENCE PERIOD TOO LONG---size of buffer "+audioBuffer.length);
	        	audioRecorder.release();
	        	onRecordEnd();
	        	return null;	
        	}
        	//----force back to top of loop, bad use of construct here
	        if( (temp >=0 && temp <= inStartVolume) && recording == false ){ //ignored prev sound
	        	//Log.v("MyActivity", "1");
	            tempIndex++;
	            continue;
	        }
	        //---added for composite, update time for usable volume
	        if( temp > inStopVolume && recording == true){   // good data ----------------------------------
	        	Log.v("MyActivity", "getBuffer_recordUntilSilence:  temp > inStopVolume timer reset");
	        	lastKnownMillis = System.currentTimeMillis();	            
	        }
	        if(((System.currentTimeMillis()-lastKnownMillis) >= inStopVolumeDuration) && recording== true ){ // check ,lastKnownMillis for expired period while RECORDING
	        	Log.v("MyActivity", "getBuffer_recordUntilSilence: SILENCE PERIOD TOO LONG : was("+String.valueOf(System.currentTimeMillis()-lastKnownMillis)+") specified ("+String.valueOf(inStopVolumeDuration)+")");
	        	Log.v("MyActivity", "getBuffer_recordUntilSilence: SILENCE PERIOD TOO LONG---size of buffer "+totalByteBuffer.length);
	        	onRecordEnd();
	        	audioRecorder.release();
	        	return Arrays.copyOf(totalByteBuffer, totalReadBytes);
	        	//return soundBytes;
	        }
	
	        if( temp > inStartVolume && recording == false ){
	           // Log.v("MyActivity", "2");
	        	lastKnownMillis=System.currentTimeMillis();
	            recording = true;
	            onRecordStart();
	        }
	
	        // -> Recording sound here.
	        Log.v("MyActivity", "Recording Sound." );
	        for( int i=0; i<numberOfReadBytes; i++ )
	            totalByteBuffer[totalReadBytes + i] = audioBuffer[i];
	        totalReadBytes += numberOfReadBytes;
	        //*/
	
	        tempIndex++;
	
	    }
	 //   Log.v("MyActivity", "DONE------" );	
		
		
		
		
	}
	
	
	@SuppressLint("NewApi")
	public byte[] getBuffer_waitForSound(float inStartVolume,long inWaitMaxDuration){
		Log.v("MyActivity","starting pre analize--------------------------------");
		
        final int RECORDER_SAMPLERATE = 44100;
        final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
        final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
        float tempLast = 0;        
        // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
        int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING  ); 
        // Initialize Audio Recorder.
        AudioRecord audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSizeInBytes);
        // Start Recording.
        audioRecorder.startRecording();
	    int numberOfReadBytes   = 0; 
	    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
	    boolean recording       = false;
	    float tempFloatBuffer[] = new float[3];
	    int tempIndex           = 0;
	    int totalReadBytes      = 0;
	    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];
	    // While data come from microphone. 
	    while( true ){
	        float totalAbsValue = 0.0f;
	        short sample        = 0; 	
	        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );	
	        // Analyze Sound.
	        for( int i=0; i<bufferSizeInBytes; i+=2 ){
	            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
	            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
	        }
	        // Analyze temp buffer.
	        tempFloatBuffer[tempIndex%3] = totalAbsValue;
	        float temp                   = 0.0f;
	        for( int i=0; i<3; ++i ){
	            temp += tempFloatBuffer[i];
	        }
	        
        	if(temp!=tempLast){
	        	Log.v("MyActivity", String.valueOf(temp));	        	
        	}
        	tempLast=temp;
	
	        if( (temp >=0 && temp <= inStartVolume) && recording == false ){
	        	//Log.v("MyActivity", "1");
	            tempIndex++;
	            continue;
	        }
	
	        if( temp > inStartVolume && recording == false ){
	        	onRecordStart();
	           // Log.v("MyActivity", "2");
	            recording = true;
	        }
	
	        if( (temp >= 0 && temp <= inStartVolume) && recording == true ){
	        	Log.v("MyActivity", "return bytes ");
	        	Log.v("MyActivity", " totalReadBytes / totalByteBuffer.len"+totalReadBytes+"/"+totalByteBuffer.length);
	        	//---
	        	audioRecorder.release();
	        	onRecordEnd();
	        	return Arrays.copyOf(totalByteBuffer, totalReadBytes);
	            
	        }
	
	        // -> Recording sound here.
	        Log.v("MyActivity", "Recording Sound." );
	        for( int i=0; i<numberOfReadBytes; i++ )
	            totalByteBuffer[totalReadBytes + i] = audioBuffer[i];
	        totalReadBytes += numberOfReadBytes;
	        //*/
	
	        tempIndex++;
	
	    }
	 //   Log.v("MyActivity", "DONE------" );
				
	}
	
	
	
	public byte[] getBuffer_recordUntilSilence(float inStopVolume,long inStopVolumeDuration){
		Log.v("MyActivity", "getBuffer_recordUntilSilence: buildPostRecordFile");	
        
    	final int RECORDER_BPP = 16;
        final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
        final String AUDIO_RECORDER_FOLDER = "ARF/prerecordings";
        final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
        final int RECORDER_SAMPLERATE = 44100;
        final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
        final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
        
        //ArrayList<byte> arrayList = new ArrayList<byte>();
        byte soundBytes[] = null;  //used for payload of actual file
        
        long lastKnownMillis=System.currentTimeMillis();
        float tempLast = 0;
       
        // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
        int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING  ); 
        // Initialize Audio Recorder.
        Log.v("MyActivity", "getBuffer_recordUntilSilence: audioRecorder create...(sample size"+bufferSizeInBytes+")");	
        AudioRecord audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSizeInBytes);
        // Start Recording.
        Log.v("MyActivity", "getBuffer_recordUntilSilence: audioRecorder startRecording STARTING...");	
        audioRecorder.startRecording();
        Log.v("MyActivity", "getBuffer_recordUntilSilence: audioRecorder startRecording DONE...");
	    int numberOfReadBytes   = 0; 
	    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
	    boolean recording       = false;
	    float tempFloatBuffer[] = new float[3];
	    int tempIndex           = 0;
	    int totalReadBytes      = 0;
	    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];
	    // While data come from microphone. 
	    while( true ){
	        float totalAbsValue = 0.0f;
	        short sample        = 0; 
	        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );
	        // Analyze Sound.
	        for( int i=0; i<bufferSizeInBytes; i+=2 ){
	            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
	            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
	        }
	        // Analyze temp buffer.
	        tempFloatBuffer[tempIndex%3] = totalAbsValue;
	        float temp                   = 0.0f;
	        for( int i=0; i<3; ++i ){
	            temp += tempFloatBuffer[i];
	        }
	        
        	if(temp!=tempLast){
	        	Log.v("MyActivity","getBuffer_recordUntilSilence: "+ String.valueOf(temp));	        	
	        	
        	}
        	tempLast=temp;
	
	        if( temp >=0 && temp <= inStopVolume){ // less than needed for recording purpose-----------	        	
	        }
	
	        if( temp > inStopVolume ){   // good data ----------------------------------
	            Log.v("MyActivity", "getBuffer_recordUntilSilence:  temp > inStopVolume timer reset");
	            lastKnownMillis = System.currentTimeMillis();	            
	        }
	        
	        if((System.currentTimeMillis()-lastKnownMillis) >= inStopVolumeDuration){ // check ,lastKnownMillis for expired period
	        	Log.v("MyActivity", "getBuffer_recordUntilSilence: SILENCE PERIOD TOO LONG : was("+String.valueOf(System.currentTimeMillis()-lastKnownMillis)+") specified ("+String.valueOf(inStopVolumeDuration)+")");
	        	Log.v("MyActivity", "getBuffer_recordUntilSilence: SILENCE PERIOD TOO LONG---size of buffer "+soundBytes.length);
	        	 audioRecorder.release();
	        	return soundBytes;
	        }
	        
	        
        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        	try {	        		
				if(soundBytes!=null){
					outputStream.write( soundBytes );
				}
			} catch (IOException e1) {
				Log.v("MyActivity", "ERROR getBuffer_recordUntilSilence: outputStream.write for soundBytes ");
				e1.printStackTrace();
			} //existing payload bytes
        	try {	        		
				outputStream.write( audioBuffer );
			} catch (IOException e) {
				Log.v("MyActivity", "ERROR getBuffer_recordUntilSilence: outputStream.write for audioBuffer ");
				e.printStackTrace();
			} // just sampled	        	
        	soundBytes = outputStream.toByteArray( );
	    }
		
	}
	
	
	
	
	public void buildPostRecordFile(String inNewFullFileName, float inStopVolume,long inStopVolumeDuration){
		Log.v("MyActivity", "buildPostRecordFile");	    
	    
        
        //-----------------
    	final int RECORDER_BPP = 16;
        final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
        final String AUDIO_RECORDER_FOLDER = "ARF/prerecordings";
        final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
        final int RECORDER_SAMPLERATE = 44100;
        final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
        final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
        
        //ArrayList<byte> arrayList = new ArrayList<byte>();
        byte soundBytes[] = null;  //used for payload of actual file
        
        long lastKnownMillis=System.currentTimeMillis();
        float tempLast = 0;
        
        // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
        int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING  ); 
        // Initialize Audio Recorder.
        Log.v("MyActivity", "audioRecorder create...(sample size"+bufferSizeInBytes+")");	
        AudioRecord audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSizeInBytes);
        // Start Recording.
        Log.v("MyActivity", "audioRecorder startRecording STARTING...");	
        audioRecorder.startRecording();
        Log.v("MyActivity", "audioRecorder startRecording DONE...");
	    int numberOfReadBytes   = 0; 
	    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
	    boolean recording       = false;
	    float tempFloatBuffer[] = new float[3];
	    int tempIndex           = 0;
	    int totalReadBytes      = 0;
	    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];
	    // While data come from microphone. 
	    while( true ){
	        float totalAbsValue = 0.0f;
	        short sample        = 0; 
	        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );
	        // Analyze Sound.
	        for( int i=0; i<bufferSizeInBytes; i+=2 ){
	            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
	            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
	        }
	        // Analyze temp buffer.
	        tempFloatBuffer[tempIndex%3] = totalAbsValue;
	        float temp                   = 0.0f;
	        for( int i=0; i<3; ++i ){
	            temp += tempFloatBuffer[i];
	        }
	        
        	if(temp!=tempLast){
	        	Log.v("MyActivity", String.valueOf(temp));	        	
        	}
        	tempLast=temp;
	
	        if( temp >=0 && temp <= inStopVolume){ // less than needed for recording purpose-----------	        	
	        }
	
	        if( temp > inStopVolume ){   // good data ----------------------------------
	            Log.v("MyActivity", "temp > inStopVolume timer reset");
	            lastKnownMillis = System.currentTimeMillis();	            
	        }
	        
	        if((System.currentTimeMillis()-lastKnownMillis) >= inStopVolumeDuration){ // check ,lastKnownMillis for expired period
	        	Log.v("MyActivity", "SILENCE PERIOD TOO LONG : was("+String.valueOf(System.currentTimeMillis()-lastKnownMillis)+") specified ("+String.valueOf(inStopVolumeDuration)+")");
	        	Log.v("MyActivity", "SILENCE PERIOD TOO LONG---size of buffer "+soundBytes.length);
	        	saveBufferAsWavFile(inNewFullFileName,soundBytes, RECORDER_SAMPLERATE,RECORDER_BPP);
	        	return;
	        }
	        
	        if(true){  // add to final set arry ------WRITE--------
	        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
	        	try {	        		
					if(soundBytes!=null){
						outputStream.write( soundBytes );
					}
				} catch (IOException e1) {
					Log.v("MyActivity", "ERROR outputStream.write for soundBytes ");
					e1.printStackTrace();
				} //existing payload bytes
	        	try {	        		
					outputStream.write( audioBuffer );
				} catch (IOException e) {
					Log.v("MyActivity", "ERROR outputStream.write for audioBuffer ");
					e.printStackTrace();
				} // just sampled	        	
	        	soundBytes = outputStream.toByteArray( );
	        }
	    }
	}
	
	public void buildPreRecordFile(String inNewFullFileName, float inStartVolume){
		Log.v("MyActivity","starting pre analize--------------------------------");
		final int RECORDER_BPP = 16;
        final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
        final String AUDIO_RECORDER_FOLDER = "ARF/prerecordings";
        final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
        final int RECORDER_SAMPLERATE = 44100;
        final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
        final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

        float tempLast = 0;
        
        // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
        int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING  ); 
        // Initialize Audio Recorder.
        AudioRecord audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSizeInBytes);
        //AudioRecord audioRecorder2 = new AudioRecord( MediaRecorder.AudioSource.MIC,RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSizeInBytes);
        //audioRecorder2.startRecording();
        
        
        
        // Start Recording.
        audioRecorder.startRecording();

	    int numberOfReadBytes   = 0; 
	    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
	    boolean recording       = false;
	    float tempFloatBuffer[] = new float[3];
	    int tempIndex           = 0;
	    int totalReadBytes      = 0;
	    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];
	    // While data come from microphone. 
	    while( true ){
	        float totalAbsValue = 0.0f;
	        short sample        = 0; 
	
	        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );
	
	        // Analyze Sound.
	        for( int i=0; i<bufferSizeInBytes; i+=2 ){
	            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
	            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
	        }
	
	        // Analyze temp buffer.
	        tempFloatBuffer[tempIndex%3] = totalAbsValue;
	        float temp                   = 0.0f;
	        for( int i=0; i<3; ++i ){
	            temp += tempFloatBuffer[i];
	        }
	        
        	if(temp!=tempLast){
	        	Log.v("MyActivity", String.valueOf(temp));	        	
        	}
        	tempLast=temp;
	
	        if( (temp >=0 && temp <= inStartVolume) && recording == false ){
	        	//Log.v("MyActivity", "1");
	            tempIndex++;
	            continue;
	        }
	
	        if( temp > inStartVolume && recording == false ){
	           // Log.v("MyActivity", "2");
	            recording = true;
	        }
	
	        if( (temp >= 0 && temp <= inStartVolume) && recording == true ){
	        	Log.v("MyActivity", "Save audio to file.");
	
	            // Save audio to file.
	            String filepath = Environment.getExternalStorageDirectory().getPath();
	            File file = new File(filepath,"AudioRecorder");
	            if( !file.exists() )
	                file.mkdirs();
	
	            String fn = inNewFullFileName;//file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".wav";
	            Log.v("MyActivity", fn);
	            
	            long totalAudioLen  = 0;
	            long totalDataLen   = totalAudioLen + 36;
	            long longSampleRate = RECORDER_SAMPLERATE*2;
	            int channels        = 1;
	            long byteRate       = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;
	            totalAudioLen       = totalReadBytes;
	            totalDataLen        = totalAudioLen + 36;
	            byte finalBuffer[]  = new byte[totalReadBytes + 44];
	
	            finalBuffer[0] = 'R';  // RIFF/WAVE header
	            finalBuffer[1] = 'I';
	            finalBuffer[2] = 'F';
	            finalBuffer[3] = 'F';
	            finalBuffer[4] = (byte) (totalDataLen & 0xff);
	            finalBuffer[5] = (byte) ((totalDataLen >> 8) & 0xff);
	            finalBuffer[6] = (byte) ((totalDataLen >> 16) & 0xff);
	            finalBuffer[7] = (byte) ((totalDataLen >> 24) & 0xff);
	            finalBuffer[8] = 'W';
	            finalBuffer[9] = 'A';
	            finalBuffer[10] = 'V';
	            finalBuffer[11] = 'E';
	            finalBuffer[12] = 'f';  // 'fmt ' chunk
	            finalBuffer[13] = 'm';
	            finalBuffer[14] = 't';
	            finalBuffer[15] = ' ';
	            finalBuffer[16] = 16;  // 4 bytes: size of 'fmt ' chunk
	            finalBuffer[17] = 0;
	            finalBuffer[18] = 0;
	            finalBuffer[19] = 0;
	            finalBuffer[20] = 1;  // format = 1
	            finalBuffer[21] = 0;
	            finalBuffer[22] = (byte) channels;
	            finalBuffer[23] = 0;
	            finalBuffer[24] = (byte) (longSampleRate & 0xff);
	            finalBuffer[25] = (byte) ((longSampleRate >> 8) & 0xff);
	            finalBuffer[26] = (byte) ((longSampleRate >> 16) & 0xff);
	            finalBuffer[27] = (byte) ((longSampleRate >> 24) & 0xff);
	            finalBuffer[28] = (byte) (byteRate & 0xff);
	            finalBuffer[29] = (byte) ((byteRate >> 8) & 0xff);
	            finalBuffer[30] = (byte) ((byteRate >> 16) & 0xff);
	            finalBuffer[31] = (byte) ((byteRate >> 24) & 0xff);
	            finalBuffer[32] = (byte) (2 * 16 / 8);  // block align
	            finalBuffer[33] = 0;
	            finalBuffer[34] = RECORDER_BPP;  // bits per sample
	            finalBuffer[35] = 0;
	            finalBuffer[36] = 'd';
	            finalBuffer[37] = 'a';
	            finalBuffer[38] = 't';
	            finalBuffer[39] = 'a';
	            finalBuffer[40] = (byte) (totalAudioLen & 0xff);
	            finalBuffer[41] = (byte) ((totalAudioLen >> 8) & 0xff);
	            finalBuffer[42] = (byte) ((totalAudioLen >> 16) & 0xff);
	            finalBuffer[43] = (byte) ((totalAudioLen >> 24) & 0xff);
	
	            for( int i=0; i<totalReadBytes; ++i )
	                finalBuffer[44+i] = totalByteBuffer[i];
	
	            FileOutputStream out;
	            try {
	                out = new FileOutputStream(fn);
	                 try {
	                        out.write(finalBuffer);
	                        out.close();
	                    } catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	
	            } catch (FileNotFoundException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }
	
	            //*/
	            tempIndex++;
	            break;
	        }
	
	        // -> Recording sound here.
	        Log.v("MyActivity", "Recording Sound." );
	        for( int i=0; i<numberOfReadBytes; i++ )
	            totalByteBuffer[totalReadBytes + i] = audioBuffer[i];
	        totalReadBytes += numberOfReadBytes;
	        //*/
	
	        tempIndex++;
	
	    }
	    Log.v("MyActivity", "DONE------" );
			
	}
	

	

	
	public byte[] mergeArraysOfByte(byte[] inArrayA,byte[] inArrayB){		
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    	try {	        		
			if(inArrayA!=null){
				outputStream.write( inArrayA );
			}
		} catch (IOException e1) {
			Log.v("MyActivity", "ERROR mergeArraysOfByte outputStream.write for inArrayA ");
			e1.printStackTrace();
		} //existing payload bytes
    	try {	        		
    		if(inArrayB!=null){
				outputStream.write( inArrayB );
			}
		} catch (IOException e) {
			Log.v("MyActivity", "ERROR mergeArraysOfByte outputStream.write for inArrayB ");
			e.printStackTrace();
		} // just sampled	        	
    	 return outputStream.toByteArray( );  
	}
	
	
	public byte[] getBufferWithHeader(byte[] inBuffer, long inSampleRate,int inBpp){
		inSampleRate=inSampleRate*2;
		Log.v("MyActivity", "Save audio to file.");
    	int totalReadBytes=inBuffer.length;        
        long totalAudioLen  = 0;
        long totalDataLen   = totalAudioLen + 36;
        long longSampleRate = inSampleRate; //RECORDER_SAMPLERATE;
        int channels        = 1;
        long byteRate       = inBpp * inSampleRate * channels/8;             //RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;
        totalAudioLen       = totalReadBytes;
        totalDataLen        = totalAudioLen + 36;
        byte finalBuffer[]  = new byte[totalReadBytes + 44];

        finalBuffer[0] = 'R';  // RIFF/WAVE header
        finalBuffer[1] = 'I';
        finalBuffer[2] = 'F';
        finalBuffer[3] = 'F';
        finalBuffer[4] = (byte) (totalDataLen & 0xff);
        finalBuffer[5] = (byte) ((totalDataLen >> 8) & 0xff);
        finalBuffer[6] = (byte) ((totalDataLen >> 16) & 0xff);
        finalBuffer[7] = (byte) ((totalDataLen >> 24) & 0xff);
        finalBuffer[8] = 'W';
        finalBuffer[9] = 'A';
        finalBuffer[10] = 'V';
        finalBuffer[11] = 'E';
        finalBuffer[12] = 'f';  // 'fmt ' chunk
        finalBuffer[13] = 'm';
        finalBuffer[14] = 't';
        finalBuffer[15] = ' ';
        finalBuffer[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        finalBuffer[17] = 0;
        finalBuffer[18] = 0;
        finalBuffer[19] = 0;
        finalBuffer[20] = 1;  // format = 1
        finalBuffer[21] = 0;
        finalBuffer[22] = (byte) channels;
        finalBuffer[23] = 0;
        finalBuffer[24] = (byte) (longSampleRate & 0xff);
        finalBuffer[25] = (byte) ((longSampleRate >> 8) & 0xff);
        finalBuffer[26] = (byte) ((longSampleRate >> 16) & 0xff);
        finalBuffer[27] = (byte) ((longSampleRate >> 24) & 0xff);
        finalBuffer[28] = (byte) (byteRate & 0xff);
        finalBuffer[29] = (byte) ((byteRate >> 8) & 0xff);
        finalBuffer[30] = (byte) ((byteRate >> 16) & 0xff);
        finalBuffer[31] = (byte) ((byteRate >> 24) & 0xff);
        finalBuffer[32] = (byte) (2 * 16 / 8);  // block align
        finalBuffer[33] = 0;
        finalBuffer[34] = (byte) inBpp; //RECORDER_BPP;  // bits per sample
        finalBuffer[35] = 0;
        finalBuffer[36] = 'd';
        finalBuffer[37] = 'a';
        finalBuffer[38] = 't';
        finalBuffer[39] = 'a';
        finalBuffer[40] = (byte) (totalAudioLen & 0xff);
        finalBuffer[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        finalBuffer[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        finalBuffer[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        for( int i=0; i<totalReadBytes; ++i )
            finalBuffer[44+i] = inBuffer[i];
        
        
        return finalBuffer;
	
	}
	//-----
	
	
	
	public void saveBufferAsWavFile(String inNewFullFileName,byte[] inBuffer, long inSampleRate,int inBpp){
		inSampleRate=inSampleRate*2;
		Log.v("MyActivity", "Save audio to file.");
    	int totalReadBytes=inBuffer.length;  
        // Save audio to file.
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,"AudioRecorder");
        if( !file.exists() )
            file.mkdirs();

        String fn = inNewFullFileName;//file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".wav";
        Log.v("MyActivity", fn);
        
        long totalAudioLen  = 0;
        long totalDataLen   = totalAudioLen + 36;
        long longSampleRate = inSampleRate; //RECORDER_SAMPLERATE;
        int channels        = 1;
        long byteRate       = inBpp * inSampleRate * channels/8;             //RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;
        totalAudioLen       = totalReadBytes;
        totalDataLen        = totalAudioLen + 36;
        byte finalBuffer[]  = new byte[totalReadBytes + 44];

        finalBuffer[0] = 'R';  // RIFF/WAVE header
        finalBuffer[1] = 'I';
        finalBuffer[2] = 'F';
        finalBuffer[3] = 'F';
        finalBuffer[4] = (byte) (totalDataLen & 0xff);
        finalBuffer[5] = (byte) ((totalDataLen >> 8) & 0xff);
        finalBuffer[6] = (byte) ((totalDataLen >> 16) & 0xff);
        finalBuffer[7] = (byte) ((totalDataLen >> 24) & 0xff);
        finalBuffer[8] = 'W';
        finalBuffer[9] = 'A';
        finalBuffer[10] = 'V';
        finalBuffer[11] = 'E';
        finalBuffer[12] = 'f';  // 'fmt ' chunk
        finalBuffer[13] = 'm';
        finalBuffer[14] = 't';
        finalBuffer[15] = ' ';
        finalBuffer[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        finalBuffer[17] = 0;
        finalBuffer[18] = 0;
        finalBuffer[19] = 0;
        finalBuffer[20] = 1;  // format = 1
        finalBuffer[21] = 0;
        finalBuffer[22] = (byte) channels;
        finalBuffer[23] = 0;
        finalBuffer[24] = (byte) (longSampleRate & 0xff);
        finalBuffer[25] = (byte) ((longSampleRate >> 8) & 0xff);
        finalBuffer[26] = (byte) ((longSampleRate >> 16) & 0xff);
        finalBuffer[27] = (byte) ((longSampleRate >> 24) & 0xff);
        finalBuffer[28] = (byte) (byteRate & 0xff);
        finalBuffer[29] = (byte) ((byteRate >> 8) & 0xff);
        finalBuffer[30] = (byte) ((byteRate >> 16) & 0xff);
        finalBuffer[31] = (byte) ((byteRate >> 24) & 0xff);
        finalBuffer[32] = (byte) (2 * 16 / 8);  // block align
        finalBuffer[33] = 0;
        finalBuffer[34] = (byte) inBpp; //RECORDER_BPP;  // bits per sample
        finalBuffer[35] = 0;
        finalBuffer[36] = 'd';
        finalBuffer[37] = 'a';
        finalBuffer[38] = 't';
        finalBuffer[39] = 'a';
        finalBuffer[40] = (byte) (totalAudioLen & 0xff);
        finalBuffer[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        finalBuffer[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        finalBuffer[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        for( int i=0; i<totalReadBytes; ++i )
            finalBuffer[44+i] = inBuffer[i];

        FileOutputStream out;
        try {
            out = new FileOutputStream(fn);
             try {
                    out.write(finalBuffer);
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        
    }
	
	public void onRecordStart(){console.log("(AUDIO BYTES overloadable)onRecordStart");}
	public void onRecordEnd(){console.log("(AUDIO BYTES overloadable)onRecordEnd");}
	public void onAnalizeStart(){console.log("(AUDIO BYTES overloadable)onAnalizeStart");}
	public void onAnalizeEnd(){console.log("(AUDIO BYTES overloadable)onAnalizeEnd");}
	
	
}
