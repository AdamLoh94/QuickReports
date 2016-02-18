package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportMainActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    ListView listViewReport;
    Button btnGetAllReport,btnAddReport;
    RestService restService;
    TextView report_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);
        restService = new RestService();
        btnGetAllReport = (Button) findViewById(R.id.btnGetAllReport);
        btnGetAllReport.setOnClickListener(this);

        btnAddReport= (Button) findViewById(R.id.btnAddReport);
        btnAddReport.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.report_action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //This function will call when the screen is activate
    @Override
    public void onResume() {

        super.onResume();
        refreshScreen();
    }

    @Override
    public void onClick(View v) {
        if (v== findViewById(R.id.btnAddReport)){

            Intent intent = new Intent(this,ReportDetail.class);
            intent.putExtra("report_Id",0);
            startActivity(intent);

        }
        else
        {
            refreshScreen();
        }
    }

    private void refreshScreen()
    {

        //Call to server to grab list of report records. this is a asyn
        restService.getService().getReport(new Callback<List<Report>>()
        {
            @Override
            public void success(List<Report> reports, Response response)
            {
                ListView lv = (ListView) findViewById(R.id.listViewReport);

                ReportCustomAdapter reportCustomAdapter = new ReportCustomAdapter(ReportMainActivity.this, R.layout.view_report, reports);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        report_Id = (TextView) view.findViewById(R.id.report_Id);
                        String reportId = report_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(), ReportDetail.class);
                        objIndent.putExtra("report_Id", Integer.parseInt(reportId));
                        startActivity(objIndent);
                    }
                });
                lv.setAdapter(reportCustomAdapter);
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(ReportMainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
