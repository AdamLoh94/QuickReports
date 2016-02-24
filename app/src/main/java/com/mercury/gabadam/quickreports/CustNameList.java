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
public class CustNameList {
    public String[] custList;

    public CustNameList(){
        super();
    }

    public String[] getCustList(){
        RestService restService = new RestService();
        restService.getService().getCustomer(new Callback<List<Customer>>() {
            @Override
            public void success(List<Customer> customers, Response response) {
                custList = new String[customers.size()];
                for(int i=0; i<customers.size(); i++){
                    custList[i]=customers.get(i).Name;
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return custList;
    }
}
