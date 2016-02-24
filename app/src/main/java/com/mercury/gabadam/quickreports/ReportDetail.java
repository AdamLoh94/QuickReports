package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportDetail extends AppCompatActivity {

    TextView tvRepId, tvRepCustName, tvRepDate, tvRepWarranty, tvRepService,
            tvRepLabour, tvRepMaterial, tvRepTransport, tvRepTotal, tvRepComments, tvRepEngName;
    Button btnEdit, btnBack;
    private int _Report_Id;
    RestService restService;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        restService = new RestService();
        sessionManager = new UserSessionManager(getApplicationContext());

        btnEdit = (Button) findViewById(R.id.btnRepEdit);
        btnBack = (Button) findViewById(R.id.btnRepBack);

        tvRepId = (TextView) findViewById(R.id.tvRepId);
        tvRepCustName = (TextView) findViewById(R.id.tvRepCustName);
        tvRepDate = (TextView) findViewById(R.id.tvRepDate);
        tvRepWarranty = (TextView) findViewById(R.id.tvRepWarranty);
        tvRepService = (TextView) findViewById(R.id.tvRepService);
        tvRepLabour = (TextView) findViewById(R.id.tvRepLabour);
        tvRepMaterial = (TextView) findViewById(R.id.tvRepMaterial);
        tvRepTransport = (TextView) findViewById(R.id.tvRepTransport);
        tvRepTotal = (TextView) findViewById(R.id.tvRepTotal);
        tvRepComments = (TextView) findViewById(R.id.tvRepComments);
        tvRepEngName = (TextView) findViewById(R.id.tvRepEngName);


        _Report_Id = 0;
        Intent intent = getIntent();
        _Report_Id = intent.getIntExtra("reportId", 0);
        restService.getService().getReportByID(_Report_Id, new Callback<Report>() {
            @Override
            public void success(Report report, Response response) {

                tvRepId.setText(String.valueOf(report.Id));
                restService.getService().getCustomerByID(report.CustomerId, new Callback<Customer>() {
                    @Override
                    public void success(Customer customer, Response response) {
                        tvRepCustName.setText(String.valueOf(customer.Name));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                tvRepDate.setText(report.Date);
                tvRepWarranty.setText(String.valueOf(report.Warranty));
                tvRepService.setText(report.ServiceNature);
                tvRepLabour.setText(String.valueOf(report.LabourCharge));
                tvRepMaterial.setText(String.valueOf(report.TotalMaterial));
                tvRepTransport.setText(String.valueOf(report.Transport));
                tvRepTotal.setText(String.valueOf(report.Total));
                tvRepComments.setText(report.Comments);
                restService.getService().getEngineerByID(report.EngineerId, new Callback<Engineer>() {
                    @Override
                    public void success(Engineer engineer, Response response) {
                        tvRepEngName.setText(String.valueOf(engineer.Name));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ReportDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_detail, menu);
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

    public void backViewReport(View view) {
        finish();
    }


    public void onEditRepClick(View view) {
        int repId = Integer.parseInt(tvRepId.getText().toString());
        Intent objIndent = new Intent(getApplicationContext(), ReportEdit.class);
        objIndent.putExtra("reportId", repId);
        startActivity(objIndent);
    }
}
