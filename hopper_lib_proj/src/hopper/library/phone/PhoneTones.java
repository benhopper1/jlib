package hopper.library.phone;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.example.hopperlibrary.HopperTimerOnce;
import com.example.hopperlibrary.console;

public class PhoneTones {
	static HopperTimerOnce timerOnce;
	final ToneGenerator mToneGenerator = new ToneGenerator(AudioManager.STREAM_VOICE_CALL,80);
	public PhoneTones(){
		
	}
	
	public void produceAudioTone(String inToneValue, String inToneDuration){
		if(inToneValue.equalsIgnoreCase("") || inToneDuration.equalsIgnoreCase("")){return;}
		
		console.log("cp-0");
		if(timerOnce != null){
			console.log("cp-1");
			timerOnce.cancel();
			console.log("cp-2");
		}
		mToneGenerator.stopTone();
		console.log("cp-3");
		
		int toneValueInt = Integer.parseInt(inToneValue);
		console.log("cp-3.b");
		mToneGenerator.startTone(toneArray[toneValueInt]);
		console.log("cp-4");
		timerOnce = new HopperTimerOnce("xx", Long.parseLong(inToneDuration)){
			@Override
			public void exec() {
				console.log("cp-5");
				console.log("-------------------ffdfdfddfdfdf hello ben ---------------");
				 mToneGenerator.stopTone();
				 console.log("cp-6");
				//super.exec();
			}
		};
		console.log("cp-7");
	}
	
	//int f =ToneGenerator.TONE_DTMF_1;
	public Integer[] toneArray = {ToneGenerator.TONE_DTMF_0, ToneGenerator.TONE_DTMF_1, ToneGenerator.TONE_DTMF_2, ToneGenerator.TONE_DTMF_3, ToneGenerator.TONE_DTMF_4, ToneGenerator.TONE_DTMF_5, ToneGenerator.TONE_DTMF_6
			, ToneGenerator.TONE_DTMF_7, ToneGenerator.TONE_DTMF_8, ToneGenerator.TONE_DTMF_9, ToneGenerator.TONE_DTMF_A, ToneGenerator.TONE_DTMF_B, ToneGenerator.TONE_DTMF_C, ToneGenerator.TONE_DTMF_D, ToneGenerator.TONE_DTMF_P
			, ToneGenerator.TONE_DTMF_S
	};

}
