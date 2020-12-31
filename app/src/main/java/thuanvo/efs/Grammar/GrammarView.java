package thuanvo.efs.Grammar;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import thuanvo.efs.R;


public class GrammarView extends AppCompatActivity {
    String voc, title;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_view);
        WebView webView = (WebView) findViewById(R.id.webView);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            voc = extras.getString("voc");
            title = extras.getString("title");
        }
        webView.loadUrl(voc);
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
