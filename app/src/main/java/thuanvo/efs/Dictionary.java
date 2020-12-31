
package thuanvo.efs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class Dictionary extends Activity {
	
	static final private String MAIN_TAG = "[Dictionary]";
	private String mDBPath;
	private DatabaseFileList mDBList;
	private DatabaseFile mDBFile;
	private SharedPreferences prefs;
    private EditText edWord;
    private ListView lstWord;
    private Button btnthem;
    private AbsSpinner sprDictionaryManager;
	public ArrayList<String> mLSTCurrentWord;
	public ArrayList<Integer> mLSTCurrentWordId;
    private ArrayAdapter<String> mAdapter;
    private Handler mHandler;
    private Runnable mUpdateTimeTask;
    InputMethodManager imm;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mDBPath = Environment.getExternalStorageDirectory() + "/Dict_Data/";
        mDBList = new DatabaseFileList(mDBPath);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sprDictionaryManager= (Spinner) findViewById(R.id.spinner_choose);
        manageDictionary();
        loadPreferences();        
        btnthem=(Button)findViewById(R.id.btnthemtu);
        btnthem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), AddNewWord.class);
				i.putExtra("dbname", mDBFile.fileName);
                startActivity(i);
			}
        });
        if (mDBList.items.size() > 0)
        {
            menuMain();
        }
        else
        {
			new AlertDialog.Builder(this)
			.setMessage("Không tìm thấy dữ liệu thích hợp")
			.setTitle("Thông báo lỗi")
			.setNeutralButton("Chấp nhận",
			   new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int whichButton){}
			   })
			.show();        
        }
    }
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   	  super.onCreateOptionsMenu(menu);
   	  int groupId = 0;
   	  this.menu=menu;   	  
   	  menu.add(groupId, 1, 1, "About").setIcon(R.drawable.info);
   	  return true;
   	}

    public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
			case (1): 
			{
				Intent i=new Intent(this,AboutUs.class);
				startActivity(i);
				break;
			}
		}
	    return true;
	}

    @Override
    public void onPause()
    {
    	super.onDestroy();
    }
    public void menuMain()
    {
		Log.i(MAIN_TAG, "Start menuMain");
    	edWord = (EditText) findViewById(R.id.edWord);
    	lstWord = (ListView) findViewById(R.id.lstWord);
    	mLSTCurrentWordId = new ArrayList<Integer>();
    	mLSTCurrentWord = new ArrayList<String>();
    	
    	mAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.customlist);
    
    	showWordlist();
    	edWord.requestFocus();
    	   	
    	mHandler = new Handler();
    	mUpdateTimeTask = new Runnable()
    	{
    		public void run()
    		{
    			Log.i(MAIN_TAG, "update word list now");
    			edWord.setEnabled(false);
    			showWordlist();
    			edWord.setEnabled(true);
    		}
    	};
    	
    	edWord.addTextChangedListener(new TextWatcher()
    	{ 
    		public void afterTextChanged(Editable s)
    		{    		
                mHandler.removeCallbacks(mUpdateTimeTask);
                mHandler.postDelayed(mUpdateTimeTask,2000);
    		}
    		
    		public void beforeTextChanged(CharSequence s, int start, int count, int after)
    		{;} 
    	
    	    public void onTextChanged(CharSequence s, int start, int before, int count)
    		{;}
    	});

    	lstWord.setOnItemClickListener(new AdapterView.OnItemClickListener()
    	{
    		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
    		{
    			imm.hideSoftInputFromWindow(edWord.getWindowToken(), 0);
    			Intent i = new Intent(v.getContext(), ContentView.class);
    			i.putExtra("word", mAdapter.getItem(arg2));
    			i.putExtra("id",mLSTCurrentWordId.get(arg2));
    			i.putExtra("db", mDBFile.fileName);
    			i.putExtra("dbName",mDBFile.dictionaryName);
                startActivity(i);
    		}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);    	
    }

	public void savePreferences()
	{
		Log.d(MAIN_TAG, "Saving default dictionary = " + mDBFile.path + "-" + mDBFile.fileName);
       	SharedPreferences.Editor editor = prefs.edit();
       	editor.putString("defaultDictionary", mDBFile.fileName);
       	editor.putString("defaultDictionaryPath", mDBFile.path);
		editor.commit();
	}
	
	public void loadPreferences()
	{
		boolean found = false;
		String savedDB = prefs.getString("defaultDictionary", ""); //Default dictionary is the first dictionary in the list
		String savedDBPath = prefs.getString("defaultDictionaryPath", ""); //Default dictionary is the first dictionary in the list
		if (savedDB.trim().equals("") || savedDBPath.trim().equals(""))
		{
			Log.i(MAIN_TAG,"Error in loading default dictionary");
			if (mDBList != null && mDBList.items.size() > 0)
			{
				mDBFile = mDBList.items.get(0);
				Log.d(MAIN_TAG,"Use the first item = " + mDBFile.path + " | filename = " + mDBFile.fileName);
			}
			else
			{
				mDBFile = null;
			}
		}
		else
		{
			Log.i(MAIN_TAG,"Loaded default dictionary = " + savedDBPath + " - " + savedDB);
			if (mDBList != null && mDBList.items.size() > 0)
			{
				for (DatabaseFile d : mDBList.items)
				{
					if (d.fileName.equals(savedDB) && d.path.equals(savedDBPath))
					{
						mDBFile = d;
						found = true;
						break;
					}
				}
				if (!found)
				{
					//when the database is deleted, set mSelectedDB to the first element of database list
					mDBFile = mDBList.items.get(0);
					Log.i(MAIN_TAG, "Database file is not in list anymore, use the first one of list");
				}
			}
			else
			{
				Log.d(MAIN_TAG,"No database found");
				mDBFile = null;
			}
		}
		if (mDBFile != null)
		{
			Log.d(MAIN_TAG,"default path = " + mDBFile.path + " | filename = " + mDBFile.fileName);
		}
		else
		{
			Log.i(MAIN_TAG,"No database found!");
		}
	}
    public void showWordlist()
    {
		Log.i(MAIN_TAG, "Start showWordList");
		edWord.setEnabled(false);
		String word = edWord.getText().toString();
		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + mDBFile.fileName + "/list/" + word);
		try
		{
			Cursor result = managedQuery(uri,null,null,null,null);
			
	        if (result != null)
	        {
	        	int countRow=result.getCount();
	    		Log.i(MAIN_TAG, "countRow = " + countRow);
	    		mLSTCurrentWord.clear();
	    		mLSTCurrentWordId.clear();
	    		mAdapter.clear();
	        	if (countRow >= 1)
	        	{
	        	   	int indexWordColumn = result.getColumnIndex("word");
	        	   	Log.i(MAIN_TAG,"indexWordColumn = "+ indexWordColumn);
	        	   	int indexIdColumn = result.getColumnIndex("id");
	        	   	Log.i(MAIN_TAG, "indexIdColumn="+indexIdColumn);

	                result.moveToFirst();
	        		String strWord;
	        		int intId;

	        		int i = 0;
	                do
	                {
	                	strWord = result.getString(indexWordColumn);
	                	intId = result.getInt(indexIdColumn);
	                    mLSTCurrentWord.add(i,strWord);
	                    mLSTCurrentWordId.add(i,intId);
	            		mAdapter.add(strWord);
	                    i++;
	                } while (result.moveToNext()); 
	            }
	           
	            result.close();
	        }
			lstWord.setAdapter(mAdapter);
		}
		catch (Exception ex)
		{
			Log.e(MAIN_TAG, "Error = " + ex.toString());
		}
		edWord.setEnabled(true);
    }
    
	
	public void manageDictionary()
	{
		ArrayList<String> items=new ArrayList<String>();
		
		Log.d(MAIN_TAG, "Size of mDBList = " + mDBList.items.size());
		for (int i=0; i < mDBList.items.size(); i++)
		{     
            items.add(mDBList.items.get(i).dictionaryName);
            Log.d(MAIN_TAG, "item= "+ items.get(i) );
            Log.d(MAIN_TAG,mDBList.items.get(i).path + " |  " + mDBList.items.get(i).fileName);
		}
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprDictionaryManager.setAdapter(dataAdapter);
        
        String db = prefs.getString("database","Từ điển Anh-Việt");
        Log.d(MAIN_TAG, "db= " + db);
        for(int i = 0; i < dataAdapter.getCount(); i++)
        {
        	Log.d(MAIN_TAG, "sprDictionaryManager.getSelectedItem " + sprDictionaryManager.getItemAtPosition(i).toString());
            if (db.equals(dataAdapter.getItem(i)) )
            {
            	sprDictionaryManager.setSelection(i);
            }
        }
        sprDictionaryManager.setOnItemSelectedListener(new OnItemSelectedListener()
        {
        	 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		mDBFile = mDBList.items.get(arg2);
        		Log.i(MAIN_TAG,"Loaded dictionary " + mDBFile.path + " | " + mDBFile.fileName);
        		savePreferences();
        		menuMain();
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
        });      
	}
}