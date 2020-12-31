package thuanvo.efs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class FavouriteView extends Activity {
	private static final String FAVOURITE_TAG = "[Dictionary - FavouriteView] ";
	private ListView mLSTFavourite;
	private ArrayList<String> lstDict;
	private ArrayList<Integer> lstId;
	private ArrayAdapter<String> aptList;
	private ArrayList<String> mWordFavourite;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
		setContentView(R.layout.favourite);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String strFavourite = prefs.getString("favourite", "");
		Log.i(FAVOURITE_TAG, "Favourite loaded");
		if (strFavourite != null && !strFavourite.equals(""))
		{
			mWordFavourite = new ArrayList<String>(Arrays.asList(strFavourite.split("&")));
		}
		else
		{
		    mWordFavourite = new ArrayList<String>();
		}
		
		Log.d(FAVOURITE_TAG,"size mWordFavourite = " + mWordFavourite.size());

		mLSTFavourite = (ListView) findViewById(R.id.lstFavourite);

	    if (lstDict == null)
	    {
			lstDict = new ArrayList<String>();
			lstId = new ArrayList<Integer>();
			aptList = new ArrayAdapter<String>(getApplicationContext(),R.layout.customlist);
	    }
	    lstDict.clear();
	    lstId.clear();
	    aptList.clear();
		if (mWordFavourite != null && mWordFavourite.size() > 0)
		{
			try
			{
		        for (int i=0; i < mWordFavourite.size(); i++)
		        {
		    		Log.i(FAVOURITE_TAG,"item = " + mWordFavourite.get(i));
		    		String arrPart[] = mWordFavourite.get(i).split("::");
		    		if (arrPart.length==3)
		    		{		    			
		                lstDict.add(i,arrPart[0]);
		                lstId.add(i, Integer.parseInt(arrPart[1]));
		        		aptList.add(arrPart[2]);
		    		}
		    		else
		    		{
		    			Log.i(FAVOURITE_TAG,"Wrong entry: " + mWordFavourite.get(i));
		    		}
		        } 
			}
			catch (Exception ex)
			{
				Log.i(FAVOURITE_TAG,"Wrong entry found!");
			}
	    }
	   
		mLSTFavourite.setAdapter(aptList);
		mLSTFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
			{
				Intent i = new Intent(v.getContext(), ContentView.class);
				i.putExtra("word", aptList.getItem(arg2));
				Log.i(FAVOURITE_TAG, aptList.getItem(arg2));
    			i.putExtra("id",lstId.get(arg2));
    			i.putExtra("db",lstDict.get(arg2));
                startActivity(i);
			}
		});
		
	}
}
