package thuanvo.efs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AddNewWord extends Activity {
	static final private String AddNewWord_TAG = "[Dictionary - Add new word]";
	final int CONTEXT_MENU_DELETE =1;
	final int CONTEXT_MENU_UPDATE =2;
	private Button btndongy;
	private EditText edtnewword;
	private EditText edtcontent;
	private ListView lstWord;
	private String dbname;
	public ArrayList<String> mLSTCurrentWord;
	public ArrayList<Integer> mLSTCurrentWordId;
    private ArrayAdapter<String> mAdapter;
    private boolean isAdd=true;
    private SharedPreferences prefs;
    private ArrayList<String> mWordHistory;
	private ArrayList<String> mWordFavourite;
	 	
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    String word = ((TextView) info.targetView).getText().toString();
	    menu.setHeaderTitle(word);      
	    menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Xoá");
	    menu.add(Menu.NONE, CONTEXT_MENU_UPDATE, Menu.NONE, "Chỉnh sửa");
	}
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final String word = ((TextView) info.targetView).getText().toString();
       
        switch (item.getItemId()) {
        case CONTEXT_MENU_DELETE:
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewWord.this);
     		alertDialog.setTitle("Thông báo");
     		alertDialog.setMessage("Bạn có muốn xóa từ "+word);
			alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	deleteNewword(getIdfromword(word));
            	removeFavouritesFromPreferences(word);
            	removeHistoryFromPreferences(word);
            	showNewWordlist();
            }
        }); 
			 alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {

		            dialog.cancel();
		            }
		        });
		        alertDialog.show();
            return (true);
        case CONTEXT_MENU_UPDATE:
        	edtcontent.setText(getContentfromword(word));
        	edtnewword.setText(word);
        	isAdd=false;
        }
       
        return (super.onOptionsItemSelected(item));
    }
    	@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);     
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    setContentView(R.layout.addnewword);
		    
		    Intent i = this.getIntent();
		    dbname  = i.getStringExtra("dbname");
		    edtnewword=(EditText)findViewById(R.id.edtaddnewword);
		    edtcontent=(EditText)findViewById(R.id.edtcontent);
		    btndongy = (Button) findViewById(R.id.btndongy);
		    mAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.customlist);
		    mLSTCurrentWordId = new ArrayList<Integer>();
	    	mLSTCurrentWord = new ArrayList<String>();
		    lstWord = (ListView) findViewById(R.id.lstNewWord);
		    registerForContextMenu(lstWord);
		    showNewWordlist();
		    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    loadHistoryFromPreferences();
		    loadFavouritesFromPreferences();
	    	btndongy.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final String word;
					final String content;
	    		 	word=edtnewword.getText().toString();
	    		 	content=edtcontent.getText().toString();
	    		 	if (isAdd)
	    		 	{
						if (word != null && content!=null && !word.equals("") && !content.equals(""))
		    			{
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewWord.this);
							alertDialog.setTitle("Thông báo");
							alertDialog.setMessage("Bạn có muốn thêm từ mới?");
							alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	if (!checkExistWord(word))
				            	{				            	
						            int maxid=getMaxId()+1;
						            //String content1="<ul><li>"+content+"</li></ul>";
						            insertNewword(maxid,word,content);
						            showNewWordlist();
				            	}
				            	else
				            		Toast.makeText(getApplicationContext(), "Từ này đã tồn tại trong cơ sở dữ liệu!", Toast.LENGTH_LONG).show();
				            }
				        });  
				        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
	
				            dialog.cancel();
				            }
				        });
				        alertDialog.show();
		    			}
		    			else
		    				Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
	    		 	}
	    		 	else
	    		 	{
	    		 		AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewWord.this);
						alertDialog.setTitle("Thông báo");
						alertDialog.setMessage("Bạn có muốn chỉnh sửa từ?");
						alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			    		 	updateNewword(getIdfromword(word), word, content);
					        showNewWordlist();
					        isAdd=true;
			            }
			        });  
			        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            	isAdd=true;
			            dialog.cancel();
			            }
			        });
			        alertDialog.show();
	    		 	}
	    		 		
				}
			});	    	
	 }
	 
	 	 public boolean checkExistWord(String word)
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/contentWord/" + word);

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		Cursor result = managedQuery(uri,null,null,null,null);
	         if (result != null && result.getCount() > 0)
	         {
	        	 Log.i(AddNewWord_TAG,"result != null");
	         	return true;
	         }
	         else
	          
	         return false;
	     }
		 public int getMaxId()
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/getmaxid/");

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		int id;
	 		Cursor result = managedQuery(uri,null,null,null,null);
	         if (result != null && result.getCount() > 0)
	         {
	        	 result.moveToFirst();
	        	 id= result.getInt(result.getColumnIndex("maxid"));
	        	 Log.i(AddNewWord_TAG,"maxid= "+id);
	         }
	         else
	        	 id=-1;
	        	 
	        return id;
	     }
		 public int getIdfromword(String word)
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/getid/"+word);

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		int id;
	 		Cursor result = managedQuery(uri,null,null,null,null);
	         if (result != null && result.getCount() > 0)
	         {
	        	 result.moveToFirst();
	        	 id= result.getInt(result.getColumnIndex("id"));
	        	 Log.i(AddNewWord_TAG,"id= "+id);
	         }
	         else
	        	 id=-1;
	        	 
	        return id;
	     }
		 public String getContentfromword(String word)
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/list/"+word);

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		String content;
	 		Cursor result = managedQuery(uri,null,null,null,null);
	         if (result != null && result.getCount() > 0)
	         {
	        	 result.moveToFirst();
	        	 content= result.getString(result.getColumnIndex("content"));
	        	 Log.i(AddNewWord_TAG,"content= "+content);
	         }
	         else
	        	 content=null;
	        	 
	        return content;
	     }
		 public void insertNewword(int id, String word, String content)
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/insertWord/"+id+"/"+word+"/"+content);

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		managedQuery(uri,null,null,null,null);
	        	 
	     }
		 public void updateNewword(int id, String word, String content)
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/updateWord/"+id+"/"+word+"/"+content);

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		managedQuery(uri,null,null,null,null);
	        	 
	     }
		 public void deleteNewword(int id)
	     {
	 		Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/delete/"+id);

	 		Log.i(AddNewWord_TAG,"uri = " + uri.toString());
	 		managedQuery(uri,null,null,null,null);
	        	 
	     }
		 public void showNewWordlist()
		 {
				Log.i(AddNewWord_TAG, "Start showWordList");
				Uri uri = Uri.parse("content://thuanvo.efs.DictionaryProvider/dict/" + dbname + "/newlist/");
				try
				{
					Cursor result = managedQuery(uri,null,null,null,null);
					
			        if (result != null)
			        {
			        	int countRow=result.getCount();
			    		Log.i(AddNewWord_TAG, "countRow = " + countRow);
			    		mLSTCurrentWord.clear();
			    		mLSTCurrentWordId.clear();
			    		mAdapter.clear();
			        	if (countRow >= 1)
			        	{
			        	   	int indexWordColumn = result.getColumnIndex("word");
			        	   	Log.i(AddNewWord_TAG,"indexWordColumn = "+ indexWordColumn);
			        	   	int indexIdColumn = result.getColumnIndex("id");
			        	   	Log.i(AddNewWord_TAG, "indexIdColumn="+indexIdColumn);

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
					//assign result return
					lstWord.setAdapter(mAdapter);
				}
				catch (Exception ex)
				{
					Log.e(AddNewWord_TAG, "Error = " + ex.toString());
				}

		   }
		 
			public void loadHistoryFromPreferences()
			{
				String strHistory = prefs.getString("history", "");
				Log.i(AddNewWord_TAG, "History loaded");
				if (strHistory != null && !strHistory.equals(""))
				{
					mWordHistory = new ArrayList<String>(Arrays.asList(strHistory.split("&")));
				}
				else
				{
					Log.i(AddNewWord_TAG, "strHistory null");
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
				Log.i(AddNewWord_TAG, "Favourite loaded");
				if (strFavourite != null && !strFavourite.equals(""))
				{
					mWordFavourite = new ArrayList<String>(Arrays.asList(strFavourite.split("&")));
				}
				else
				{
					Log.i(AddNewWord_TAG, "strFavourite null");
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
		    public void removeFavouritesFromPreferences(String word)
			{
		    	if (mWordFavourite != null && mWordFavourite.size() >= 1)
				{
					StringBuilder sbFavourite = new StringBuilder();
					for (String item : mWordFavourite)
					{				
						String arrItemPart[] = item.split("::");
						if (!arrItemPart[2].equals(word))
						{	
							sbFavourite.append(item);
							sbFavourite.append("&");
						}
					}
					SharedPreferences.Editor editor1 = prefs.edit();
					if(sbFavourite.length()!=0)
					{
						String strFavourite = sbFavourite.substring(0, sbFavourite.length()-1);
						editor1.putString("favourite", strFavourite);
						editor1.commit();
						Log.i(AddNewWord_TAG,"favourite = " + strFavourite);
						Log.i(AddNewWord_TAG,word+" đã được xóa khỏi mục yêu thích");
					}
					else
					{
						editor1.putString("favourite","");
						editor1.commit();
						Log.i(AddNewWord_TAG,"favourite = null");
					}
				}
			}
		    public void removeHistoryFromPreferences(String word)
			{
		    	if (mWordHistory != null && mWordHistory.size() >= 1)
				{
					StringBuilder sbHistory = new StringBuilder();
					for (String item : mWordHistory)
					{				
						String arrItemPart[] = item.split("::");
						if (!arrItemPart[2].equals(word))
						{	
							sbHistory.append(item);
							sbHistory.append("&");
						}
					}
					SharedPreferences.Editor editor1 = prefs.edit();
					if(sbHistory.length()!=0)
					{
						String strHistory = sbHistory.substring(0, sbHistory.length()-1);
						editor1.putString("history", strHistory);				
						editor1.commit();
						Log.i(AddNewWord_TAG,"history = " + strHistory);
						Log.i(AddNewWord_TAG,word+" đã được xóa khỏi lịch sử");
					}
					else
					{
						editor1.putString("history", "");				
						editor1.commit();
						Log.i(AddNewWord_TAG,"history = null");
					}
				}
			}
}
