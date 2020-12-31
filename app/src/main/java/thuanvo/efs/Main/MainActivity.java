package thuanvo.efs.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

import thuanvo.efs.Games.GamesActivity;
import thuanvo.efs.Grammar.GrammarMain;
import thuanvo.efs.Listening.ListeningMain;
import thuanvo.efs.MainTab;
import thuanvo.efs.R;
import thuanvo.efs.Settings.SettingsActivity;
import thuanvo.efs.Speaking.SpeakingMain;
import thuanvo.efs.Vocabulary.VocabularyMain;
import thuanvo.efs.Voice.VoiceMain;

public class MainActivity extends AppCompatActivity {

    static final Integer WRITE_EXST = 0x1;
    static final Integer READ_EXST = 0x2;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            public void onInit(int paramInt)
            {
                if (paramInt != -1)
                    tts.setLanguage(Locale.US);
            }
        });
        ImageView img = (ImageView) findViewById(R.id.ic_launcher);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tts.speak("Welcome to EFS! Use now!!", 0, null);
            }
        });

        //request premission storage
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);

        //startmain
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<Main> arrayMain = new ArrayList<>();

        arrayMain.add(new Main(R.drawable.sub_dictionary,getString(R.string.subs_dict),getString(R.string.subst_dict)));
        arrayMain.add(new Main(R.drawable.sub_listen,getString(R.string.subs_listen),getString(R.string.subst_listen)));
        arrayMain.add(new Main(R.drawable.sub_voc,getString(R.string.subs_voc),getString(R.string.subst_voc)));
        arrayMain.add(new Main(R.drawable.sub_grammar,getString(R.string.subs_grammar),getString(R.string.subst_grammar)));
        arrayMain.add(new Main(R.drawable.sub_speaking,getString(R.string.subs_speak),getString(R.string.subst_speak)));
        arrayMain.add(new Main(R.drawable.sub_translate,getString(R.string.subs_trans),getString(R.string.subst_trans)));
        arrayMain.add(new Main(R.drawable.sub_voa,getString(R.string.subs_voice),getString(R.string.subst_voice)));
        arrayMain.add(new Main(R.drawable.sub_test,getString(R.string.subs_games),getString(R.string.subst_games)));
        arrayMain.add(new Main(R.drawable.sub_share,"Share!!",""));
        arrayMain.add(new Main(R.drawable.sub_settings,getString(R.string.subs_sets),getString(R.string.subst_sets)));

        MainAdapter adapter = new MainAdapter(
                MainActivity.this,
                R.layout.custom_main,
                arrayMain
        );

        listView.setAdapter(adapter);
        //hide scrollbar
        listView.setVerticalScrollBarEnabled(false);
        getSupportActionBar().hide();//hide actionbar

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (position){
                    case 0:
                        i = new Intent(MainActivity.this, MainTab.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(MainActivity.this, ListeningMain.class);
                        startActivity(i);
                    break;
                    case 2:
                        i = new Intent(MainActivity.this, VocabularyMain.class);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(MainActivity.this, GrammarMain.class);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(MainActivity.this, SpeakingMain.class);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(MainActivity.this, thuanvo.efs.OCR_READ.MainActivity.class);
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(MainActivity.this, VoiceMain.class);
                        startActivity(i);
                        break;
                    case 7:
                        i = new Intent(MainActivity.this, GamesActivity.class);
                        startActivity(i);
                        break;
                    case 8:
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "English for Students - EFS";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "English for Students");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                    case 9:
                        i = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(i);
                        break;


                }
            }
        });

    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Log.d("PREMISSION",permission + " is already granted.");
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
}
