package thuanvo.efs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

public class DictionaryEngine {
	static final private String SQL_TAG = "[Dictionary - DictionaryEngine]";
	
	private SQLiteDatabase mDB = null;
	private String mDBName;
	private String mDBPath;
	public ArrayList<String> lstCurrentWord = null;
	public ArrayList<String> lstCurrentContent = null;

	public DictionaryEngine()
	{
		lstCurrentContent = new ArrayList<String>();
    	lstCurrentWord = new ArrayList<String>();
	}
	
	public DictionaryEngine(String basePath, String dbName, String dbExtension)
	{
		lstCurrentContent = new ArrayList<String>();
    	lstCurrentWord = new ArrayList<String>();
		this.setDatabaseFile(basePath, dbName, dbExtension);
	}
	
	public boolean setDatabaseFile(String basePath, String dbName, String dbExtension)
	{
		if (mDB != null)
		{
			if (mDB.isOpen() == true) // Database is already opened
			{
				if (basePath.equals(mDBPath) && dbName.equals(mDBName)) // the opened database has the same name and path -> do nothing
				{
					Log.i(SQL_TAG, "Database is already opened!");
					return true;
				}
				else
				{
					mDB.close();
				}
			}
		}
		String fullDbPath="";
		try
		{
			fullDbPath = basePath + dbName + "/" + dbName + dbExtension;
			mDB = SQLiteDatabase.openDatabase(fullDbPath, null, SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}
		catch (SQLiteException ex)
		{
			ex.printStackTrace();
			Log.i(SQL_TAG, "There is no valid dictionary database " + dbName +" at path " + basePath);
			return false; 
		}
		if (mDB == null)
		{
			return false;
		}
		this.mDBName = dbName;
		this.mDBPath = basePath;
		Log.i(SQL_TAG,"Database " + dbName + " is opened!");
		return true;
	}
	
	public Cursor getCursorWordList(String word)
	{
		String query;
		Cursor result;
		if (word.equals("") || word == null)
		{
			return null;
		}
		else
		{
		    query = "SELECT id,content,word FROM " + mDBName + " WHERE  word >= '"+word+"' LIMIT 0,30";
		}
		Log.i(SQL_TAG, "query = " + query);
		result = mDB.rawQuery(query,null);
	   	return result;		
	}
	public Cursor getCursorContentFromId(int wordId)
	{
		String query;
		if (wordId <= 0)
		{
			return null;
		}
		else
		{
			query = "SELECT id,content,word FROM " + mDBName + " WHERE Id = " + wordId ;
		}
		Log.i(SQL_TAG, "query = " + query);
	   	Cursor result = mDB.rawQuery(query,null);
	   	
	   	return result;		
	}

	public Cursor getCursorContentFromWord(String word)
	{
		String query;
		if (word == null || word.equals(""))
		{
			return null;
		}
		else
		{
			query = "SELECT id,content,word FROM " + mDBName + " WHERE word = '" + word + "' LIMIT 0,1";
		}
		Log.i(SQL_TAG, "query = " + query);
		
	   	Cursor result = mDB.rawQuery(query,null);
	   	
	   	return result;		
	}
	public Cursor insertWord(int id, String word, String content)
	{
		String query;
		if (word.equals("") || word == null)
		{
			return null;
		}
		else
		{
			query = "INSERT INTO " + mDBName + " (id,word,content) VALUES(" + id + ",'" + word + "','" + content + "')";
		}
		Log.i(SQL_TAG, "query = " + query);
	   	Cursor result = mDB.rawQuery(query,null);
	   	
	   	return result;		
	}
	public Cursor getMaxId()
	{
		String query;
		Cursor result;
		
		query = "SELECT MAX(id) as maxid FROM " + mDBName;

		Log.i(SQL_TAG, "query = " + query);
		result = mDB.rawQuery(query,null);
	   	return result;		
	}
	public Cursor getCursorNewWordList(String dbname)
	{
		String query;
		Cursor result;
		if (dbname.equals("anh_viet"))
		{
			query = "SELECT id,content,word FROM " + mDBName + " WHERE  id > 387517";
		}
		else
		{
		    query = "SELECT id,content,word FROM " + mDBName + " WHERE  id > 390163";
		}
		Log.i(SQL_TAG, "query = " + query);
		result = mDB.rawQuery(query,null);
	   	return result;		
	}
	public Cursor deleteWord(int id)
	{
		String query;
		if (id==-1)
		{
			return null;
		}
		else
		{
			query = "Delete From " + mDBName + " where id="+id;
		}
		Log.i(SQL_TAG, "query = " + query);
	   	Cursor result = mDB.rawQuery(query,null);
	   	
	   	return result;		
	}
	public Cursor getIdFromWord(String word)
	{
		String query;
		if (word == null || word.equals(""))
		{
			return null;
		}
		else
		{
			query = "SELECT id FROM " + mDBName + " WHERE word = '" + word + "' LIMIT 0,1";
		}
		Log.i(SQL_TAG, "query = " + query);
		
	   	Cursor result = mDB.rawQuery(query,null);
	   	
	   	return result;		
	}
	public Cursor updateWord(int id, String word, String content)
	{
		String query;
		if (word.equals("") || word == null)
		{
			return null;
		}
		else
		{
			query = "UPDATE " + mDBName + " SET word='" + word + "',content='" + content + "' where id="+id;
		}
		Log.i(SQL_TAG, "query = " + query);
	   	Cursor result = mDB.rawQuery(query,null);
	   	
	   	return result;		
	}
	public void closeDatabase()
	{
		mDB.close();
	}
	
	public boolean isOpen()
	{
		return mDB.isOpen();
	}
	
	public boolean isReadOnly()
	{
		return mDB.isReadOnly();
	}

}
