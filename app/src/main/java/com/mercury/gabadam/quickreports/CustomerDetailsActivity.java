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

public class CustomerDetailsActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    Button btnSave, btnClose;
    EditText editTextCustID, editTextName, editTextContact, editTextAddress, editTextEngID, editTextActive;
    private int _Customer_Id = 0;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restService = new RestService();
        setContentView(R.layout.activity_customer_details);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        editTextCustID = (EditText) findViewById(R.id.editTextCustID);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextEngID = (EditText) findViewById(R.id.editTextEngID);
        editTextActive = (EditText) findViewById(R.id.editTextActive);

        _Customer_Id = 0;
        Intent intent = getIntent();
        _Customer_Id = intent.getIntExtra("customerId", 0);
        if (_Customer_Id > 0) {
            restService.getService().getCustomerByID(_Customer_Id, new Callback<Customer>() {
                @Override
                public void success(Customer customer, Response response) {

                    editTextCustID.setText(String.valueOf(customer.CustomerId));
                    editTextName.setText(customer.Name);
                    editTextContact.setText(String.valueOf(customer.Contact));
                    editTextAddress.setText(customer.Customeraddress);
                    editTextEngID.setText(String.valueOf(customer.EngineerId));
                    editTextActive.setText(String.valueOf(customer.Active));
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(CustomerDetailsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_customer_settings) {
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
        else if (v == btnSave)
        {

            Customer customer = new Customer();
            Integer status = 0;
            customer.CustomerId = Integer.parseInt(editTextCustID.getText().toString());
            customer.Name = editTextName.getText().toString();
            customer.Contact = Integer.parseInt(editTextContact.getText().toString());
            customer.Customeraddress = editTextAddress.getText().toString();
            customer.EngineerId = Integer.parseInt(editTextEngID.getText().toString());
            customer.Active = Boolean.parseBoolean(editTextActive.getText().toString());
            customer.CustomerId = _Customer_Id;

            if (_Customer_Id == 0)
            {
                restService.getService().addCustomer(customer, new Callback<Customer>()
                {
                    @Override
                    public void success(Customer customer, Response response)
                    {
                        Toast.makeText(CustomerDetailsActivity.this, "New Customer Inserted.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        Toast.makeText(CustomerDetailsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }
            else
            {
                restService.getService().updateCustomerById(_Customer_Id, customer, new Callback<Customer>()
                {
                    @Override
                    public void success(Customer customer, Response response)
                    {
                        Toast.makeText(CustomerDetailsActivity.this, "Customer Record updated.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        Toast.makeText(CustomerDetailsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }

        }
    }
}
