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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String strDouble){
        try{
            Double.parseDouble(strDouble);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean checkTotal(String str1, String str2, String str3, String strTotal){
        double db1 = Double.parseDouble(str1);
        double db2 = Double.parseDouble(str2);
        double db3 = Double.parseDouble(str3);
        double dbTotal =Double.parseDouble(strTotal);
        if((db1 + db2 + db3) == dbTotal){
            return true;
        }
        else{
            return false;
        }
    }

    //Method to submit form
    public void onAddRepSave(View view) {
        //validations
        final String Name = etAddRepCustName.getText().toString();
        final String Date = etAddRepDate.getText().toString();
        final String Labour = etAddRepLabour.getText().toString();
        final String Material = etAddRepMaterial.getText().toString();
        final String Transport = etAddRepTransport.getText().toString();
        final String Total = etAddRepTotal.getText().toString();

        //Customer name validation
        if (Name.length() == 0) {
            etAddRepCustName.requestFocus();
            etAddRepCustName.setError("FIELD CANNOT BE EMPTY!!");
            return; //exit out of submit form method
        } else if (Date.length() == 0) {
            etAddRepDate.requestFocus();
            etAddRepDate.setError("FIELD CANNOT BE EMPTY!!");
            return;
        } else if(!isValidDate(Date)){
            etAddRepDate.requestFocus();
            etAddRepDate.setError("Date must be in the dd/MM/yyyy format");
            return;
        } else if (rgAddRepWarranty.getCheckedRadioButtonId() == -1) {
            rbAddRepYes.setError("");
            rbAddRepNo.setError("");
            return;
        }else if(rgAddRepService.getCheckedRadioButtonId() == -1){
            rbAddRepInstallation.setError("");
            rbAddRepMaintenance.setError("");
            rbAddRepRepair.setError("");
            rbAddRepTerminate.setError("");
            return;
        }
        //Labour
        else if(Labour.length() == 0){
            etAddRepLabour.requestFocus();
            etAddRepLabour.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Labour)){
            etAddRepLabour.requestFocus();
            etAddRepLabour.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }
        //Material
        else if(Material.length() == 0){
            etAddRepMaterial.requestFocus();
            etAddRepMaterial.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Material)){
            etAddRepMaterial.requestFocus();
            etAddRepMaterial.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }
        //Transport
        else if(Transport.length() == 0){
            etAddRepTransport.requestFocus();
            etAddRepTransport.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Transport)){
            etAddRepTransport.requestFocus();
            etAddRepTransport.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }
        //Total
        else if(Total.length() == 0){
            etAddRepTotal.requestFocus();
            etAddRepTotal.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Total)){
            etAddRepTotal.requestFocus();
            etAddRepTotal.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }else if(!checkTotal(Labour, Material, Transport, Total)){
            etAddRepTotal.requestFocus();
            etAddRepTotal.setError("Please re-check total amount!");
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

        if (checkExist == false) {
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
