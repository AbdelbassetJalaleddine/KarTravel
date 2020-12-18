package jalaleddine.abdelbasset.kartravel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class ListAdapter extends ArrayAdapter<ContactInformation> {

    private int resourceLayout;
    private Context mContext;

    public ListAdapter(Context context, int resource, List<ContactInformation> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        ContactInformation p = getItem(position);

       if (p != null) {
           LinearLayout background = v.findViewById(R.id.background);
            ImageView IV = (ImageView) v.findViewById(R.id.flag);
            TextView tt2 = (TextView) v.findViewById(R.id.txt);
            TextView tt3 = (TextView) v.findViewById(R.id.cur);

            if (IV != null && p.getGender() != null) {
                if(p.getGender().toLowerCase().equals("travel agent")){
                  IV.setImageResource(R.drawable.agent);
                }
                else if(p.getGender().toLowerCase().equals("corporate travel consultant")) {
                    IV.setImageResource(R.drawable.consultant);
                }
                else if (p.getGender().toLowerCase().equals("office manager")){
                    IV.setImageResource(R.drawable.manager);
                }
                else if(p.getGender().toLowerCase().equals("greece")){
                    IV.setImageResource(R.drawable.greece);
                }
                else if(p.getGender().toLowerCase().equals("germany")){
                    IV.setImageResource(R.drawable.germany);
                }
                //Glide.with(mContext).load("http:" + p.getName()).into(IV);
            }

            if (tt2 != null) {
                tt2.setText(p.getName());
            }
           if (tt3 != null) {
               tt3.setText(p.getLastSeen());
           }

           // Log.d("ListAdapter " ,"tt2 " + tt2.getText().toString() + " tt3" + tt3.getText().toString());
        }

        return v;
    }}

