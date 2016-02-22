package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AdamLoh on 20/2/2016.
 */

public class HomeActivity extends AppCompatActivity {

    RestService restService;
    TextView engineerName;
    UserSessionManager session;
    private List<Report> list;
    Button logOut;
    private int custId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restService = new RestService();
        setContentView(R.layout.activity_home);
        engineerName = (TextView) findViewById(R.id.engineerName);
        session = new UserSessionManager(getApplicationContext());

        if(session.checkLogin()) {
            finish();
        }
        refreshScreen();

    }

    private void refreshScreen(){
        restService.getService().getEngineerByID(session.getUserId(), new Callback<Engineer>() {
            @Override
            public void success(Engineer engineer, Response response) {
                engineerName.setText(engineer.Name);
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(HomeActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }


    public void onLogoutClick(View view) {
        session.logoutUser();
    }

    public void onViewAllReportClick(View view) {
        Intent intent = new Intent(this,ReportViewAllActivity.class);
        startActivity(intent);
    }

    public void onClickViewInfo(View view) {
        engineerName = (TextView)view.findViewById(R.id.engineerName);
        Intent objIndent = new Intent(getApplicationContext(), Detail.class);
        objIndent.putExtra("engineerName", session.getUserId());
        startActivity(objIndent);
    }

    public void onAddEngineer(View view) {
        Intent intent = new Intent(this,Detail.class);
        intent.putExtra("engineerName",0);
        startActivity(intent);
    }
}
