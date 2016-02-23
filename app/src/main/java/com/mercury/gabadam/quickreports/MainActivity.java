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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    ListView listView;
    Button btnGetAll, btnAdd, btnBack;
    RestService restService;
    TextView engineer_Id;
    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        restService = new RestService();
        setContentView(R.layout.activity_main);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button,
        // so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
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

            Intent intent = new Intent(this,Detail.class);
            intent.putExtra("engineerName",0);
            startActivity(intent);

        }
        else if (v== findViewById(R.id.btnBack))
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
        restService.getService().getEngineer(new Callback<List<Engineer>>() {
            @Override
            public void success(List<Engineer> engineers, Response response) {
                ListView lv = (ListView) findViewById(R.id.listView);

                CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, R.layout.view_engineer, engineers);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        engineer_Id = (TextView) view.findViewById(R.id.engineer_Id);
                        String engineerId = engineer_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(), Detail.class);
                        objIndent.putExtra("engineerName", Integer.parseInt(engineerId));
                        startActivity(objIndent);
                    }
                });
                lv.setAdapter(customAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private void refreshScreen()
    {

        //Call to server to grab list of student records. this is a asyn
        restService.getService().getEngineerByID(session.getUserId(), new Callback<Engineer>()
        {
            @Override
            public void success(Engineer engineers, Response response)
            {
                List<Engineer> eList = new ArrayList<Engineer>();
                eList.add(engineers);
                ListView lv = (ListView) findViewById(R.id.listView);

                CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, R.layout.view_engineer, eList);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        engineer_Id = (TextView) view.findViewById(R.id.engineer_Id);
                        String engineerId = engineer_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(), Detail.class);
                        objIndent.putExtra("engineer_Id", Integer.parseInt(engineerId));
                        startActivity(objIndent);
                    }
                });
                lv.setAdapter(customAdapter);

            }


            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }*/
}
