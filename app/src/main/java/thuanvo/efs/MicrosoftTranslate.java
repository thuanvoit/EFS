package thuanvo.efs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.Arrays;


public class MicrosoftTranslate extends Activity {

	
	static final private String TAG = "[MicrosoftTranslate]";
	private String mLanguageNames[];
	private String mLanguageCodes[];
	private ArrayList<String> lstLanguageNames;
	private ArrayList<String> lstLanguageCodes;
	private ArrayAdapter<String> aaLanguageNames;
	
	private EditText InputText;
	private Button TranslateButton;
	private TextView OutputText;
	
	private ImageButton btnChangeDirection;
	private Spinner spnSourceLanguages;
	private Spinner spnDestinationLanguages;
	
	private ProgressDialog dlgProgress;
	private Runnable tTranslate;
	private Handler hShowProgress;
	private String OutputString;
	
	private Language from;
	private Language to;
	
	SharedPreferences prefs;
	@Override
    public void onPause()
    {
    	super.onPause();
       	SharedPreferences.Editor editor = prefs.edit();
       	editor.putString("sourceLanguage", lstLanguageCodes.get(spnSourceLanguages.getSelectedItemPosition()));
       	editor.putString("destinationLanguage", lstLanguageCodes.get(spnDestinationLanguages.getSelectedItemPosition()));
		editor.commit();
    }
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);
	     setContentView(R.layout.translate);
	     
	    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	        
	     String fromLanguage = prefs.getString("sourceLanguage", Language.ENGLISH.toString());
	     String toLanguage = prefs.getString("destinationLanguage", Language.GERMAN.toString());
	        
	     mLanguageNames = (String[]) getResources().getStringArray(R.array.lstLanguageNames);
	     mLanguageCodes = (String[]) getResources().getStringArray(R.array.lstLanguageCodes);
	        
	     btnChangeDirection = (ImageButton) findViewById(R.id.btnChangeDirection);
	        
	     lstLanguageNames = new ArrayList<String>(Arrays.asList(mLanguageNames));
	     lstLanguageCodes = new ArrayList<String>(Arrays.asList(mLanguageCodes));
	        
	     spnSourceLanguages = (Spinner) findViewById(R.id.spnSourceLanguages);
	     spnDestinationLanguages = (Spinner) findViewById(R.id.spnDestinationLanguages);
	        
	     aaLanguageNames = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstLanguageNames);
	     aaLanguageNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	     spnSourceLanguages.setAdapter(aaLanguageNames);
	     spnDestinationLanguages.setAdapter(aaLanguageNames);
	       
	     spnSourceLanguages.setSelection(lstLanguageCodes.indexOf(fromLanguage));
	     spnDestinationLanguages.setSelection(lstLanguageCodes.indexOf(toLanguage));
	     InputText = (EditText)findViewById(R.id.InputText);
	     TranslateButton = (Button)findViewById(R.id.btnTranslate);
	     OutputText = (TextView)findViewById(R.id.OutputText);
	  
	     TranslateButton.setOnClickListener(MyTranslateButtonOnClickListener);
	     btnChangeDirection.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final int temp = spnSourceLanguages.getSelectedItemPosition();
					spnSourceLanguages.setSelection(spnDestinationLanguages.getSelectedItemPosition());
					spnDestinationLanguages.setSelection(temp);			     
				}
			});
	 }

	 private Button.OnClickListener MyTranslateButtonOnClickListener = new Button.OnClickListener(){
	 @SuppressLint("HandlerLeak")
	@Override
	 public void onClick(View v) {
	  final String InputString;
	  InputString = InputText.getText().toString();
	  if(checkInternetConnection())
	  {
		  Log.i(TAG,"Start translating...");
			
			final int sourceLanguage = spnSourceLanguages.getSelectedItemPosition();
			final int destinationLanguage = spnDestinationLanguages.getSelectedItemPosition();
			dlgProgress = ProgressDialog.show(MicrosoftTranslate.this, "Đang dịch...", "Vui lòng đợi!");
			hShowProgress = new Handler()
			{
				public void handleMessage(Message msg)
				{
					Bundle bundle = msg.getData();
					String Output = bundle.getString("bundleOutputString");
					OutputText.setText(Output);
					Log.i(TAG,"Output="+ Output);
					dlgProgress.dismiss();
				}
			};

			for (Language l : Language.values())
			{
				if (l.toString().equals(lstLanguageCodes.get(sourceLanguage)))
				{
					from = l;
					Log.i(TAG,"source = " + from.toString());
				}
				if (l.toString().equals(lstLanguageCodes.get(destinationLanguage)))
				{
					to = l;
					Log.i(TAG,"dest = " + to.toString());
				}
			}
			tTranslate=new Runnable()
			{
				public void run()
				{
					try
					{
						Translate.setClientId("13defca7-decf-4379-af07-11438068a542");
					    Translate.setClientSecret("XzHoTbcDWU04eBxGYRHG+ogwT6ArcorrsPvnOPXJoc4=");
				
					    OutputString = Translate.execute(InputString, from, to);
					    Log.i(TAG,"translatdText="+OutputString);
					    
					    Message msg = hShowProgress.obtainMessage();
					    Bundle bundleOutputString = new Bundle();
					    bundleOutputString.putString("bundleOutputString", OutputString);
					    msg.setData(bundleOutputString);
					    hShowProgress.sendMessage(msg);
						dlgProgress.dismiss();
					}
					catch (Exception ex)
					{
						Log.e(TAG, "Error = " + ex.toString());
						hShowProgress.sendEmptyMessage(0);
						dlgProgress.dismiss();
					}
				}
			};
		Thread thread = new Thread(tTranslate);
		thread.start();
	  }
	  else
	  {
		  Toast.makeText(getApplicationContext(), "Chưa kết nối internet", Toast.LENGTH_SHORT).show();
	  }	  
	 }};
 private boolean checkInternetConnection() {

	 ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

	 if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) 
	 {
		 Log.v(TAG, "Internet Connection Present");
		 return true;
	 } else {
		 Log.v(TAG, "Internet Connection Not Present");
		 return false;
	 }
	 } 
}
