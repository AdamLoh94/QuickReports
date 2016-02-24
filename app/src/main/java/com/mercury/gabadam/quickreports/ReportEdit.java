package com.mercury.gabadam.quickreports;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AdamLoh on 24/2/2016.
 */
public class ReportEdit extends Activity implements AlertDialogRadio.AlertPositiveListener {

    TextView tvEditRepId, tvEditRepEngName;

    EditText etEditRepDate, etEditRepLabour, etEditRepMaterial, etEditRepTransport,
            etEditRepTotal, etEditRepComments, etEditRepTest;

    RadioButton rbEditRepYes, rbEditRepNo, rbEditRepInstallation,
            rbEditRepRepair, rbEditRepMaintenance, rbEditRepTerminate;

    RadioGroup rgEditRepWarranty, rgEditRepService;

    Button btnEditRepBack, getBtnEditRepSave;

    List<Customer> custList;
    ArrayList<String> custNameList;

    private int _Report_Id = 0;
    RestService restService;
    UserSessionManager session;

    int position = 0;

    CustNameList customerName = new CustNameList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_edit);
        restService = new RestService();
        session = new UserSessionManager(getApplicationContext());

        custList = new ArrayList<>();
        custNameList = new ArrayList<>();

        tvEditRepId = (TextView) findViewById(R.id.tvEditRepId);
        tvEditRepEngName = (TextView) findViewById(R.id.tvEditRepEngName);

        etEditRepDate = (EditText) findViewById(R.id.etEditRepDate);
        etEditRepLabour = (EditText) findViewById(R.id.etEditRepLabour);
        etEditRepMaterial = (EditText) findViewById(R.id.etEditRepMaterial);
        etEditRepTransport = (EditText) findViewById(R.id.etEditRepTransport);
        etEditRepTotal = (EditText) findViewById(R.id.etEditRepTotal);
        etEditRepComments = (EditText) findViewById(R.id.etEditRepComments);

        //test stuff
        etEditRepTest = (EditText) findViewById(R.id.etEditRepTest);


        rbEditRepYes = (RadioButton) findViewById(R.id.rbEditRepYes);
        rbEditRepNo = (RadioButton) findViewById(R.id.rbEditRepNo);
        rbEditRepInstallation = (RadioButton) findViewById(R.id.rbEditRepInstallation);
        rbEditRepRepair = (RadioButton) findViewById(R.id.rbEditRepRepair);
        rbEditRepMaintenance = (RadioButton) findViewById(R.id.rbEditRepMaintenance);
        rbEditRepTerminate = (RadioButton) findViewById(R.id.rbEditRepTerminate);

        rgEditRepWarranty = (RadioGroup) findViewById(R.id.rgEditRepWarranty);
        rgEditRepService = (RadioGroup) findViewById(R.id.rgEditRepService);

        btnEditRepBack = (Button) findViewById(R.id.btnEditRepBack);
        getBtnEditRepSave = (Button) findViewById(R.id.btnEditRepSave);



        Intent intent = getIntent();
        _Report_Id = intent.getIntExtra("reportId", 0);
        restService.getService().getReportByID(_Report_Id, new Callback<Report>() {
            @Override
            public void success(Report report, Response response) {
                tvEditRepId.setText(String.valueOf(report.Id));
                restService.getService().getCustomerByID(report.CustomerId, new Callback<Customer>() {
                    @Override
                    public void success(Customer customer, Response response) {
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }

                });
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
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                AlertDialogRadio alert = new AlertDialogRadio();
                Bundle b = new Bundle();
                b.putInt("position", position);
                alert.setArguments(b);
                alert.show(manager, "alert_dialog_radio");
            }
        };
        etEditRepTest.setOnClickListener(listener);
    }



    @Override
    public void onPositiveClick(int position) {
        this.position = position;
        etEditRepTest.setText(customerName.getCustList()[this.position]);
    }
}
