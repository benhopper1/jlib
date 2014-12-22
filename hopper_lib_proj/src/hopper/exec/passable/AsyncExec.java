package hopper.exec.passable;

import com.example.hopperlibrary.console;

import android.os.AsyncTask;

public class AsyncExec extends AsyncTask<Object, Integer, Object> {
    
	@Override
	protected Object doInBackground(Object... objs) {
		console.log("doInBackground");
		publishProgress(0);	
        
        return exec(objs);
	}
	
	
    protected void onProgressUpdate(Integer... progress) {
        console.log("onProgressUpdate:"+String.valueOf(progress));
    }

    protected void onPostExecute(Long result) {
        console.log("onPostExecute"+String.valueOf(result));
    }
    protected Object exec(Object... objs){
    	return null;
    }

	

}
