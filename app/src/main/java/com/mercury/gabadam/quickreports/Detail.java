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

public class Detail extends AppCompatActivity implements android.view.View.OnClickListener {

    Button btnSave, btnClose;
    EditText editTextId, editTextName, editTextUsername, editTextPw, editTextEmail, editTextHP, editTextAdmin, editTextActive;
    private int _Engineer_Id = 0;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restService = new RestService();
        setContentView(R.layout.activity_detail);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextHP = (EditText) findViewById(R.id.editTextHP);
        editTextAdmin = (EditText) findViewById(R.id.editTextAdmin);
        editTextActive = (EditText) findViewById(R.id.editTextActive);

        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        _Engineer_Id = 0;
        Intent intent = getIntent();
        _Engineer_Id = intent.getIntExtra("engineer_Id", 0);
        if (_Engineer_Id > 0) {
            restService.getService().getEngineerByID(_Engineer_Id, new Callback<Engineer>() {
                @Override
                public void success(Engineer engineer, Response response) {

                    editTextId.setText(String.valueOf(engineer.Id));
                    editTextName.setText(engineer.Name);
                    editTextUsername.setText(engineer.Username);
                    editTextPw.setText(String.valueOf(engineer.Password));
                    editTextEmail.setText(engineer.Email);
                    editTextHP.setText(String.valueOf(engineer.PhoneNum));
                    editTextAdmin.setText(String.valueOf(engineer.Admin));
                    editTextActive.setText(String.valueOf(engineer.Active));
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(Detail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

            Engineer engineer = new Engineer();
            Integer status = 0;
            engineer.Id = Integer.parseInt(editTextId.getText().toString());
            engineer.Name = editTextName.getText().toString();
            engineer.Username = editTextUsername.getText().toString();
            engineer.Password = editTextPw.getText().toString();
            engineer.Email = editTextEmail.getText().toString();
            engineer.PhoneNum = Integer.parseInt(editTextHP.getText().toString());
            engineer.Admin = Boolean.parseBoolean(editTextAdmin.getText().toString());
            engineer.Active = Boolean.parseBoolean(editTextActive.getText().toString());
            engineer.Id = _Engineer_Id;

            if (_Engineer_Id == 0)
            {
                restService.getService().addEngineer(engineer, new Callback<Engineer>()
                {
                    @Override
                    public void success(Engineer engineer, Response response)
                    {
                        Toast.makeText(Detail.this, "New Engineer Inserted.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        Toast.makeText(Detail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }
            else
            {
                restService.getService().updateEngineerById(_Engineer_Id, engineer, new Callback<Engineer>() {
                    @Override
                    public void success(Engineer engineer, Response response) {
                        Toast.makeText(Detail.this, "Engineer Record updated.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(Detail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }


        }
    }
}