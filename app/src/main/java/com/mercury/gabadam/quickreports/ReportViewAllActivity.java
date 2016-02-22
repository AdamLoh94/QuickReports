package com.mercury.gabadam.quickreports;

/**
 * Created by AdamLoh on 21/2/2016.
 */

import static com.mercury.gabadam.quickreports.Constant.FIRST_COLUMN;
import static com.mercury.gabadam.quickreports.Constant.SECOND_COLUMN;
import static com.mercury.gabadam.quickreports.Constant.THIRD_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportViewAllActivity extends Activity {

    private ArrayList<HashMap> list;
    private String custName;

    RestService restService;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_main);

        session = new UserSessionManager(getApplicationContext());
        if (session.checkLogin()) {
            finish();
        }
        restService = new RestService();
        refreshScreen();
    }

    private void refreshScreen() {
        restService.getService().getReport(new Callback<List<Report>>() {

                                               @Override
                                               public void success(List<Report> reports, Response response) {
                                                   ListView lview = (ListView) findViewById(R.id.listview);
                                                   ReportCustomAdapter reportCustomAdapter = new ReportCustomAdapter(ReportViewAllActivity.this, R.layout.reports_listview_row, reports);
                                                   lview.setAdapter(reportCustomAdapter);
                                               }

                                               @Override
                                               public void failure(RetrofitError error) {
                                                   Toast.makeText(ReportViewAllActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                               }
                                           }
        );
    }

    private void populateList() {
        restService.getService().getReport(new Callback<List<Report>>() {
            @Override
            public void success(List<Report> reports, Response response) {
                list = new ArrayList<HashMap>();

                for (Report r : reports) {
                    if (r.EngineerId == session.getUserId()) {
                        HashMap temp = new HashMap();
                        temp.put(FIRST_COLUMN, r.Id);
                        temp.put(SECOND_COLUMN, customerName(r.CustomerId));
                        temp.put(THIRD_COLUMN, r.Date);
                        list.add(temp);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(ReportViewAllActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String customerName(int customerId) {
        restService.getService().getCustomerByID(customerId, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                custName = customer.Name;
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(ReportViewAllActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
        return custName;
    }


}
