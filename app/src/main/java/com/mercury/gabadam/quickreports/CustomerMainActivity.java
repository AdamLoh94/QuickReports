package com.mercury.gabadam.quickreports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomerMainActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    ListView listViewCustomer;
    Button btnGetAll, btnAdd, btnBack;
    RestService restService;
    TextView customer_Id;
    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restService = new RestService();
        setContentView(R.layout.activity_customer_main);
        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnGetAll.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        session = new UserSessionManager(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity and finish current activity from activity stack.
        if(session.checkLogin())
        {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button,
        // so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_customer_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {

        super.onResume();
        refreshScreen();
    }

    @Override
    public void onClick(View v)
    {
        if (v== findViewById(R.id.btnAdd))
        {

            Intent intent = new Intent(this,CustomerDetailsActivity.class);
            intent.putExtra("customer_Id",0);
            startActivity(intent);

        }
        else if (v == findViewById(R.id.btnBack))
        {
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        else
        {
            // You should use refreshScreen() instead, just show you an easier method only :P
            refreshScreen();
        }
    }

    private void refreshScreen()
    {

    //Call to server to grab list of student records. this is a asyn
    restService.getService().getCustomer(new Callback<List<Customer>>() {
        @Override
        public void success(List<Customer> customers, Response response) {
            ListView lv = (ListView) findViewById(R.id.listViewCustomer);

            CustomerCustomAdapter customerCustomAdapter = new CustomerCustomAdapter(CustomerMainActivity.this, R.layout.view_customer, customers);
            lv.setAdapter(customerCustomAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    customer_Id = (TextView)view.findViewById(R.id.customer_Id);
                    String customerId = customer_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
                    objIndent.putExtra("customerId", Integer.parseInt(customerId));
                    startActivity(objIndent);
                }
            });
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(CustomerMainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    });
    }


}
