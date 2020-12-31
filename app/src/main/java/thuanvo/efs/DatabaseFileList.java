package thuanvo.efs;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class DatabaseFileList {
	static final private String FILELIST_TAG = "[Dictionary - DatabaseFileList]";
	public ArrayList<DatabaseFile> items;
	
	public DatabaseFileList(String dbPath)
	{
		items = new ArrayList<DatabaseFile>();
		getDatabaseFileList(dbPath);
	}

	private void getDatabaseFileList(String dbPath)
	{
		items.clear();
		File dataDirectory = new File(dbPath);

        File[] lstDirectory = dataDirectory.listFiles();
        Log.i(FILELIST_TAG,"Filelist path = " + dbPath);
        if (lstDirectory != null && lstDirectory.length > 0)
        {
	        for (File currentDirectory : lstDirectory)
	        {
	            DatabaseFile db = new DatabaseFile();
	        	String path = currentDirectory.getAbsolutePath() + "/" + currentDirectory.getName();
	        	Log.i(FILELIST_TAG,"Filelist path = " + path);
          		db.fileName = currentDirectory.getName();
          		db.path = currentDirectory.getPath();
          		Log.i(FILELIST_TAG,"fileName = " + db.fileName + " | path = " + db.path);
          		if(db.fileName.equals("anh_viet"))
          			db.dictionaryName="Từ điển Anh-Việt";
          		else 
          			db.dictionaryName="Từ điển Việt-Anh";	           
          		items.add(db);
	        }
	        Log.i(FILELIST_TAG,"Found " + items.size() + " dictionaries");
        }
        else
        {
        	Log.i(FILELIST_TAG,"Do not find any valid dictionary");
        }
	}
}
