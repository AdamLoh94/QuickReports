package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomerDetailsActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    Button btnSave, btnClose;
    EditText editTextCustID, editTextName, editTextContact, editTextAddress, editTextEngName;
    RadioButton rbTrue, rbFalse;
    RadioGroup activeRG;
    List<Engineer> engAll;
    ArrayList<String> engName = new ArrayList<String>();
    private int _Customer_Id = 0;
    RestService restService;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restService = new RestService();
        setContentView(R.layout.activity_customer_details);
        session = new UserSessionManager(getApplicationContext());

        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        editTextCustID = (EditText) findViewById(R.id.editTextCustID);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextEngName = (EditText) findViewById(R.id.editTextEngName);

        activeRG = (RadioGroup) findViewById(R.id.ActiveRG);
        rbTrue = (RadioButton) findViewById(R.id.activeTrue);
        rbFalse = (RadioButton) findViewById(R.id.activeFalse);

        restService.getService().getEngineer(new Callback<List<Engineer>>() {
            @Override
            public void success(List<Engineer> engineers, Response response) {
                engAll = engineers;
                for(Engineer e: engineers)
                {
                    engName.add(String.valueOf(e.Name));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


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

                    restService.getService().getEngineerByID(customer.EngineerId, new Callback<Engineer>() {
                        @Override
                        public void success(Engineer engineer, Response response) {
                            editTextEngName.setText(String.valueOf(engineer.Name));
                        }

                        @Override
                        public void failure(RetrofitError error) {   }
                    });

                    if(customer.Active)
                    {
                        activeRG.check(R.id.activeTrue);
                    }
                    else
                    {
                        activeRG.check(R.id.activeFalse);
                    }
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
//            customer.EngineerId = Integer.parseInt(editTextEngName.getText().toString());

            boolean checkExist = false;

            //check customer exist
            loop1:
            for (Engineer e : engAll) {
                if (e.Name.equals(editTextEngName.getText().toString())) {
                    customer.EngineerId = e.Id;
                    Toast.makeText(CustomerDetailsActivity.this, "Engineer successfully added!",
                            Toast.LENGTH_SHORT).show();
                    checkExist = true;
                    break loop1;
                }
            }

            if(checkExist == false){
                Toast.makeText(CustomerDetailsActivity.this, "Engineer does not exist!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if(rbTrue.isChecked())
            {
                customer.Active=true;
            }
            else if(rbFalse.isChecked())
            {
                customer.Active=false;
            }

            customer.CustomerId = _Customer_Id;

            if (_Customer_Id == 0)
            {
                restService.getService().addCustomer(customer, new Callback<Customer>()
                {
                    @Override
                    public void success(Customer customer, Response response)
                    {
                        Toast.makeText(CustomerDetailsActivity.this, "New Customer Inserted.", Toast.LENGTH_LONG).show();
                        btnSave.setEnabled(false);
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
                        btnSave.setEnabled(false);
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
