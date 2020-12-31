package thuanvo.efs.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import thuanvo.efs.R;


public class GrammarMain extends AppCompatActivity {

    String voc,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_main);

        //startmain
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<Grammar> arrayMain = new ArrayList<>();

        arrayMain.add(new Grammar(getString(R.string.grammar1),getString(R.string.subgram1)));
        arrayMain.add(new Grammar(getString(R.string.grammar2),getString(R.string.subgram2)));
        arrayMain.add(new Grammar(getString(R.string.grammar3),getString(R.string.subgram3)));
        arrayMain.add(new Grammar(getString(R.string.grammar4),getString(R.string.subgram4)));
        arrayMain.add(new Grammar(getString(R.string.grammar5),getString(R.string.subgram5)));
        arrayMain.add(new Grammar(getString(R.string.grammar6),getString(R.string.subgram6)));
        arrayMain.add(new Grammar(getString(R.string.grammar7),getString(R.string.subgram7)));
        arrayMain.add(new Grammar(getString(R.string.grammar8),getString(R.string.subgram8)));
        arrayMain.add(new Grammar(getString(R.string.grammar9),getString(R.string.subgram9)));
        arrayMain.add(new Grammar(getString(R.string.grammar10),getString(R.string.subgram10)));
        arrayMain.add(new Grammar(getString(R.string.grammar11),getString(R.string.subgram11)));
        arrayMain.add(new Grammar(getString(R.string.grammar12),getString(R.string.subgram12)));

        GrammarAdapter adapter = new GrammarAdapter(
                GrammarMain.this,
                R.layout.custom_grammar,
                arrayMain
        );

        listView.setAdapter(adapter);
        //hide scrollbar
        listView.setVerticalScrollBarEnabled(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (position){
                    case 0:
                        voc = "file:///android_asset/grammar/tense1.html";
                        title = getString(R.string.grammar1);
                        send();
                        break;

                    case 1:
                        voc = "file:///android_asset/grammar/tense2.html";
                        title = getString(R.string.grammar2);
                        send();
                        break;

                    case 2:
                        voc = "file:///android_asset/grammar/tense3.html";
                        title = getString(R.string.grammar3);
                        send();
                        break;

                    case 3:
                        voc = "file:///android_asset/grammar/tense4.html";
                        title = getString(R.string.grammar4);
                        send();
                        break;

                    case 4:
                        voc = "file:///android_asset/grammar/tense5.html";
                        title = getString(R.string.grammar5);
                        send();
                        break;

                    case 5:
                        voc = "file:///android_asset/grammar/tense6.html";
                        title = getString(R.string.grammar6);
                        send();
                        break;

                    case 6:
                        voc = "file:///android_asset/grammar/tense7.html";
                        title = getString(R.string.grammar7);
                        send();
                        break;

                    case 7:
                        voc = "file:///android_asset/grammar/tense8.html";
                        title = getString(R.string.grammar8);
                        send();
                        break;

                    case 8:
                        voc = "file:///android_asset/grammar/tense9.html";
                        title = getString(R.string.grammar9);
                        send();
                        break;

                    case 9:
                        voc = "file:///android_asset/grammar/tense10.html";
                        title = getString(R.string.grammar10);
                        send();
                        break;

                    case 10:
                        voc = "file:///android_asset/grammar/tense11.html";
                        title = getString(R.string.grammar11);
                        send();
                        break;

                    case 11:
                        voc = "file:///android_asset/grammar/tense12.html";
                        title = getString(R.string.grammar12);
                        send();
                        break;


                }
            }
        });
        getSupportActionBar().setTitle("Grammar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
    public void send(){
        Intent i = new Intent(GrammarMain.this, GrammarView.class);
        i.putExtra("voc", voc);
        i.putExtra("title",title);
        startActivity(i);
    }
}
