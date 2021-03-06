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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomerDetailsActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    Button btnSave, btnClose;

    TextView tvCustID;

    EditText editTextCustID, editTextName, editTextContact, editTextAddress, editTextEngName;

    RadioButton rbTrue, rbFalse;
    RadioGroup activeRG;

    List<Engineer> engAll;
    List<Engineer> engListActiveOnly;

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

        tvCustID = (TextView) findViewById(R.id.textViewCustID);

        editTextCustID = (EditText) findViewById(R.id.editTextCustID);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextEngName = (EditText) findViewById(R.id.editTextEngName);

        activeRG = (RadioGroup) findViewById(R.id.ActiveRG);
        rbTrue = (RadioButton) findViewById(R.id.activeTrue);
        rbFalse = (RadioButton) findViewById(R.id.activeFalse);

        engListActiveOnly = new ArrayList<>();

        restService.getService().getEngineer(new Callback<List<Engineer>>() {
            @Override
            public void success(List<Engineer> engineers, Response response) {
                engAll = engineers;
                for(Engineer e: engineers)
                {
                    //Add name to Engineer list to display Name
                    engName.add(String.valueOf(e.Name));
                    //Adding Active admins into List so to validate in the form
                    //Preventing user from adding Engineer name that is not active
                    if(e.Active)
                    {
                        engListActiveOnly.add(e);
                    }

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
        else
        {
            tvCustID.setVisibility(View.INVISIBLE);
            editTextCustID.setVisibility(View.INVISIBLE);
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

    public static boolean isInteger(String strInteger){
        try{
            Integer.parseInt(strInteger);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
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

            //validations Initialization
            final String Name = editTextName.getText().toString();
            final String Contact = editTextContact.getText().toString();
            final String CustAddress = editTextAddress.getText().toString();
            final String EngName = editTextEngName.getText().toString();

            //Validate Name
            if (Name.length() == 0)
            {
                editTextName.requestFocus();
                editTextName.setError("FIELD CANNOT BE EMPTY!!");
                return; //exit out of submit form method
            }
            //Validate Contact
            if (Contact.length() == 0) {
                editTextContact.requestFocus();
                editTextContact.setError("FIELD CANNOT BE EMPTY!!");
                return; //exit out of submit form method
            }
            else if(!isInteger(Contact)){
                editTextContact.requestFocus();
                editTextContact.setError("Contact must be in Integer format(e.g. 11, 1, 2)");
                return;
            }
            else if(Contact.length() != 8)
            {
                editTextContact.requestFocus();
                editTextContact.setError("Contact must 8 characters");
                return;
            }
            //Validate Customer Address
            if (CustAddress.length() == 0)
            {
                editTextAddress.requestFocus();
                editTextAddress.setError("FIELD CANNOT BE EMPTY!!");
                return; //exit out of submit form method
            }
            //Validate Engineer Name
            if (EngName.length() == 0)
            {
                editTextEngName.requestFocus();
                editTextEngName.setError("FIELD CANNOT BE EMPTY!!");
                return; //exit out of submit form method
            }
            if(activeRG.getCheckedRadioButtonId() == -1)
            {
                rbTrue.setError("");
                rbFalse.setError("");
                return;
            }



            Customer customer = new Customer();
            Integer status = 0;
            customer.CustomerId = _Customer_Id;
            customer.Name = editTextName.getText().toString();
            customer.Contact = Integer.parseInt(editTextContact.getText().toString());
            customer.Customeraddress = editTextAddress.getText().toString();
//            customer.EngineerId = Integer.parseInt(editTextEngName.getText().toString());

            boolean checkExist = false;
            boolean checkActive = false;

            //check customer exist & if engineer is existed
            loop1:
            for (Engineer e : engAll) {
                if (e.Name.equals(editTextEngName.getText().toString())) {
                    checkExist = true;
                    for(Engineer a : engListActiveOnly)
                    {
                        if(a.Name.equals(editTextEngName.getText().toString()))
                        {
                            customer.EngineerId = a.Id;
                            Toast.makeText(CustomerDetailsActivity.this, "Engineer successfully added!",
                                    Toast.LENGTH_SHORT).show();
                            checkActive = true;
                            break loop1;
                        }
                    }
                }
            }

            //checkExist false means Engineer does not exist
            if(checkExist == false){
                Toast.makeText(CustomerDetailsActivity.this, "Engineer does not exist!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //checkActive false mean Engineer entered not active
            if(checkActive == false){
                Toast.makeText(CustomerDetailsActivity.this, "Engineer is not Active!",
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
