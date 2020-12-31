package thuanvo.efs.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import thuanvo.efs.R;


public class ReportEFS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_view);
        WebView wv = (WebView) findViewById(R.id.webView);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl("https://goo.gl/forms/UgrdmDprTspVW13P2");
        wv.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
            {
                paramWebView.loadUrl(paramString);
                return true;
            }
        });

        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}
