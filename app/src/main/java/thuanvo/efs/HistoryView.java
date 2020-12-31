package thuanvo.efs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryView extends Activity {
	private static final String HISTORY_TAG = "[Dictionary - HistoryView] ";
	private ListView mLSTHistory;
	private ArrayList<String> lstDict;
	private ArrayList<Integer> lstId;
	private ArrayAdapter<String> aptList;
	private ArrayList<String> mWordHistory;
	private SharedPreferences prefs;

	Menu menu;
	@Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   	  super.onCreateOptionsMenu(menu);

   	  int groupId = 0;
   	  this.menu=menu;

   	  menu.add(groupId,1,1, "Xóa lịch sử").setIcon(R.drawable.clear);
   	  return true;
   	}

    public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
				
		switch (item.getItemId()) {
			case (1): 
			{
				ClearHistory();
				break;
			}
		}
	    return true;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
		setContentView(R.layout.history);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String strHistory = prefs.getString("history", "");
		Log.i(HISTORY_TAG, "History loaded");
		if (strHistory != null && !strHistory.equals(""))
		{
			mWordHistory = new ArrayList<String>(Arrays.asList(strHistory.split("&")));
		}
		else
		{
		    mWordHistory = new ArrayList<String>();
		}
		
		Log.d(HISTORY_TAG,"size mWordHistory = " + mWordHistory.size());

		mLSTHistory = (ListView) findViewById(R.id.lstHistory);

	    if (lstDict == null)
	    {
			lstDict = new ArrayList<String>();
			lstId = new ArrayList<Integer>();
			aptList = new ArrayAdapter<String>(getApplicationContext(),R.layout.customlist);
	    }
	    lstDict.clear();
	    lstId.clear();
	    aptList.clear();
		if (mWordHistory != null && mWordHistory.size() > 0)
		{
			try
			{
		        for (int i=0; i < mWordHistory.size(); i++)
		        {
		    		Log.i(HISTORY_TAG,"item = " + mWordHistory.get(i));
		    		String arrPart[] = mWordHistory.get(i).split("::");
		    		if (arrPart.length==3)
		    		{
		    			//Log.i(CONTENT_TAG, "loaded content " +arrPart.length + ", wordId = " + arrPart[1]);
		                lstDict.add(i,arrPart[0]);
		                lstId.add(i, Integer.parseInt(arrPart[1]));
		        		aptList.add(arrPart[2]);
		    		}
		    		else
		    		{
		    			Log.i(HISTORY_TAG,"Wrong entry: " + mWordHistory.get(i));
		    		}
		        } 
			}
			catch (Exception ex)
			{
				Log.i(HISTORY_TAG,"Wrong entry found!");
			}
	    }
	   
		mLSTHistory.setAdapter(aptList);
		mLSTHistory.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
			{
				Intent i = new Intent(v.getContext(), ContentView.class);
				i.putExtra("word", aptList.getItem(arg2));
				Log.i(HISTORY_TAG, aptList.getItem(arg2));
    			i.putExtra("id",lstId.get(arg2));
    			i.putExtra("db",lstDict.get(arg2));
                startActivity(i);
			}
		});
		
	}			
	public void ClearHistory() 
	{
		mWordHistory.clear();
		aptList.clear();
		mLSTHistory.setAdapter(aptList);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("history", "");
		editor.commit();
	}	
}
