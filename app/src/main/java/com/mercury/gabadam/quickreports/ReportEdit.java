package com.mercury.gabadam.quickreports;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CalendarView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AdamLoh on 24/2/2016.
 */
public class ReportEdit extends AppCompatActivity {

    TextView tvEditRepId, tvEditRepEngName, tvEditRepCustName;

    EditText etEditRepDate, etEditRepLabour, etEditRepMaterial, etEditRepTransport,
            etEditRepTotal, etEditRepComments;

    RadioButton rbEditRepYes, rbEditRepNo, rbEditRepInstallation,
            rbEditRepRepair, rbEditRepMaintenance, rbEditRepTerminate;

    RadioGroup rgEditRepWarranty, rgEditRepService;


    private int _Report_Id = 0;
    RestService restService;
    UserSessionManager session;

    int custId = 0;
    int repId = 0;
    int engId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_edit);
        restService = new RestService();
        session = new UserSessionManager(getApplicationContext());

        tvEditRepId = (TextView) findViewById(R.id.tvEditRepId);
        tvEditRepEngName = (TextView) findViewById(R.id.tvEditRepEngName);
        tvEditRepCustName = (TextView) findViewById(R.id.tvEditRepCustName);

        etEditRepDate = (EditText) findViewById(R.id.etEditRepDate);
        etEditRepLabour = (EditText) findViewById(R.id.etEditRepLabour);
        etEditRepMaterial = (EditText) findViewById(R.id.etEditRepMaterial);
        etEditRepTransport = (EditText) findViewById(R.id.etEditRepTransport);
        etEditRepTotal = (EditText) findViewById(R.id.etEditRepTotal);
        etEditRepComments = (EditText) findViewById(R.id.etEditRepComments);


        rbEditRepYes = (RadioButton) findViewById(R.id.rbEditRepYes);
        rbEditRepNo = (RadioButton) findViewById(R.id.rbEditRepNo);
        rbEditRepInstallation = (RadioButton) findViewById(R.id.rbEditRepInstallation);
        rbEditRepRepair = (RadioButton) findViewById(R.id.rbEditRepRepair);
        rbEditRepMaintenance = (RadioButton) findViewById(R.id.rbEditRepMaintenance);
        rbEditRepTerminate = (RadioButton) findViewById(R.id.rbEditRepTerminate);

        rgEditRepWarranty = (RadioGroup) findViewById(R.id.rgEditRepWarranty);
        rgEditRepService = (RadioGroup) findViewById(R.id.rgEditRepService);


        Intent intent = getIntent();
        _Report_Id = intent.getIntExtra("reportId", 0);
        restService.getService().getReportByID(_Report_Id, new Callback<Report>() {
            @Override
            public void success(Report report, Response response) {
                repId = report.Id;
                tvEditRepId.setText(String.valueOf(report.Id));
                
                restService.getService().getCustomerByID(report.CustomerId, new Callback<Customer>() {
                    @Override
                    public void success(Customer customer, Response response) {
                        custId = customer.CustomerId;
                        tvEditRepCustName.setText(String.valueOf(customer.Name));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                etEditRepDate.setText(String.valueOf(report.Date));

                if (report.Warranty == true) {
                    rgEditRepWarranty.check(R.id.rbEditRepYes);
                } else {
                    rgEditRepWarranty.check(R.id.rbEditRepNo);
                }

                if ((report.ServiceNature).equals("Installation")) {
                    rgEditRepService.check(R.id.rbEditRepInstallation);
                } else if ((report.ServiceNature).equals("Repair")) {
                    rgEditRepService.check(R.id.rbEditRepRepair);
                } else if ((report.ServiceNature).equals("Maintenance")) {
                    rgEditRepService.check(R.id.rbEditRepMaintenance);
                } else if ((report.ServiceNature).equals("Terminate")) {
                    rgEditRepService.check(R.id.rbEditRepTerminate);
                }

                etEditRepLabour.setText(String.valueOf(report.LabourCharge));
                etEditRepMaterial.setText(String.valueOf(report.TotalMaterial));
                etEditRepTransport.setText(String.valueOf(report.Transport));
                etEditRepTotal.setText(String.valueOf(report.Total));

                etEditRepComments.setText(report.Comments);
                restService.getService().getEngineerByID(report.EngineerId, new Callback<Engineer>() {
                    @Override
                    public void success(Engineer engineer, Response response) {
                        engId = engineer.Id;
                        tvEditRepEngName.setText(engineer.Name);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public void onEditRepSave(View view) {
        Report report = new Report();
        RadioButton radioButton =
                (RadioButton) findViewById(rgEditRepService.getCheckedRadioButtonId());


        report.Id = repId;
        report.CustomerId = custId;
        report.Date = String.valueOf(etEditRepDate.getText());

        if(rbEditRepNo.isChecked()){
            report.Warranty = false;
        }else if(rbEditRepYes.isChecked()){
            report.Warranty = true;
        }

        report.ServiceNature = String.valueOf(radioButton.getText());
        report.LabourCharge = Double.valueOf(etEditRepLabour.getText().toString());
        report.TotalMaterial = Double.valueOf(etEditRepMaterial.getText().toString());
        report.Transport = Double.valueOf(etEditRepTransport.getText().toString());
        report.Total = Double.valueOf(etEditRepTotal.getText().toString());
        report.Comments = String.valueOf(etEditRepComments.getText());
        report.EngineerId = engId;

        restService.getService().updateReportById(repId, report, new Callback<Report>() {
            @Override
            public void success(Report report, Response response) {
                Toast.makeText(ReportEdit.this, " Report Updated!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ReportEdit.this, "Error in report!", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void onEditRepBack(View view) {
        finish();
    }
}
