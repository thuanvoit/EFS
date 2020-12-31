package thuanvo.efs.Listening;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import thuanvo.efs.R;


public class ListeningMain extends AppCompatActivity {
    MediaPlayer m,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16;
    String link;
    String title;
    Button btnstop;
    int music, vitri;
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_main);

        btnstop = (Button) findViewById(R.id.btnstop);
        final WebView wv = (WebView) findViewById(R.id.webView);
        sound();
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                getSupportActionBar().setTitle("Listening");
            }
        });

        //setList
        //create listview
        ListView listView = (ListView) findViewById(R.id.listView);
        String[] arrData = new String[]{"Unit1", "Unit 2", "Unit 3", "Unit 4",
                "Unit 5", "Unit 6", "Unit 7", "Unit 8",
                "Unit 9", "Unit 10", "Unit 11", "Unit 12",
                "Unit 13", "Unit 14", "Unit 15", "Unit 16",};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, arrData);

        final String asset = "file:///android_asset/listening/unit";
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getSupportActionBar().setTitle(R.string.nameu1);
                        wv.loadUrl(asset+position+".html");
                        if (m1.isPlaying()){
                            m1.pause();
                        } else{
                            m1.start();

                        }
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.nameu2);
                        wv.loadUrl(asset+position+".html");

                        if (m2.isPlaying()){
                            m2.pause();
                        } else{
                            m2.start();
                        }
                        break;
                    case 2:
                        getSupportActionBar().setTitle(R.string.nameu3);
                        wv.loadUrl(asset+position+".html");

                        if (m3.isPlaying()){
                            m3.pause();
                        } else{
                            m3.start();
                        }
                        break;
                    case 3:
                        getSupportActionBar().setTitle(R.string.nameu4);
                        wv.loadUrl(asset+position+".html");

                        if (m4.isPlaying()){
                            m4.pause();
                        } else{
                            m4.start();
                        }
                        break;
                    case 4:
                        getSupportActionBar().setTitle(R.string.nameu5);
                        wv.loadUrl(asset+position+".html");

                        if (m5.isPlaying()){
                            m5.pause();
                        } else{
                            m5.start();
                        }
                        break;
                    case 5:
                        getSupportActionBar().setTitle(R.string.nameu6);
                        wv.loadUrl(asset+position+".html");

                        if (m6.isPlaying()){
                            m6.pause();
                        } else{
                            m6.start();
                        }
                        break;
                    case 6:
                        getSupportActionBar().setTitle(R.string.nameu7);
                        wv.loadUrl(asset+position+".html");

                        if (m7.isPlaying()){
                            m7.pause();
                        } else{
                            m7.start();
                        }
                        break;
                    case 7:
                        getSupportActionBar().setTitle(R.string.nameu8);
                        wv.loadUrl(asset+position+".html");

                        if (m8.isPlaying()){
                            m8.pause();
                        } else{
                            m8.start();
                        }
                        break;
                    case 8:
                        getSupportActionBar().setTitle(R.string.nameu9);
                        wv.loadUrl(asset+position+".html");

                        if (m9.isPlaying()){
                            m9.pause();
                        } else{
                            m9.start();
                        }
                        break;
                    case 9:
                        getSupportActionBar().setTitle(R.string.nameu10);
                        wv.loadUrl(asset+position+".html");

                        if (m10.isPlaying()){
                            m10.pause();
                        } else{
                            m10.start();
                        }
                        break;
                    case 10:
                        getSupportActionBar().setTitle(R.string.nameu11);
                        wv.loadUrl(asset+position+".html");

                        if (m11.isPlaying()){
                            m11.pause();
                        } else{
                            m11.start();
                        }
                        break;
                    case 11:
                        getSupportActionBar().setTitle(R.string.nameu12);
                        wv.loadUrl(asset+position+".html");

                        if (m12.isPlaying()){
                            m12.pause();
                        } else{
                            m12.start();
                        }
                        break;
                    case 12:
                        getSupportActionBar().setTitle(R.string.nameu13);
                        wv.loadUrl(asset+position+".html");

                        if (m13.isPlaying()){
                            m13.pause();
                        } else{
                            m13.start();
                        }
                        break;
                    case 13:
                        getSupportActionBar().setTitle(R.string.nameu14);
                        wv.loadUrl(asset+position+".html");

                        if (m14.isPlaying()){
                            m14.pause();
                        } else{
                            m14.start();
                        }
                        break;
                    case 14:
                        getSupportActionBar().setTitle(R.string.nameu15);
                        wv.loadUrl(asset+position+".html");

                        if (m15.isPlaying()){
                            m15.pause();
                        } else{
                            m15.start();
                        }
                        break;
                    case 15:
                        getSupportActionBar().setTitle(R.string.nameu16);
                        wv.loadUrl(asset+position+".html");

                        if (m16.isPlaying()){
                            m16.pause();
                        } else{
                            m16.start();
                        }
                        break;
                }
            }
        });
        getSupportActionBar().setTitle("Listening");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        back();
        finish();
        onBackPressed();
        return true;
    }
    public void setview(){
        wv.loadUrl("file:///android_asset/listening/unit0.html");
    }
    public void back(){
        m1.stop();
        m2.stop();
        m3.stop();
        m4.stop();
        m5.stop();
        m6.stop();
        m7.stop();
        m8.stop();
        m9.stop();
        m10.stop();
        m11.stop();
        m12.stop();
        m13.stop();
        m14.stop();
        m15.stop();
        m16.stop();
    }
    public void stop(){
        m1.pause();
        m2.pause();
        m3.pause();
        m4.pause();
        m5.pause();
        m6.pause();
        m7.pause();
        m8.pause();
        m9.pause();
        m10.pause();
        m11.pause();
        m12.pause();
        m13.pause();
        m14.pause();
        m15.pause();
        m16.pause();
        m1.seekTo(0);
        m2.seekTo(0);
        m3.seekTo(0);
        m4.seekTo(0);
        m5.seekTo(0);
        m6.seekTo(0);
        m7.seekTo(0);
        m8.seekTo(0);
        m9.seekTo(0);
        m10.seekTo(0);
        m11.seekTo(0);
        m12.seekTo(0);
        m13.seekTo(0);
        m14.seekTo(0);
        m15.seekTo(0);
        m16.seekTo(0);
        sound();
    }

    public void sound(){
        m1 = MediaPlayer.create(ListeningMain.this, R.raw.unit1);
        m2 = MediaPlayer.create(ListeningMain.this, R.raw.unit2);
        m3 = MediaPlayer.create(ListeningMain.this, R.raw.unit3);
        m4 = MediaPlayer.create(ListeningMain.this, R.raw.unit4);
        m5 = MediaPlayer.create(ListeningMain.this, R.raw.unit5);
        m6 = MediaPlayer.create(ListeningMain.this, R.raw.unit6);
        m7 = MediaPlayer.create(ListeningMain.this, R.raw.unit7);
        m8 = MediaPlayer.create(ListeningMain.this, R.raw.unit8);
        m9 = MediaPlayer.create(ListeningMain.this, R.raw.unit9);
        m10 = MediaPlayer.create(ListeningMain.this, R.raw.unit10);
        m11 = MediaPlayer.create(ListeningMain.this, R.raw.unit11);
        m12 = MediaPlayer.create(ListeningMain.this, R.raw.unit12);
        m13 = MediaPlayer.create(ListeningMain.this, R.raw.unit13);
        m14 = MediaPlayer.create(ListeningMain.this, R.raw.unit14);
        m15 = MediaPlayer.create(ListeningMain.this, R.raw.unit15);
        m16 = MediaPlayer.create(ListeningMain.this, R.raw.unit16);
    }
    public void playBeep() {
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd("/raw/unit1.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
