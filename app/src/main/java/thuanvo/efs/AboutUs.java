package thuanvo.efs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutUs extends Activity {
	private TextView tvwInfo;
	private Button btnBack;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.about);
	        openAbout();
	 }
	private void openAbout()
	{
		
	    tvwInfo = (TextView) findViewById(R.id.tvwInfo);
	    String string="Đây là từ điển Anh - Việt Việt - Anh thuộc đề tài khóa luận tốt nghiệp 2013 của nhóm sinh viên lớp đại học Tin học 09 - Trường đại học Tiền Giang."+"\n"
	    +"Chương trình có tham khảo một số nội dung từ dự án mã nguồn mở từ điển Andict."+"\n"+
	    "Cơ sở dữ liệu của từ điển khoảng 800 nghìn từ cũng được tải từ dự án này.";
		tvwInfo.setText(string);
		btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{       		
				finish();
        	}
        });
		
	}
}
