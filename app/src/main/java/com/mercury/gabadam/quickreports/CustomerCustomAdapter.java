package com.mercury.gabadam.quickreports;

/**
 * Created by user on 23/2/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomerCustomAdapter extends ArrayAdapter<Customer>{

    public CustomerCustomAdapter(Context context, int resource, List<Customer> customers) {
        super(context, resource, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_customer, parent, false);
        }

        Customer customer = getItem(position);

        if (customer != null) {
            TextView tvCustomerId = (TextView) v.findViewById(R.id.customer_Id);
            TextView tvCustomerName = (TextView) v.findViewById(R.id.customer_name);
            tvCustomerId.setText(String.valueOf(customer.CustomerId));
            tvCustomerName.setText(customer.Name);
        }

        return v;
    }
}
