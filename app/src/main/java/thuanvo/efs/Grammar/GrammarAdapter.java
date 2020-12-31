package thuanvo.efs.Grammar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import thuanvo.efs.R;


/**
 * Created by tv on 3/18/2017.
 */

public class GrammarAdapter extends BaseAdapter {

    Context maincontext;
    int mainlayout;
    List<Grammar> mainmainadapter;

    public GrammarAdapter(Context context, int layout, List<Grammar> mainadapter){
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


        TextView txtvmain = (TextView) convertView.findViewById(R.id.txtvsach);
        TextView txtvgia = (TextView) convertView.findViewById(R.id.txtvgia);

        txtvmain.setText(mainmainadapter.get(position).ten);
        txtvgia.setText(""+mainmainadapter.get(position).gia);

        return convertView;
    }
}
