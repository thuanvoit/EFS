package thuanvo.efs;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class DictionaryProvider extends ContentProvider {

	public static final String PROVIDER_TAG = "[DictionaryProvider]";
	public static final String PROVIDER_NAME = "thuanvo.efs.DictionaryProvider";
	private static final int CODE_LIST_EMPTY = 0;
	private static final int CODE_LIST = 1;
	private static final int CODE_CONTENT_FROM_ID = 2;
	private static final int CODE_CONTENT_FROM_WORD = 3;
	private static final int CODE_INSERT = 4;
	private static final int CODE_MAXID = 5;
	private static final int CODE_NEW_LIST = 6;
	private static final int CODE_DELETE = 7;
	private static final int CODE_GET_ID = 8;
	private static final int CODE_UPDATE = 9;
	private String mDBExtension;
	private String mDBPath;
	private String mCurrentDB = null;
	
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/list/", CODE_LIST_EMPTY);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/list/*", CODE_LIST);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/contentId/#", CODE_CONTENT_FROM_ID);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/contentWord/*", CODE_CONTENT_FROM_WORD);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/insertWord/#/*/*", CODE_INSERT);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/updateWord/#/*/*", CODE_UPDATE);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/getmaxid/", CODE_MAXID);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/newlist/", CODE_NEW_LIST);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/delete/*", CODE_DELETE);
		uriMatcher.addURI(PROVIDER_NAME, "dict/*/getid/*", CODE_GET_ID);
	}

	private DictionaryEngine mDBEngine;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
        mDBExtension = getContext().getResources().getString(R.string.dbExtension);
        mDBPath = Environment.getExternalStorageDirectory() + "/Dict_Data/";
        mDBEngine = new DictionaryEngine();
		Log.i(PROVIDER_TAG,">>> DictionaryProvider is ready <<<");
        return true;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		String strDB = uri.getPathSegments().get(1);
		Log.i(PROVIDER_TAG,strDB);
		if (mCurrentDB == null || !mCurrentDB.equals(strDB))
		{
			mCurrentDB = strDB;
			mDBEngine.setDatabaseFile(mDBPath, strDB, mDBExtension);
		}
		if (mDBEngine == null)
		{
        	Log.e(PROVIDER_TAG,"Can not create database engine");
			return null;
		}	
		String word;
		String content;
		String dbname;
		int wordId;
		Cursor c;
		
		switch (uriMatcher.match(uri))
		{
			case CODE_LIST_EMPTY:
				Log.d(PROVIDER_TAG,"LIST_EMPTY");
				c = mDBEngine.getCursorWordList("");
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;	
			case CODE_LIST:
				word = uri.getPathSegments().get(3);	
				Log.d(PROVIDER_TAG,"LIST word = " + word);
				c = mDBEngine.getCursorWordList(word);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_NEW_LIST:
				dbname = uri.getPathSegments().get(1);	
				Log.d(PROVIDER_TAG,"LIST new word = " + dbname);
				c = mDBEngine.getCursorNewWordList(dbname);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_CONTENT_FROM_ID:
				wordId = Integer.parseInt(uri.getPathSegments().get(3));
				Log.d(PROVIDER_TAG,"CONTENT_FROM_ID wordId = " + wordId);
				c = mDBEngine.getCursorContentFromId(wordId);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			case CODE_CONTENT_FROM_WORD:
				word = uri.getPathSegments().get(3);		
				Log.d(PROVIDER_TAG,"CONTENT_FROM_WORD word = " + word);
				c = mDBEngine.getCursorContentFromWord(word);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_INSERT:
				wordId = Integer.parseInt(uri.getPathSegments().get(3));
				word = uri.getPathSegments().get(4);
				content = uri.getPathSegments().get(5);
				Log.d(PROVIDER_TAG,"insert id= "+ wordId+" word= "+word+" content = " +content);
				c = mDBEngine.insertWord(wordId, word, content);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_UPDATE:
				wordId = Integer.parseInt(uri.getPathSegments().get(3));
				word = uri.getPathSegments().get(4);
				content = uri.getPathSegments().get(5);
				Log.d(PROVIDER_TAG,"update word= "+word+" content = " +content+" where id= "+ wordId);
				c = mDBEngine.updateWord(wordId, word, content);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_MAXID:		
				Log.d(PROVIDER_TAG,"MAXID ");
				c = mDBEngine.getMaxId();
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_DELETE:
				wordId = Integer.parseInt(uri.getPathSegments().get(3));
				Log.d(PROVIDER_TAG,"DELETE id = " + wordId);
				c = mDBEngine.deleteWord(wordId);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
			case CODE_GET_ID:
				word = uri.getPathSegments().get(3);
				Log.d(PROVIDER_TAG,"word = " + word);
				c = mDBEngine.getIdFromWord(word);
				c.setNotificationUri(getContext().getContentResolver(), uri);	
				return c;
	        default:
	            //throw new IllegalArgumentException("DictionaryProvider - Unsupported URI: " + uri);
	        	Log.e(PROVIDER_TAG,"DictionaryProvider - Unsupported URI: " + uri);
	        	return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
