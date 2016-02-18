package com.mercury.gabadam.quickreports;

/**
 * Created by user on 15/2/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
public class CustomAdapter extends ArrayAdapter<Engineer>{

    public CustomAdapter(Context context, int resource, List<Engineer> engineers) {
        super(context, resource, engineers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_engineer, parent, false);
        }

        Engineer engineer = getItem(position);

        if (engineer != null) {
            TextView tvEngineerId = (TextView) v.findViewById(R.id.engineer_Id);
            TextView tvEngineerName = (TextView) v.findViewById(R.id.engineer_name);
            tvEngineerId.setText(Integer.toString(engineer.Id));
            tvEngineerName.setText(engineer.Name);
        }

        return v;
    }
}
