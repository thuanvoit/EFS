package thuanvo.efs.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thuanvo.efs.R;


/**
 * Created by tv on 3/18/2017.
 */

public class MainAdapter extends BaseAdapter {

    Context maincontext;
    int mainlayout;
    List<Main> mainmainadapter;

    public MainAdapter(Context context, int layout, List<Main> mainadapter){
        maincontext = context;
        mainlayout = layout;
        mainmainadapter = mainadapter;

    }

    @Override
    public int getCount() {
        return mainmainadapter.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) maincontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(mainlayout,null);


        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView txtvmain = (TextView) convertView.findViewById(R.id.txtvsach);
        TextView txtvgia = (TextView) convertView.findViewById(R.id.txtvgia);

        img.setImageResource(mainmainadapter.get(position).hinh);
        txtvmain.setText(mainmainadapter.get(position).ten);
        txtvgia.setText(""+mainmainadapter.get(position).gia);

        return convertView;
    }
}
