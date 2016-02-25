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
import android.widget.AutoCompleteTextView;
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
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.io.Serializable;
import java.text.ParseException;
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

    AutoCompleteTextView actEditRepCustName;

    TextView tvEditRepId, tvEditRepEngName, tvEditRepTotal, tvEditRepDate;

    EditText etEditRepLabour, etEditRepMaterial, etEditRepTransport, etEditRepComments;

    RadioButton rbEditRepYes, rbEditRepNo, rbEditRepInstallation,
            rbEditRepRepair, rbEditRepMaintenance, rbEditRepTerminate;

    RadioGroup rgEditRepWarranty, rgEditRepService;

    Button btnEditRepSave;

    private static List<Customer> custList;
    private static List<String> custNames = new ArrayList<>();

    private int _Report_Id = 0;
    RestService restService;
    UserSessionManager session;

    int repId = 0;
    int engId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_edit);
        restService = new RestService();
        session = new UserSessionManager(getApplicationContext());

        actEditRepCustName = (AutoCompleteTextView) findViewById(R.id.actEditRepCustName);

        tvEditRepTotal = (TextView) findViewById(R.id.tvEditRapTotal);
        tvEditRepId = (TextView) findViewById(R.id.tvEditRepId);
        tvEditRepEngName = (TextView) findViewById(R.id.tvEditRepEngName);
        tvEditRepDate = (TextView) findViewById(R.id.tvEditRepDate);
        etEditRepLabour = (EditText) findViewById(R.id.etEditRepLabour);
        etEditRepMaterial = (EditText) findViewById(R.id.etEditRepMaterial);
        etEditRepTransport = (EditText) findViewById(R.id.etEditRepTransport);
        etEditRepComments = (EditText) findViewById(R.id.etEditRepComments);


        rbEditRepYes = (RadioButton) findViewById(R.id.rbEditRepYes);
        rbEditRepNo = (RadioButton) findViewById(R.id.rbEditRepNo);
        rbEditRepInstallation = (RadioButton) findViewById(R.id.rbEditRepInstallation);
        rbEditRepRepair = (RadioButton) findViewById(R.id.rbEditRepRepair);
        rbEditRepMaintenance = (RadioButton) findViewById(R.id.rbEditRepMaintenance);
        rbEditRepTerminate = (RadioButton) findViewById(R.id.rbEditRepTerminate);

        rgEditRepWarranty = (RadioGroup) findViewById(R.id.rgEditRepWarranty);
        rgEditRepService = (RadioGroup) findViewById(R.id.rgEditRepService);

        btnEditRepSave = (Button) findViewById(R.id.btnEditRepSave);

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
        actEditRepCustName.setAdapter(adapter);

        rgEditRepWarranty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbEditRepYes){
                    etEditRepLabour.setText("0");
                    etEditRepLabour.setEnabled(false);
                    etEditRepLabour.setFocusableInTouchMode(false);
                    etEditRepLabour.clearFocus();

                    etEditRepMaterial.setText("0");
                    etEditRepMaterial.setEnabled(false);
                    etEditRepMaterial.setFocusableInTouchMode(false);
                    etEditRepMaterial.clearFocus();

                    etEditRepTransport.setText("0");
                    etEditRepTransport.setEnabled(false);
                    etEditRepTransport.setFocusableInTouchMode(false);
                    etEditRepTransport.clearFocus();
                }else{
                    etEditRepLabour.setEnabled(true);
                    etEditRepLabour.setFocusableInTouchMode(true);

                    etEditRepMaterial.setEnabled(true);
                    etEditRepMaterial.setFocusableInTouchMode(true);

                    etEditRepTransport.setEnabled(true);
                    etEditRepTransport.setFocusableInTouchMode(true);
                }
            }
        });


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
                        actEditRepCustName.setText(String.valueOf(customer.Name));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                tvEditRepDate.setText(String.valueOf(report.Date));

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
                tvEditRepTotal.setText(String.valueOf(report.Total));

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

    public static Double addTotal(String str1, String str2, String str3){
        double db1 = Double.parseDouble(str1);
        double db2 = Double.parseDouble(str2);
        double db3 = Double.parseDouble(str3);
        double dbTotal = db1 + db2 + db3;
        return dbTotal;
    }

    public void onEditRepSave(View view) {
        final String Name = actEditRepCustName.getText().toString();
        final String Labour = etEditRepLabour.getText().toString();
        final String Material = etEditRepMaterial.getText().toString();
        final String Transport = etEditRepTransport.getText().toString();

        //Customer name validation
        if (Name.length() == 0) {
            actEditRepCustName.requestFocus();
            actEditRepCustName.setError("FIELD CANNOT BE EMPTY!!");
            return; //exit out of submit form method
        } else if (rgEditRepWarranty.getCheckedRadioButtonId() == -1) {
            rbEditRepYes.setError("");
            rbEditRepNo.setError("");
            return;
        }else if(rgEditRepService.getCheckedRadioButtonId() == -1){
            rbEditRepInstallation.setError("");
            rbEditRepMaintenance.setError("");
            rbEditRepRepair.setError("");
            rbEditRepTerminate.setError("");
            return;
        }
        //Labour
        else if(Labour.length() == 0){
            etEditRepLabour.requestFocus();
            etEditRepLabour.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Labour)){
            etEditRepLabour.requestFocus();
            etEditRepLabour.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }
        //Material
        else if(Material.length() == 0){
            etEditRepMaterial.requestFocus();
            etEditRepMaterial.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Material)){
            etEditRepMaterial.requestFocus();
            etEditRepMaterial.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }
        //Transport
        else if(Transport.length() == 0){
            etEditRepTransport.requestFocus();
            etEditRepTransport.setError("FIELD CANNOT BE EMPTY!!");
            return;
        }else if(!isDouble(Transport)){
            etEditRepTransport.requestFocus();
            etEditRepTransport.setError("Cost must be in double format(e.g. 11.00)");
            return;
        }

        Report report = new Report();
        RadioButton radioButton =
                (RadioButton) findViewById(rgEditRepService.getCheckedRadioButtonId());
        boolean checkExist = false;

        //check customer exist
        loop1:
        for (Customer c : custList) {
            if (c.Name.equals(actEditRepCustName.getText().toString())) {
                report.CustomerId = c.CustomerId;
                Toast.makeText(ReportEdit.this, "Customer is available!",
                        Toast.LENGTH_LONG).show();
                checkExist = true;
                break loop1;
            }
        }

        if (checkExist == false) {
            Toast.makeText(ReportEdit.this, "Customer does not exist!",
                    Toast.LENGTH_LONG).show();
            return;
        }


        report.Id = repId;
        report.Date = tvEditRepDate.getText().toString();

        if(rbEditRepNo.isChecked()){
            report.Warranty = false;
        }else if(rbEditRepYes.isChecked()){
            report.Warranty = true;
        }

        tvEditRepTotal.setText(String.valueOf(addTotal(Labour, Material, Transport)));

        report.ServiceNature = String.valueOf(radioButton.getText());
        report.LabourCharge = Double.valueOf(etEditRepLabour.getText().toString());
        report.TotalMaterial = Double.valueOf(etEditRepMaterial.getText().toString());
        report.Transport = Double.valueOf(etEditRepTransport.getText().toString());
        report.Total = Double.valueOf(tvEditRepTotal.getText().toString());
        report.Comments = String.valueOf(etEditRepComments.getText());
        report.EngineerId = engId;

        restService.getService().updateReportById(repId, report, new Callback<Report>() {
            @Override
            public void success(Report report, Response response) {
                Toast.makeText(ReportEdit.this, "Report Updated!", Toast.LENGTH_LONG).show();
                btnEditRepSave.setEnabled(false);
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
