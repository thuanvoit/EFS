package thuanvo.efs;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;


public class MyTextToSpeech extends Activity implements OnInitListener {

	 	private TextToSpeech tts;
	    private Button btnSpeak;
	    private EditText txtText;
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_my_text_to_speech);
	 
	        tts = new TextToSpeech(getApplicationContext(), this);
	 
	        btnSpeak = (Button) findViewById(R.id.btn_speak);
	 
	        txtText = (EditText) findViewById(R.id.et_content);
	 
	        // button on click event
	        btnSpeak.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View arg0) {
	                speakOut();
	            }
	 
	        });
	    }
	 
	    @Override
	    public void onDestroy() {
	        if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	        }
	        super.onDestroy();
	    }
	 
	    @Override
	    public void onInit(int status) {
	 
	        if (status == TextToSpeech.SUCCESS) {
	 
	            int result = tts.setLanguage(Locale.US);
	 
	            if (result == TextToSpeech.LANG_MISSING_DATA
	                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	            	btnSpeak.setEnabled(false);
	                Log.e("TTS", "This Language is not supported");
	                Toast.makeText(getApplicationContext(), "This Language is not supported", Toast.LENGTH_LONG).show();
	            } else {
	                btnSpeak.setEnabled(true);
	                speakOut();
	            }
	 
	        } else {
	            Log.e("TTS", "Initilization Failed!");
	        }
	 
	    }
	 
	    private void speakOut() {
	        String text = txtText.getText().toString();
	        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	    }

}
