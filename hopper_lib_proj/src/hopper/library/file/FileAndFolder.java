package hopper.library.file;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

public class FileAndFolder {
	
	private FileAndFolder(){}
	
	static public JSONArray getContentsAsJsonArray(String inPath){
		console.log("------------FileAndFolder.getContentsAsJson------------------------------");
		if(inPath == null || inPath.equalsIgnoreCase("")){
			inPath = "/";
		}
		
		if(!(inPath.substring(inPath.length() - 1).equalsIgnoreCase("/"))){
			inPath = inPath + "/";
		}
		
		console.log("thePath:" + inPath);
		
		File f = new File(inPath);
		if(!(f.exists())){console.log("no Exist file"); return null;}
		console.log("Yes Exist file");
		
		if(f == null){console.log("f == null");}
		
		
        File[] files = f.listFiles();
        console.log("Yes Exist file2");
        JSONObject singleFileOrDir_json;
        
        
        JSONArray result_jsonArray = new JSONArray();
        if(files == null){console.log("files == null");return result_jsonArray;}
        
        
        
        console.log("filesCount:" + files.length);
        for (File theFile : files) {
        	singleFileOrDir_json = new JSONObject();
            if (theFile.isDirectory()){
               console.log("folderPath:" + theFile.getAbsolutePath());
               HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "type", "dir");
               HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "dir", theFile.getPath());
               HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "file", theFile.getName()); //with ext
               HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "absPath", theFile.getAbsolutePath()); //with ext
               HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "ext", "");
               HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "byteSize", "");
            }
            if(theFile.isFile()){
            	console.log("filePath:" + theFile.getAbsolutePath());
                HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "type", "file");
                HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "dir", getCurrentFolder(theFile));
                HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "file", theFile.getName()); //with ext
                HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "absPath", theFile.getAbsolutePath()); //with ext
                HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "ext", getExtension(theFile.getName()));
                HopperJsonStatic.putKeyValueStringsForJsonObject(singleFileOrDir_json, "byteSize", String.valueOf(theFile.length()));
            }
            console.log("$$:" + singleFileOrDir_json.toString());
            console.log("--------------------------------------------------------");
            
            HopperJsonStatic.putObjectIntoArray(result_jsonArray, singleFileOrDir_json);
            
            
        }
        console.log(result_jsonArray.toString());
        return result_jsonArray;
	}
	
	static public String getExtension(String inString){
		String filenameArray[] = inString.split("\\.");
		if(filenameArray.length<1){return "";}
        return filenameArray[filenameArray.length-1];		
	}
	
	static private String getCurrentFolder(File inFile){
		String result = inFile.getParentFile().getName();
		if(result.equalsIgnoreCase("")){
			result = "/";
		}
		return result;
	}

}



