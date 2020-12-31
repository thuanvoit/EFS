package thuanvo.efs.Translate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import thuanvo.efs.R;

import static thuanvo.efs.R.id.textView;

public class TranslateMain extends AppCompatActivity {
RadioButton radioeng, radioviet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Translate.setClientId("ndctvefs");
        Translate.setClientSecret("sR6hMDmaVdtdLgJH2yHyqC7TZnZgv7j939dbWBdvONE=");
        setContentView(R.layout.activity_translate_main);

        final TextView txtv = (TextView) findViewById(textView);
        Button btn = (Button) findViewById(R.id.button);
        final EditText edt =(EditText)findViewById(R.id.editText);
        radioeng = (RadioButton) findViewById(R.id.radioeng);
        radioviet = (RadioButton) findViewById(R.id.radioviet);
        txtv.setMovementMethod(new ScrollingMovementMethod());

        setbutton();
        final ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        txtv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text;
                text = txtv.getText().toString();

                ClipData myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            if (radioeng.isChecked()){
                                String translated = Translate.execute(edt.getText().toString(), Language.ENGLISH, Language.VIETNAMESE);
                                txtv.setText(translated);
                            } if (radioviet.isChecked()){
                                String translated = Translate.execute(edt.getText().toString(), Language.VIETNAMESE, Language.ENGLISH);
                                txtv.setText(translated);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


        getSupportActionBar().setTitle("Vocabulary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
    public void setbutton(){
        radioeng.setChecked(true);
        radioeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioeng.setChecked(true);
                radioviet.setChecked(false);
            }
        });
        radioviet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioviet.setChecked(true);
                radioeng.setChecked(false);
            }
        });
    }
}

