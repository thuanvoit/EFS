package thuanvo.efs.Settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import thuanvo.efs.R;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysettings_main);


        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        //startmain
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<Main> arrayMain = new ArrayList<>();

        arrayMain.add(new Main(R.mipmap.ic_launcher_round,getString(R.string.app_name),getString(R.string.subapp_name)));
        arrayMain.add(new Main(R.drawable.numone,getString(R.string.version),version));
        arrayMain.add(new Main(R.drawable.thuanvo,"Author","Thuan Vo"));
        arrayMain.add(new Main(R.drawable.sub_support,getString(R.string.email),getString(R.string.subemail)));
        arrayMain.add(new Main(R.drawable.sub_report,getString(R.string.report),""));

        MainAdapter adapter = new MainAdapter(
                SettingsActivity.this,
                R.layout.custom_main,
                arrayMain
        );

        listView.setAdapter(adapter);
        //hide scrollbar
        listView.setVerticalScrollBarEnabled(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 4:
                        Intent i = new Intent(SettingsActivity.this, ReportEFS.class);
                        startActivity(i);
                }
            }
        });

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}
