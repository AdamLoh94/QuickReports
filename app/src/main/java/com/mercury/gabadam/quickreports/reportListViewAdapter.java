package com.mercury.gabadam.quickreports;

/**
 * Created by AdamLoh on 21/2/2016.
 */

import static com.mercury.gabadam.quickreports.Constant.FIRST_COLUMN;
import static com.mercury.gabadam.quickreports.Constant.SECOND_COLUMN;
import static com.mercury.gabadam.quickreports.Constant.THIRD_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class reportListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap> list;
    Activity activity;

    public reportListViewAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.reports_listview_row, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.firstText);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.secondText);
            holder.txtThird = (TextView) convertView.findViewById(R.id.thirdText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.txtFirst.setText((String)map.get(FIRST_COLUMN));
        holder.txtSecond.setText((String)map.get(SECOND_COLUMN));
        holder.txtThird.setText((String)map.get(THIRD_COLUMN));

        return convertView;
    }
}
