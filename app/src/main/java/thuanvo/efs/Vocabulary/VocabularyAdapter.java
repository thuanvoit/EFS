package thuanvo.efs.Vocabulary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thuanvo.efs.R;

import static thuanvo.efs.R.layout.custom_layout_voc;


/**
 * Created by anhth on 4/21/2017.
 */

public class VocabularyAdapter extends BaseAdapter {

    Context voccontext;
    int voclayout;
    List<Vocabulary> vocadapter;

    public VocabularyAdapter(Context voccontext, int voclayout, List<Vocabulary> vocadapter) {
        this.voccontext = voccontext;
        this.voclayout = voclayout;
        this.vocadapter = vocadapter;
    }

    @Override
    public int getCount() {
        return vocadapter.size();
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

        LayoutInflater inflater = (LayoutInflater) voccontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(custom_layout_voc,null);


        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView txtv = (TextView) convertView.findViewById(R.id.txtv);

        img.setImageResource(vocadapter.get(position).hinh);
        txtv.setText(vocadapter.get(position).title);

        return convertView;

    }
}