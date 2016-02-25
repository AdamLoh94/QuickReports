package com.mercury.gabadam.quickreports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AdamLoh on 25/2/2016.
 */
public class ReportNew extends AppCompatActivity {

    AutoCompleteTextView actAddRepCustName;

    TextView tvAddRepTotal, tvAddRepEngName, tvAddRepDate;

    EditText etAddRepLabour, etAddRepMaterial, etAddRepTransport, etAddRepComments;

    RadioButton rbAddRepYes, rbAddRepNo, rbAddRepInstallation,
            rbAddRepRepair, rbAddRepMaintenance, rbAddRepTerminate;

    RadioGroup rgAddRepWarranty, rgAddRepService;

    Button btnAddRepSave;

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String nowDate = sdf.format(date);

    RestService restService;
    UserSessionManager session;

    private static List<Customer> custList;
    private static List<String> custNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_new);
        restService = new RestService();
        session = new UserSessionManager(getApplicationContext());

        actAddRepCustName = (AutoCompleteTextView) findViewById(R.id.actAddRepCustName);

        tvAddRepEngName = (TextView) findViewById(R.id.tvAddRepEngName);
        tvAddRepTotal = (TextView) findViewById(R.id.tvAddRepTotal);
        tvAddRepDate = (TextView) findViewById(R.id.tvAddRepDate);

        etAddRepLabour = (EditText) findViewById(R.id.etAddRepLabour);
        etAddRepMaterial = (EditText) findViewById(R.id.etAddRepMaterial);
        etAddRepTransport = (EditText) findViewById(R.id.etAddRepTransport);
        etAddRepComments = (EditText) findViewById(R.id.etAddRepComments);

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


        restService.getService().getCustomer(new Callback<List<Customer>>() {
            @Override
            public void success(List<Customer> customers, Response response) {
                for (Customer c : customers) {
                    custNames.add(c.Name);
                }
                custList = customers;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, custNames);
        actAddRepCustName.setAdapter(adapter);
        tvAddRepDate.setText(nowDate);
        tvAddRepTotal.setText("0");

        rgAddRepWarranty.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbAddRepYes){
                    etAddRepLabour.setText("0");
                    etAddRepLabour.setEnabled(false);
                    etAddRepLabour.setFocusableInTouchMode(false);
                    etAddRepLabour.clearFocus();

                    etAddRepMaterial.setText("0");
                    etAddRepMaterial.setEnabled(false);
                    etAddRepMaterial.setFocusableInTouchMode(false);
                    etAddRepMaterial.clearFocus();

                    etAddRepTransport.setText("0");
                    etAddRepTransport.setEnabled(false);
                    etAddRepTransport.setFocusableInTouchMode(false);
                    etAddRepTransport.clearFocus();
                }
                else{
                    etAddRepLabour.setEnabled(true);
                    etAddRepLabour.setFocusableInTouchMode(true);

                    etAddRepMaterial.setEnabled(true);
                    etAddRepMaterial.setFocusableInTouchMode(true);

                    etAddRepTransport.setEnabled(true);
                    etAddRepTransport.setFocusableInTouchMode(true);
                }
            }
        });
    }



    public static boolean isDouble(String strDouble){
        try{
            Double.parseDouble(strDouble);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static Double addTotal(String str1, String str2, String str3){
        double db1 = Double.parseDouble(str1);
        double db2 = Double.parseDouble(str2);
        double db3 = Double.parseDouble(str3);
        double dbTotal = db1 + db2 + db3;
        return dbTotal;
    }

    //Method to submit form
    public void onAddRepSave(View view) {
        //validations
        final String Name = actAddRepCustName.getText().toString();
        final String Labour = etAddRepLabour.getText().toString();
        final String Material = etAddRepMaterial.getText().toString();
        final String Transport = etAddRepTransport.getText().toString();

        //Customer name validation
        if (Name.length() == 0) {
            actAddRepCustName.requestFocus();
            actAddRepCustName.setError("FIELD CANNOT BE EMPTY!!");
            return; //exit out of submit form method
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



        Report report = new Report();
        RadioButton radioButton =
                (RadioButton) findViewById(rgAddRepService.getCheckedRadioButtonId());
        boolean checkExist = false;

        //check customer exist
        loop1:
        for (Customer c : custList) {
            if (c.Name.equals(actAddRepCustName.getText().toString())) {
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


        report.Date = String.valueOf(tvAddRepDate.getText());

        if (rbAddRepNo.isChecked()) {
            report.Warranty = false;
        } else if (rbAddRepYes.isChecked()) {
            report.Warranty = true;
        }

        report.ServiceNature = String.valueOf(radioButton.getText());
        report.LabourCharge = Double.valueOf(etAddRepLabour.getText().toString());
        report.TotalMaterial = Double.valueOf(etAddRepMaterial.getText().toString());
        report.Transport = Double.valueOf(etAddRepTransport.getText().toString());
        report.Total = addTotal(Labour, Material, Transport);
        tvAddRepTotal.setText(String.valueOf(addTotal(Labour, Material, Transport)));
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
