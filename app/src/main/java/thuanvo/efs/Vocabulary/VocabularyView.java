package thuanvo.efs.Vocabulary;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import thuanvo.efs.R;


public class VocabularyView extends AppCompatActivity {
    String voc, title;
    EditText edtspeak;
    Button btnspeak;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_view);
        WebView webView = (WebView) findViewById(R.id.webView);
        edtspeak = (EditText) findViewById(R.id.edtspeak);
        btnspeak = (Button) findViewById(R.id.btnspeak);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            voc = extras.getString("voc");
            title = extras.getString("title");
        }
        webView.loadUrl(voc);
        //

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            public void onInit(int paramInt)
            {
                if (paramInt != -1)
                    VocabularyView.this.tts.setLanguage(Locale.US);
            }
        });

        //
        btnspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(edtspeak.getText().toString(), 0, null);
            }
        });
        //
        //

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
