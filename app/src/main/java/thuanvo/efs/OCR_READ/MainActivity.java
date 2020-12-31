/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package thuanvo.efs.OCR_READ;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.Locale;

import thuanvo.efs.Dictionary;
import thuanvo.efs.R;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtv;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "SettingsActivity";

    RadioButton radioeng, radioviet;
    EditText edt;
    Button btnspeak;
    TextToSpeech ttsen;
    ImageButton imgbtnen, imgbtnvi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_main);
        //
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Online translate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Translate.setClientId("ndctvefs");
        Translate.setClientSecret("sR6hMDmaVdtdLgJH2yHyqC7TZnZgv7j939dbWBdvONE=");
        setContentView(R.layout.activity_translate_main);


        txtv = (TextView) findViewById(R.id.textView);
        Button btn = (Button) findViewById(R.id.button);
        btnspeak = (Button) findViewById(R.id.btnspeak);
        Button btndict = (Button) findViewById(R.id.btndict);
        edt =(EditText)findViewById(R.id.editText);
        radioeng = (RadioButton) findViewById(R.id.radioeng);
        radioviet = (RadioButton) findViewById(R.id.radioviet);
        txtv.setMovementMethod(new ScrollingMovementMethod());
        btnspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioeng.isChecked()){
                    promptSpeechInputEN();
                } if (radioviet.isChecked()){
                    promptSpeechInputVI();
                }
            }
        });
        btndict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Dictionary.class);
                i.putExtra("word",edt.getText().toString().toLowerCase());
                startActivity(i);
            }
        });
        //
        imgbtnen = (ImageButton)findViewById(R.id.imgbtnen);
        imgbtnvi = (ImageButton) findViewById(R.id.imgbtnvi);

        imgbtnvi.setVisibility(View.GONE);
        ttsen = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            public void onInit(int paramInt)
            {
                if (paramInt != -1)
                    ttsen.setLanguage(Locale.US);
            }
        });

        //
        imgbtnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsen.speak(edt.getText().toString(), 0, null);
            }
        });
        imgbtnvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsen.speak(txtv.getText().toString(), 0, null);
            }
        });
        //
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
        //

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_text).setOnClickListener(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInputEN() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void promptSpeechInputVI() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }




    public void setbutton(){
        radioeng.setChecked(true);
        radioeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioeng.setChecked(true);
                radioviet.setChecked(false);
                edt.setText(null);
                txtv.setText(null);
                imgbtnvi.setVisibility(View.GONE);
                imgbtnen.setVisibility(View.VISIBLE);
                Button btn = (Button) findViewById(R.id.read_text);
                btn.setVisibility(View.VISIBLE);
            }
        });
        radioviet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioviet.setChecked(true);
                radioeng.setChecked(false);
                edt.setText(null);
                txtv.setText(null);
                imgbtnen.setVisibility(View.GONE);
                imgbtnvi.setVisibility(View.VISIBLE);
                Button btn = (Button) findViewById(R.id.read_text);
                btn.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    edt.setText(R.string.ocr_success);
                    edt.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    edt.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                edt.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode){
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edt.setText(result.get(0));
            }
            break;
        }
    }
    }
}
