package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    RestService restService;
    private EditText editTextUsername, editTextPw;

    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        restService = new RestService();
        // User Session Manager
        session = new UserSessionManager(getApplicationContext());
    }


    public void onLoginClick(View v)
    {
        restService.getService().getEngineer(new Callback<List<Engineer>>()
        {
            @Override
            public void success(List<Engineer> engineers, Response response)
            {
                editTextUsername = (EditText) findViewById(R.id.editTextUsername);
                String id = editTextUsername.getText().toString();
                editTextPw = (EditText) findViewById(R.id.editTextPw);
                String pw = editTextPw.getText().toString();

                loop1:for (Engineer a : engineers)
                {
                    if (id.equals(a.Username))
                    {
                        if (pw.equals(a.Password))
                        {
                            if(a.Admin)
                            {
                                if(a.Active)
                                {
                                    Toast.makeText(LoginActivity.this, "Redirecting..", Toast.LENGTH_SHORT).show();
                                    session.createUserLoginSession(a.Id, a.Username);
                                    Intent intent = new Intent("com.mercury.gabadam.quickreports.HomeActivity");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // Add new Flag to start new Activity
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "User is not Active", Toast.LENGTH_SHORT).show();
                                    break loop1;
                                }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "User is not an Admin", Toast.LENGTH_SHORT).show();
                                break loop1;
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                            break loop1;
                        }
                    }
                    else
                    {

                    }
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(LoginActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onExitClick(View view) {
        finish();
        System.exit(0);
    }
}

