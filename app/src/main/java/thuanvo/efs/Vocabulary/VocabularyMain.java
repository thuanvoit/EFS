package thuanvo.efs.Vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import thuanvo.efs.R;


public class VocabularyMain extends AppCompatActivity {
    String voc,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_main);
        //create listview
        GridView gridView = (GridView) findViewById(R.id.gridView);
        ArrayList<Vocabulary> arrayVoc = new ArrayList<>();

        arrayVoc.add(new Vocabulary(R.drawable.unit1,"Unit1"));
        arrayVoc.add(new Vocabulary(R.drawable.unit2,"Unit2"));
        arrayVoc.add(new Vocabulary(R.drawable.unit3,"Unit3"));
        arrayVoc.add(new Vocabulary(R.drawable.unit4,"Unit4"));
        arrayVoc.add(new Vocabulary(R.drawable.unit5,"Unit5"));
        arrayVoc.add(new Vocabulary(R.drawable.unit6,"Unit6"));
        arrayVoc.add(new Vocabulary(R.drawable.unit7,"Unit7"));
        arrayVoc.add(new Vocabulary(R.drawable.unit8,"Unit8"));
        arrayVoc.add(new Vocabulary(R.drawable.unit9,"Unit9"));
        arrayVoc.add(new Vocabulary(R.drawable.unit10,"Unit10"));
        arrayVoc.add(new Vocabulary(R.drawable.unit11,"Unit11"));
        arrayVoc.add(new Vocabulary(R.drawable.unit12,"Unit12"));
        arrayVoc.add(new Vocabulary(R.drawable.unit13,"Unit13"));
        arrayVoc.add(new Vocabulary(R.drawable.unit14,"Unit14"));
        arrayVoc.add(new Vocabulary(R.drawable.unit15,"Unit15"));
        arrayVoc.add(new Vocabulary(R.drawable.unit16,"Unit16"));

        VocabularyAdapter adapter = new VocabularyAdapter(
                VocabularyMain.this,
                R.layout.custom_layout_voc,
                arrayVoc
        );
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //setList
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        voc = "file:///android_asset/vocabulary/unit1.html";
                        title = getString(R.string.unit1);
                        send();
                        break;
                    case 1:
                        voc = "file:///android_asset/vocabulary/unit2.html";
                        title = getString(R.string.unit2);
                        send();
                        break;

                    case 2:
                        voc = "file:///android_asset/vocabulary/unit3.html";
                        title = getString(R.string.unit3);
                        send();
                        break;

                    case 3:
                        voc = "file:///android_asset/vocabulary/unit4.html";
                        title = getString(R.string.unit4);
                        send();
                        break;

                    case 4:
                        voc = "file:///android_asset/vocabulary/unit5.html";
                        title = getString(R.string.unit5);
                        send();
                        break;

                    case 5:
                        voc = "file:///android_asset/vocabulary/unit6.html";
                        title = getString(R.string.unit6);
                        send();
                        break;

                    case 6:
                        voc = "file:///android_asset/vocabulary/unit7.html";
                        title = getString(R.string.unit7);
                        send();
                        break;

                    case 7:
                        voc = "file:///android_asset/vocabulary/unit8.html";
                        title = getString(R.string.unit8);
                        send();
                        break;
                    case 8:
                        voc = "file:///android_asset/vocabulary/unit9.html";
                        title = getString(R.string.unit9);
                        send();
                        break;
                    case 9:
                        voc = "file:///android_asset/vocabulary/unit10.html";
                        title = getString(R.string.unit10);
                        send();
                        break;

                    case 10:
                        voc = "file:///android_asset/vocabulary/unit11.html";
                        title = getString(R.string.unit11);
                        send();
                        break;

                    case 11:
                        voc = "file:///android_asset/vocabulary/unit12.html";
                        title = getString(R.string.unit12);
                        send();
                        break;

                    case 12:
                        voc = "file:///android_asset/vocabulary/unit13.html";
                        title = getString(R.string.unit13);
                        send();
                        break;

                    case 13:
                        voc = "file:///android_asset/vocabulary/unit14.html";
                        title = getString(R.string.unit14);
                        send();
                        break;

                    case 14:
                        voc = "file:///android_asset/vocabulary/unit15.html";
                        title = getString(R.string.unit15);
                        send();
                        break;

                    case 15:
                        voc = "file:///android_asset/vocabulary/unit16.html";
                        title = getString(R.string.unit16);
                        send();
                        break;



                }
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
    public void send(){
        Intent i = new Intent(VocabularyMain.this, VocabularyView.class);
        i.putExtra("voc", voc);
        i.putExtra("title",title);
        startActivity(i);
    }
}
