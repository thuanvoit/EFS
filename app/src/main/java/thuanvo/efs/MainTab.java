package thuanvo.efs;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainTab extends TabActivity {
	
	static final private String tag="CopyFileAssetsToSDCardActivity";
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab);
        createDirectory();
		CopyAssets("anh_viet","anh_viet");
		CopyAssets("viet_anh","viet_anh");
        
		TabHost tabHost = getTabHost();
 
        TabSpec tra_tu = tabHost.newTabSpec("Tra từ");
        tra_tu.setIndicator("Tra từ", getResources().getDrawable(R.drawable.search));
        Intent tra_tuIntent = new Intent(this, Dictionary.class);
        tra_tu.setContent(tra_tuIntent);

        TabSpec history = tabHost.newTabSpec("History");
        history.setIndicator("History", getResources().getDrawable(R.drawable.history));
        Intent historyIntent = new Intent(this, HistoryView.class);
        historyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        history.setContent(historyIntent);
        
        TabSpec favourite = tabHost.newTabSpec("Favourite");
        favourite.setIndicator("Favourite", getResources().getDrawable(R.drawable.favourite));
        Intent favouriteIntent = new Intent(this, FavouriteView.class);
        favouriteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        favourite.setContent(favouriteIntent);
 
        tabHost.addTab(tra_tu);
        tabHost.addTab(history); 
        tabHost.addTab(favourite); 
    }
    private void createDirectory()
	{
		  File direct = new File(Environment.getExternalStorageDirectory() + "/Dict_Data");
		  File direct1 = new File(Environment.getExternalStorageDirectory() + "/Dict_Data/anh_viet");
		  File direct2 = new File(Environment.getExternalStorageDirectory() + "/Dict_Data/viet_anh");
		  Log.i(tag, Environment.getExternalStorageDirectory() + "/Dict_Data");
		   if(!direct.exists())
		   {
		        if(direct.mkdir()) 
		        	 Log.i(tag,"Thư mục Dict_Data đã tạo");
		   }
		   else
			   Log.i(tag,"Thư mục Dict_Data đã tồn tại");
		   if(!direct1.exists())
		        if(direct1.mkdir()) 
		        	 Log.i(tag,"Thư mục anh_viet đã tạo");
		   if(!direct2.exists())
		        if(direct2.mkdir()) 
		        	 Log.i(tag,"Thư mục viet_anh đã tạo");
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    private void CopyAssets(String fromFile, String toFile) {
       
    	AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list(fromFile);
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        for(String filename : files) {
         Log.i("File=",filename );
            InputStream in = null;
            OutputStream out = null;
            try 
            {                         
              File direct = new File(Environment.getExternalStorageDirectory().toString() +"/" +"Dict_Data/"+toFile+"/" + filename);
              if(!direct.exists())
              {
            	  in = assetManager.open(fromFile+"/"+filename);  
            	  out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/" +"Dict_Data/"+toFile+"/" + filename);
            	  copyFile(in, out);
            	  Log.e("tag","Copy");
            	  in.close();
                  in = null;
                  out.flush();
                  out.close();
                  out = null;
              }
              else
            	  Log.e("tag","exist");
            } catch(Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
    }
}
