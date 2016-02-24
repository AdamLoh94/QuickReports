package com.mercury.gabadam.quickreports;

import android.app.Application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by AdamLoh on 24/2/2016.
 */
public class CustNameList extends Application {
    public String[] custList;
    public ArrayList<String> custNames;

    public CustNameList(){

    }

    public String[] getCustList(){
        RestService restService = new RestService();
        restService.getService().getCustomer(new Callback<List<Customer>>() {
            @Override
            public void success(List<Customer> customers, Response response) {
                custNames = new ArrayList<String>();
                for(Customer c : customers) {
                    custNames.add(c.Name);
                }
                custList = new String[custNames.size()];
                for(int i=0; i<custNames.size(); i++) {
                    custList[i] = custNames.get(i);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return custList;
    }
}
