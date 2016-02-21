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
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportCustomAdapter extends ArrayAdapter<Report> {

    private String custName;
    RestService restService = new RestService();

    public ReportCustomAdapter(Context context, int resource, List<Report> report) {
        super(context, resource, report);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.reports_listview_row, parent, false);
        }

        Report report = getItem(position);

        if (report != null) {
            TextView tvReportId = (TextView) v.findViewById(R.id.firstText);
            TextView tvCustomerId = (TextView) v.findViewById(R.id.secondText);
            TextView tvDateId = (TextView) v.findViewById(R.id.thirdText);
            tvReportId.setText( Integer.toString(report.Id));
            tvCustomerId.setText( customerName(report.CustomerId));
            tvDateId.setText(report.Date);
        }

        return v;
    }

    private String customerName(int customerId) {
        restService.getService().getCustomerByID(customerId, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                custName = customer.Name;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return custName;
    }
}
