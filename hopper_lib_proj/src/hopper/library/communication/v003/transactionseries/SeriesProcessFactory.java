package hopper.library.communication.v003.transactionseries;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.example.hopperlibrary.console;



public class SeriesProcessFactory{
	/*
	 * <string		command		for lookup
	 * , String>	Full pakage.class
	 */
	private static HashMap<String,String> hashOfProcessPackagePaths = new HashMap<String,String>();
	private static HashMap<String,Context> hashOfProcessPackageContext = new HashMap<String,Context>();
	
	//manual add of process, future will replace with package class reader.....
	static{
		/*addProcessPackagePathByCommand("testing", "hopper.application.qrreader.transactionseriesprocess.Testing");
		addProcessPackagePathByCommand("GetAllSmsByNumber", "hopper.application.qrreader.transactionseriesprocess.GetAllSmsByNumber");*/
	}
	
	
	
	public static void addProcessPackagePathByCommand(String inCommand, String inPackagePath, Context inContext){
		console.log("addProcessPackagePathByCommand");
		if(inContext == null){
			console.log("context is null");
		}else{
			console.log("context is NOT null");
		}
		hashOfProcessPackagePaths.put(inCommand, inPackagePath);
		hashOfProcessPackageContext.put(inCommand, inContext);
		console.log("HashOfProcessPackagePaths adding / :" + inCommand + " / "  + inPackagePath);
	}
	
	
	public static void addProcessPackagePathByCommand(String inCommand, String inPackagePath){
		hashOfProcessPackagePaths.put(inCommand, inPackagePath);
		console.log("HashOfProcessPackagePaths adding / :" + inCommand + " / "  + inPackagePath);
	}
	
	
	static public TransactionSeriesProcess getNewInstance(String inProcessName, String inTransactionSeriesId, Object... firstTransmitedObjects){
		console.log("inProcessName :" + inProcessName);
		if(inProcessName != ""){	
			ClassLoader classLoader = SeriesProcessFactory.class.getClassLoader();
			Class aClass = null;
		    try {
		        //aClass = classLoader.loadClass("hopper.application.qrreader.transactionseriesprocess.Testing");
		    	String packagePath = hashOfProcessPackagePaths.get(inProcessName);
		    	if(packagePath != null){
		    		aClass = classLoader.loadClass(packagePath);
		    	}else{
		    		console.log("package not exist");
		    		return null;
		    	}
		    	
		    	
		        System.out.println("aClass.getName() = " + aClass.getName());
		    } catch (ClassNotFoundException e) {
		        e.printStackTrace();
		    }
		    
		    
		    TransactionSeriesProcess process =null;
		    try {
				process = (TransactionSeriesProcess)aClass.newInstance();
				//process.postConstructor(inTransactionSeriesId, firstTransmitedObjects);
				Context tmpContext = hashOfProcessPackageContext.get(inProcessName);
				if(tmpContext != null){
					process.setContext(tmpContext);
				}
				process.postConstructor(inTransactionSeriesId, firstTransmitedObjects);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //Test test = (Test)
		   //process.xxx();
		  // getClassesForPackage(aClass.getPackage());
			
			return process;
		}
		
		return null;
	}
	
	
}