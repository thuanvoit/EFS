package thuanvo.efs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ContentView extends Activity {

	static final private String CONTENT_TAG = "[Dictionary - Content]";
	private static final String MIMETYPE = "text/html";
    private static final String ENCODING = "UTF-8";
    private WebView wvContent;
    private ImageButton btnBack;
    private ImageButton btnSpeak;
    private ImageButton btnAddfavourite;
    private ImageButton btnRemovefavourite;
    private TextView txttitle;
    private String mCurrentWord;
    private String mSelectedDB;
    private String mContentStyle;
    private int mCurrentWordId;   
	private ArrayList<String> mWordHistory;
	private ArrayList<String> mWordFavourite;
	private SharedPreferences prefs;
	private ProgressDialog pd;
	private TextToSpeech tts;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.content);
	    Intent i = this.getIntent();
	    int wordId = i.getIntExtra("id", -1);
	    mCurrentWord = i.getStringExtra("word");
	    mSelectedDB = i.getStringExtra("db");
	    Log.d(CONTENT_TAG,"current word = " + mCurrentWord);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
      	loadHistoryFromPreferences();
        loadFavouritesFromPreferences();
        
        wvContent = (WebView) findViewById(R.id.wvContent);
    	initWebview();
    	String content = getContentById(wordId);
    	showContent(content);
 
    	saveHistoryToPreferences();
    	
    	Log.d(CONTENT_TAG,"mSelectedDB= " + mSelectedDB);
    	txttitle=(TextView)findViewById(R.id.txttitle);
    	btnSpeak = (ImageButton) findViewById(R.id.btnspeak);
    	btnSpeak.setVisibility(8);
    	if (mSelectedDB.equals("anh_viet"))
    	{
    		txttitle.setText("Từ điển Anh - Việt");
    		btnSpeak.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		txttitle.setText("Từ điển Việt - Anh");
    		btnSpeak.setVisibility(View.GONE);
    	}

    	btnBack = (ImageButton) findViewById(R.id.btnback);
    	btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(CONTENT_TAG, "Start going back");
				menuList();
			}
		});
    	
    	tts = new TextToSpeech(this,ttsInitListener);
    	btnSpeak.setOnClickListener(new View.OnClickListener() {
	 
	          @Override
	          public void onClick(View arg0) {
	                speakOut();
	            }
	 
	        });
    	btnAddfavourite = (ImageButton) findViewById(R.id.btnAddfavourite);
    	btnAddfavourite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(CONTENT_TAG, "Đã thêm vào mục yêu thích");
				addFavourite();
				Toast.makeText(getApplicationContext(), "Đã thêm vào mục yêu thích", Toast.LENGTH_LONG).show();
				saveFavouriteToPreferences();
				btnRemovefavourite.setVisibility(View.VISIBLE);
				btnAddfavourite.setVisibility(View.GONE);
			}
		});
    	btnRemovefavourite = (ImageButton) findViewById(R.id.btnRemovefavourite);
    	btnRemovefavourite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(CONTENT_TAG, "Đã xóa mục yêu thích");
				removeFavouritesFromPreferences();
				Toast.makeText(getApplicationContext(), "Đã xóa khỏi mục yêu thích", Toast.LENGTH_LONG).show();
				btnRemovefavourite.setVisibility(View.GONE);
				btnAddfavourite.setVisibility(View.VISIBLE);
			}
		});
    	
    	showhidebtnFavourites();
	}
    
    public void menuList()
	{
		Intent i=new Intent(this,MainTab.class);
		startActivity(i);
		i.putExtra("word",mCurrentWord);
		finish();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.i(CONTENT_TAG, "onPause()");
		saveHistoryToPreferences();
	}
	public void saveHistoryToPreferences()
	{
		if (prefs.getBoolean("saveHistory", true) && mWordHistory != null && mWordHistory.size() >= 1)
		{
			StringBuilder sbHistory = new StringBuilder();
			for (String item : mWordHistory)
			{
				sbHistory.append(item);
				sbHistory.append("&");
			}
			
			String strHistory = sbHistory.substring(0, sbHistory.length()-1);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("history", strHistory);
			String db="";
			if (mSelectedDB.equals("anh_viet"))
				db="Từ điển Anh-Việt";
			else
				db="Từ điển Việt-Anh";
			editor.putString("database", db);
			editor.commit();
			Log.i(CONTENT_TAG,"history = " + strHistory);
			Log.i(CONTENT_TAG,"History saved!");
		}
	}
	public void saveFavouriteToPreferences()
	{
		if (mWordFavourite != null && mWordFavourite.size() >= 1)
		{
			StringBuilder sbFavourite = new StringBuilder();
			for (String item : mWordFavourite)
			{
				sbFavourite.append(item);
				sbFavourite.append("&");
			}
			
			String strHistory = sbFavourite.substring(0, sbFavourite.length()-1);
			SharedPreferences.Editor editor1 = prefs.edit();
			editor1.putString("favourite", strHistory);
			String db="";
			if (mSelectedDB.equals("anh_viet"))
				db="Từ điển Anh-Việt";
			else
				db="Từ điển Việt-Anh";
			editor1.putString("database", db);
			editor1.commit();
			Log.i(CONTENT_TAG,"favourite = " + strHistory);
			Log.i(CONTENT_TAG,"favourite saved!");
		}
	}
	public void loadHistoryFromPreferences()
	{
		String strHistory = prefs.getString("history", "");
		Log.i(CONTENT_TAG, "History loaded");
		if (strHistory != null && !strHistory.equals(""))
		{
			mWordHistory = new ArrayList<String>(Arrays.asList(strHistory.split("&")));
		}
		else
		{
			Log.i(CONTENT_TAG, "strHistory null");
			if (mWordHistory == null)
			{
			       mWordHistory = new ArrayList<String>();
			}
			else
			{
				mWordHistory.clear();
			}
		}
	}
	public void loadFavouritesFromPreferences()
	{
		String strFavourite = prefs.getString("favourite", "");
		Log.i(CONTENT_TAG, "Favourite loaded");
		if (strFavourite != null && !strFavourite.equals(""))
		{
			mWordFavourite = new ArrayList<String>(Arrays.asList(strFavourite.split("&")));
		}
		else
		{
			Log.i(CONTENT_TAG, "strFavourite null");
			if (mWordFavourite == null)
			{
			       mWordFavourite = new ArrayList<String>();
			}
			else
			{
				mWordFavourite.clear();
			}
		}
	}
    private OnInitListener ttsInitListener = new OnInitListener() {
    @Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            	btnSpeak.setEnabled(false);
                Log.e("TTS", "This Language is not supported");
                Toast.makeText(getApplicationContext(), "This Language is not supported", Toast.LENGTH_LONG).show();
            } else {
                btnSpeak.setEnabled(true);
                //speakOut();
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
 
    }
    };
    
    private void speakOut() {
        tts.speak(mCurrentWord, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    }
    public void initWebview()
    {
    	setContentView(R.layout.content);
    	wvContent = (WebView) findViewById(R.id.wvContent);
	 	wvContent.setBackgroundColor(Color.argb(255, 0, 0, 0));

        wvContent.setWebViewClient(new WebViewClient()
        {
        	public void onPageFinished(WebView view, String url)
        	{
        		if (pd != null)
        		{
        			pd.dismiss();
        			pd = null;
        		}
        	}    	
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url)
        	{
        		Log.i(CONTENT_TAG,"WebView link clicked; url = " + url);
        		try
        		{
            		String arrUrlPart[] = url.split("://");
            		
            		if (arrUrlPart[0].equals("entry"))
            		{
            			String word= IDN.toUnicode(arrUrlPart[1]);
            			Log.i(CONTENT_TAG,"WebView link clicked; word = " + word);
            			String content = getContentByWord(word);
            			showContent(content);
            			showhidebtnFavourites();
            		}
            		else if (arrUrlPart[0].equals("http"))
            		{
            	         try {
            	        	 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            	         } catch (Exception ex) {
                             ex.printStackTrace();
            	         }             			
            		}
        		}
        		catch (Exception ex)
        		{
        			ex.printStackTrace();
        		}
        		return true;
        	}
        });
    }
    
    public String getContentById(int id)
    {
		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + mSelectedDB + "/contentId/" + id);

		Cursor result = managedQuery(uri,null,null,null,null);
		
    	String content;
        if (result != null)
        {
        	result.moveToFirst();
        	content = result.getString(result.getColumnIndex("content"));
        	mCurrentWordId = result.getInt(result.getColumnIndex("id"));
        	mCurrentWord = result.getString(result.getColumnIndex("word"));
        }
        else 
        {
        	content = getString(R.string.errorWordNotFound);
        	mCurrentWordId = -1;
        	mCurrentWord = "";
        }
        content = formatContent(content);
                
        return content;
    }
    
    public String getContentByWord(String word)
    {
		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + mSelectedDB + "/contentWord/" + word);

		Log.i(CONTENT_TAG,"uri = " + uri.toString());
		Cursor result = managedQuery(uri,null,null,null,null);
		
    	String content;
        if (result != null && result.getCount() > 0)
        {
        	result.moveToFirst();    	
        	content = result.getString(result.getColumnIndex("content"));
        	mCurrentWordId = result.getInt(result.getColumnIndex("id"));
        	mCurrentWord = result.getString(result.getColumnIndex("word"));
        }
        else
        {
        	content = getString(R.string.errorWordNotFound) + word;
        	mCurrentWordId = -1;
        	mCurrentWord = "";
        }
        content = formatContent(content);
        
        return content;
    }

    public void saveHistory()
    {
		String item = mSelectedDB + "::" + mCurrentWordId + "::" + mCurrentWord;
		if (mWordHistory.indexOf(item) == -1 && mCurrentWordId != -1) 
		{
			mWordHistory.add(item);
		}
    }
    public void addFavourite()
    {
		String item = mSelectedDB + "::" + mCurrentWordId + "::" + mCurrentWord;
		if (mWordFavourite.indexOf(item) == -1 && mCurrentWordId != -1) 
		{
			mWordFavourite.add(item);
		}
    }
    public void removeFavouritesFromPreferences()
	{
    	if (mWordFavourite != null && mWordFavourite.size() >= 1)
		{
			StringBuilder sbFavourite = new StringBuilder();
			Log.i(CONTENT_TAG,"mCurrentWord=" + mCurrentWord);
			for (String item : mWordFavourite)
			{				
				String arrItemPart[] = item.split("::");
				if (!arrItemPart[2].equals(mCurrentWord))
				{	
					sbFavourite.append(item);
					sbFavourite.append("&");
				}
			}
			String strFavourite;
			SharedPreferences.Editor editor1 = prefs.edit();
			if(sbFavourite.length()!=0)
			{
				strFavourite = sbFavourite.substring(0, sbFavourite.length()-1);
				editor1.putString("favourite", strFavourite);
				Log.i(CONTENT_TAG,"favourite = " + strFavourite);
				Log.i(CONTENT_TAG,mCurrentWord+" đã được xóa khỏi mục yêu thích");
			}
			else
			{
				editor1.putString("favourite", "");
				Log.i(CONTENT_TAG,mCurrentWord+" đã được xóa khỏi mục yêu thích");
				Log.i(CONTENT_TAG,"favourite = null");
			}
			String db="";
			if (mSelectedDB.equals("anh_viet"))
				db="Từ điển Anh-Việt";
			else
				db="Từ điển Việt-Anh";
			editor1.putString("database", db);
			editor1.commit();
			
		}
	}
    public void showhidebtnFavourites()
  	{
    	boolean flag=false;
      	if (mWordFavourite != null && mWordFavourite.size() >= 1)
  		{
  			for (String item : mWordFavourite)
  			{
  				String arrItemPart[] = item.split("::");
  				if (arrItemPart[2].equals(mCurrentWord))
  				{
  					flag=true;
  					break;
  				}
  				else
  				{
  					flag=false;
  				}
  			}
  		}
		if (flag)
		{
			btnRemovefavourite.setVisibility(View.VISIBLE);
			btnAddfavourite.setVisibility(View.GONE);
		}
		else
		{
			btnRemovefavourite.setVisibility(View.GONE);
			btnAddfavourite.setVisibility(View.VISIBLE);
		}		
  	}
    public String formatContent(String content)
    {
    	mContentStyle="body {background-color:#ffffff; color:#e00bf9;font-size:14px; font-family: Tahoma, Arial, Verdana, serif} * {margin:0px; padding:0px;} ul {padding:1px; margin-left:20px;} li{padding:0px;} .type{font-weight:bold;font-size:18px;color:#12ff00;} .example {font-weight:bold;color:#FA8000;} .title{font-weight:bold; font-size:18px; color:#ff0000;} .mexample{color:#000000; font-style:italic;} .aexample{font-weight:bold;color:#FA8000;} .aidiom{font-weight:bold;color:#CC0000;}";
		StringBuilder htmlData = new StringBuilder();
		htmlData.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
		if (mContentStyle != null && !mContentStyle.equals(""))
		{
			htmlData.append("<head><style type=\"text/css\">"+mContentStyle+"</style></head>\n");
		}
		htmlData.append("<body><font face=\"Arial\">");

		htmlData.append(content);
		
		htmlData.append("</font></body></html>");
		
		return htmlData.toString();
    }
    
    public void showContent(String content)
    {
    	if (content != null)
    	{
    		pd = ProgressDialog.show(this, "Working..", "Loading content", true,false);
    		saveHistory();
            wvContent.loadDataWithBaseURL (null, content, MIMETYPE, ENCODING,"about:blank");
    	}
    }


}
