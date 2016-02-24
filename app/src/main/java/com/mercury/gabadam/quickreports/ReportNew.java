package com.mercury.gabadam.quickreports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AdamLoh on 25/2/2016.
 */
public class ReportNew extends AppCompatActivity {

    TextView tvAddRepEngName;

    EditText etAddRepDate, etAddRepLabour, etAddRepMaterial, etAddRepTransport,
            etAddRepTotal, etAddRepComments, etAddRepCustName;

    RadioButton rbAddRepYes, rbAddRepNo, rbAddRepInstallation,
            rbAddRepRepair, rbAddRepMaintenance, rbAddRepTerminate;

    RadioGroup rgAddRepWarranty, rgAddRepService;

    Button btnAddRepSave;

    RestService restService;
    UserSessionManager session;

    List<Customer> custList;
    ArrayList<String> custName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_new);
        restService = new RestService();
        session = new UserSessionManager(getApplicationContext());

        tvAddRepEngName = (TextView) findViewById(R.id.tvAddRepEngName);

        etAddRepDate = (EditText) findViewById(R.id.etAddRepDate);
        etAddRepLabour = (EditText) findViewById(R.id.etAddRepLabour);
        etAddRepMaterial = (EditText) findViewById(R.id.etAddRepMaterial);
        etAddRepTransport = (EditText) findViewById(R.id.etAddRepTransport);
        etAddRepTotal = (EditText) findViewById(R.id.etAddRepTotal);
        etAddRepComments = (EditText) findViewById(R.id.etAddRepComments);
        etAddRepCustName = (EditText) findViewById(R.id.etAddRepCustName);

        rbAddRepYes = (RadioButton) findViewById(R.id.rbAddRepYes);
        rbAddRepNo = (RadioButton) findViewById(R.id.rbAddRepNo);
        rbAddRepInstallation = (RadioButton) findViewById(R.id.rbAddRepInstallation);
        rbAddRepRepair = (RadioButton) findViewById(R.id.rbAddRepRepair);
        rbAddRepMaintenance = (RadioButton) findViewById(R.id.rbAddRepMaintenance);
        rbAddRepTerminate = (RadioButton) findViewById(R.id.rbAddRepTerminate);

        rgAddRepWarranty = (RadioGroup) findViewById(R.id.rgAddRepWarranty);
        rgAddRepService = (RadioGroup) findViewById(R.id.rgAddRepService);

        btnAddRepSave = (Button) findViewById(R.id.btnAddRepSave);

        restService.getService().getEngineerByID(session.getUserId(), new Callback<Engineer>() {
            @Override
            public void success(Engineer engineer, Response response) {
                tvAddRepEngName.setText(String.valueOf(engineer.Name));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        custName = new ArrayList<>();
        restService.getService().getCustomer(new Callback<List<Customer>>() {
            @Override
            public void success(List<Customer> customers, Response response) {
                custList = customers;
                for (Customer c : customers) {
                    custName.add(c.Name);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }


    //Method to submit form
    public void onAddRepSave(View view) {
        //validations
        final String Name = etAddRepCustName.getText().toString();

        //Customer name validation
        if(Name.length()== 0){
            etAddRepCustName.requestFocus();
            etAddRepCustName.setError("FIELD CANNOT BE EMPTY!!");
            return; //exit out of submit form method
        }else if(!Name.matches("[a-zA-Z]+")){
            etAddRepCustName.requestFocus();
            etAddRepCustName.setError("ENTER ONLY ALPHABETICAL CHARACTER!");
            return;
        }


        Report report = new Report();
        RadioButton radioButton =
                (RadioButton) findViewById(rgAddRepService.getCheckedRadioButtonId());
        boolean checkExist = false;

        //check customer exist
        loop1:
        for (Customer c : custList) {
            if (c.Name.equals(etAddRepCustName.getText().toString())) {
                report.CustomerId = c.CustomerId;
                Toast.makeText(ReportNew.this, "Customer successfully added!",
                        Toast.LENGTH_LONG).show();
                checkExist = true;
                break loop1;
            }
        }

        if(checkExist == false){
            Toast.makeText(ReportNew.this, "Customer does not exist!",
                    Toast.LENGTH_LONG).show();
            return;
        }



        report.Date = String.valueOf(etAddRepDate.getText());

        if (rbAddRepNo.isChecked()) {
            report.Warranty = false;
        } else if (rbAddRepYes.isChecked()) {
            report.Warranty = true;
        }

        report.ServiceNature = String.valueOf(radioButton.getText());
        report.LabourCharge = Double.valueOf(etAddRepLabour.getText().toString());
        report.TotalMaterial = Double.valueOf(etAddRepMaterial.getText().toString());
        report.Transport = Double.valueOf(etAddRepTransport.getText().toString());
        report.Total = Double.valueOf(etAddRepTotal.getText().toString());
        report.Comments = String.valueOf(etAddRepComments.getText());
        report.EngineerId = session.getUserId();

        restService.getService().addReport(report, new Callback<Report>() {
            @Override
            public void success(Report report, Response response) {
                Toast.makeText(ReportNew.this, "New Report Added!", Toast.LENGTH_LONG).show();
                btnAddRepSave.setEnabled(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ReportNew.this, "Error Adding Report!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onAddRepBack(View view) {
        finish();
    }
}
