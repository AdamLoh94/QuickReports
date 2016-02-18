package com.mercury.gabadam.quickreports;

/**
 * Created by user on 16/2/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReportCustomAdapter extends ArrayAdapter<Report> {

    public ReportCustomAdapter(Context context, int resource, List<Report> report) {
        super(context, resource, report);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_report, parent, false);
        }

        Report report = getItem(position);

        if (report != null) {
            TextView tvReportId = (TextView) v.findViewById(R.id.report_Id);
            TextView tvEngineerId = (TextView) v.findViewById(R.id.engineer_Id);
            tvReportId.setText( Integer.toString(report.Id));
            tvEngineerId.setText( Integer.toString(report.EngineerId));
        }

        return v;
    }
}
