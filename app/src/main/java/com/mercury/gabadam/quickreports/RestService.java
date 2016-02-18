package com.mercury.gabadam.quickreports;

/**
 * Created by user on 15/2/2016.
 */
public class RestService {

    private static final String URL = "http://servicereport-m.azurewebsites.net/api/";
    private retrofit.RestAdapter restAdapter;
    private InstituteService apiService;

    public RestService()
    {
        restAdapter = new retrofit.RestAdapter.Builder().setEndpoint(URL).setLogLevel(retrofit.RestAdapter.LogLevel.FULL).build();
        apiService = restAdapter.create(InstituteService.class);
    }

    public InstituteService getService()
    {
        return apiService;
    }
}
