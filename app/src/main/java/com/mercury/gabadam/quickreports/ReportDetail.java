package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportDetail extends AppCompatActivity implements android.view.View.OnClickListener {

    Button btnSave, btnClose;
    EditText editTextReportID;
    EditText editTextCustID;
    EditText editTextDate;
    EditText editTextWarranty;
    EditText editTextSN;
    EditText editTextLC;
    EditText editTextTM;
    EditText editTextTransport;
    EditText editTextTotal;
    EditText editTextComments;
    EditText editTextEngID;
    private int _Report_Id = 0;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        restService = new RestService();

        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);

        editTextReportID = (EditText) findViewById(R.id.editTextReportID);
        editTextCustID = (EditText) findViewById(R.id.editTextCustID);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextWarranty = (EditText) findViewById(R.id.editTextWarranty);
        editTextSN = (EditText) findViewById(R.id.editTextSN);
        editTextLC = (EditText) findViewById(R.id.editTextLC);
        editTextTM = (EditText) findViewById(R.id.editTextTM);
        editTextTransport = (EditText) findViewById(R.id.editTextTransport);
        editTextTotal = (EditText) findViewById(R.id.editTextTotal);
        editTextComments = (EditText) findViewById(R.id.editTextComments);
        editTextEngID = (EditText) findViewById(R.id.editTextEngID);

        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        _Report_Id = 0;
        Intent intent = getIntent();
        _Report_Id = intent.getIntExtra("report_Id", 0);
        if (_Report_Id > 0) {
            restService.getService().getReportByID(_Report_Id, new Callback<Report>() {
                @Override
                public void success(Report report, Response response) {

                    editTextReportID.setText(String.valueOf(report.Id));
                    editTextCustID.setText(String.valueOf(report.CustomerId));
                    editTextDate.setText(report.Date);
                    editTextWarranty.setText(String.valueOf(report.Warranty));
                    editTextSN.setText(report.ServiceNature);
                    editTextLC.setText(String.valueOf(report.LabourCharge));
                    editTextTM.setText(String.valueOf(report.TotalMaterial));
                    editTextTransport.setText(String.valueOf(report.Transport));
                    editTextTotal.setText(String.valueOf(report.Total));
                    editTextComments.setText(report.Comments);
                    editTextEngID.setText(String.valueOf(report.EngineerId));
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(ReportDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
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

    @Override
    public void onClick(View v)
    {
        if (v == findViewById(R.id.btnClose))
        {
            finish();
        }
        else if (findViewById(R.id.btnSave) == v)
        {

            Report report = new Report();
            Integer status = 0;
            report.Id = Integer.parseInt(editTextReportID.getText().toString());
            report.CustomerId = Integer.parseInt(editTextCustID.getText().toString());
            report.Date = editTextDate.getText().toString();
            report.Warranty = Boolean.parseBoolean(editTextWarranty.getText().toString());
            report.ServiceNature = editTextSN.getText().toString();
            report.LabourCharge = Double.parseDouble(editTextLC.getText().toString());
            report.TotalMaterial = Double.parseDouble(editTextTM.getText().toString());
            report.Transport = Double.parseDouble(editTextTransport.getText().toString());
            report.Total = Double.parseDouble(editTextTotal.getText().toString());
            report.Comments = editTextComments.getText().toString();
            report.EngineerId = Integer.parseInt(editTextEngID.getText().toString());
            report.Id = _Report_Id;

            if (_Report_Id == 0)
            {
                restService.getService().addReport(report, new Callback<Report>()
                {
                    @Override
                    public void success(Report report, Response response)
                    {
                        Toast.makeText(ReportDetail.this, "New Report Inserted.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        Toast.makeText(ReportDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }
            else
            {
                restService.getService().updateReportById(_Report_Id, report, new Callback<Report>()
                {
                    @Override
                    public void success(Report report, Response response)
                    {
                        Toast.makeText(ReportDetail.this, "Report Record updated.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        Toast.makeText(ReportDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
