package hopper.library.email;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Email {
	private Activity activity;
	public Email(Activity inActivity){
		activity = inActivity;
	};
	
	public void send(String toEmailAddress, String inSubject, String inBody){
		String[] recipients = {toEmailAddress};
		
		Intent email = new Intent(android.content.Intent.ACTION_SEND);//Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));	
		// prompts email clients only	
		//email.setType("message/rfc822")
		email.setType("plain/text");  	
		email.putExtra(Intent.EXTRA_EMAIL, recipients);	
  		email.putExtra(Intent.EXTRA_SUBJECT, inSubject);	
  		email.putExtra(Intent.EXTRA_TEXT, inBody);	
      	try {	
	        // the user can choose the email client	
      		activity.startActivity(email);//.createChooser(email, "Choose an email client from..."));	
      	} catch (android.content.ActivityNotFoundException ex) {	
      		Toast.makeText(activity, "No email client installed.",	
			Toast.LENGTH_LONG).show();	
      	}
	}
}
