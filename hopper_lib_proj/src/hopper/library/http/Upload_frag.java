package hopper.library.http;

import org.json.JSONObject;

import hopper.library.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;


public class Upload_frag  extends Fragment implements OnClickListener{
	final int SELECT_RETURN = 1;
	private Activity attachedActivity;
	
	private String uploadServiceUrl;
	private String command;
	private String theme;
	private String caption;
	private String fileFilter;
	private OnUploadListener listener;
	
	public Upload_frag(String inUploadServiceUrl, String inCommand, String inTheme, String inCaption, String inFileFilter, OnUploadListener inListener){
		this.uploadServiceUrl = inUploadServiceUrl;
		this.command = inCommand;
		this.theme = inTheme;
		this.caption = inCaption;
		this.fileFilter = inFileFilter;
		this.listener = inListener;
	}
	
	//-------events----------------------------------------------------------------------------------------------------------------------
	//private ArrayList<OnUploadListener> onUploadListenerArrayList = new ArrayList<OnUploadListener>();
	
	public interface OnUploadListener{
	    public void onUpload(Object...objects);
	}
    public void setOnUploadListener(OnUploadListener inListener) {
    	this.listener = inListener;
    }
    private void reportOnUpload(Object...objects){    	
    		this.listener.onUpload(objects);        
    }
	    //--------------
	
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		 View v = inflater.inflate(R.layout.blank, container, false);
		 
		 Intent popUpIntent = new Intent();
		 popUpIntent.setType(this.fileFilter);
		 popUpIntent.setAction(Intent.ACTION_GET_CONTENT);
		 startActivityForResult(Intent.createChooser(popUpIntent, this.caption), SELECT_RETURN);
		 
		 return v;
		 
	 }
	 
	 
	 
	 
	 
	 
	 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        attachedActivity = activity;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_RETURN) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = this.getPath(selectedImageUri);
                console.log("IMAGE UPLOADING IS:-----------------------");
                console.log(selectedImagePath);
                /*File. r;
                Cursor returnCursor = attachedActivity.getContentResolver().query(selectedImageUri, null, null, null, null);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                console.log("-----------FileSize:" + returnCursor.getString(sizeIndex));
                */
                HttpUploadFileFormFields uploadFileHttpObject = new HttpUploadFileFormFields(this.uploadServiceUrl, selectedImagePath);
                String retString = uploadFileHttpObject.upload(this.command,this.theme, selectedImagePath);
                JSONObject json_retFromServer = HopperJsonStatic.createJsonObjectFromJsonString(retString);
                String filePath = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "domainFilePath");
                filePath = filePath.replaceFirst("/", "");
                
                
                reportOnUpload(filePath);
        		//String newFileName = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "domainFilePath");
        		
        		
	            
                
            }
        }
    }
	
	
	public String getPath(Uri uri) {    
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
       
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
		Cursor cursor = attachedActivity.managedQuery(uri, projection, null, null, null);
        if(cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
	}
}
