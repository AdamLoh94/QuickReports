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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportViewAllActivity extends Activity {

    private ArrayList<Report> list;
    private String custName;
    private ListView lview;

    RestService restService;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_main);
        restService = new RestService();
        session = new UserSessionManager(getApplicationContext());

        if (session.checkLogin()) {
            finish();
        }
        refreshScreen();
    }

    private void refreshScreen() {
        restService.getService().getReport(new Callback<List<Report>>() {

                                               @Override
                                               public void success(List<Report> reports, Response response) {
                                                   lview = (ListView) findViewById(R.id.listview);
                                                   ReportCustomAdapter reportCustomAdapter = new ReportCustomAdapter(ReportViewAllActivity.this, R.layout.reports_listview_row, reports);
                                                   lview.setAdapter(reportCustomAdapter);
                                                   lview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                                                       @Override
                                                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                           Report item = (Report) lview.getItemAtPosition(position);
                                                           int reportId = item.Id;
                                                           Intent objIndent = new Intent(getApplicationContext(), ReportDetail.class);
                                                           objIndent.putExtra("reportId", reportId);
                                                           startActivity(objIndent);
                                                       }
                                                   });
                                               }

                                               @Override
                                               public void failure(RetrofitError error) {
                                                   Toast.makeText(ReportViewAllActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                               }
                                           }
        );
    }


}
