package com.mercury.gabadam.quickreports;

/**
 * Created by user on 15/2/2016.
 */
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


public interface InstituteService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //Engineers
    //i.e. http://servicereport-m.azurewebsites.net/api/engineers
    @GET("/engineers")
    public void getEngineer(Callback<List<Engineer>> callback);

    //i.e. http://servicereport-m.azurewebsites.net/api/engineers/1
    //Get engineer record base on ID
    @GET("/engineers/{id}")
    public void getEngineerByID(@Path("id") Integer id,Callback<Engineer> callback);

    //i.e. http://servicereport-m.azurewebsites.net/api/engineers/1
    //Update engineer record and post content in HTTP request BODY
    @PUT("/engineers/{id}")
    public void updateEngineerById(@Path("id") Integer id,@Body Engineer engineer,Callback<Engineer> callback);

    //i.e. http://servicereport-m.azurewebsites.net/api/engineers/
    //Create & Add Engineer record and post content in HTTP request BODY
    @POST("/engineers")
    public void addEngineer(@Body Engineer engineer,Callback<Engineer> callback);



    //Reports
    //i.e. http://servicereport-m.azurewebsites.net/api/reports
    @GET("/reports")
    public void getReport(Callback<List<Report>> callback);

    //i.e. http://servicereport-m.azurewebsites.net/api/reports/1
    //Get report record base on ID
    @GET("/reports/{id}")
    public void getReportByID(@Path("id") Integer id,Callback<Report> callback);

    //i.e. http://servicereport-m.azurewebsites.net/api/reports/1
    //Update report record and post content in HTTP request BODY
    @PUT("/reports/{id}")
    public void updateReportById(@Path("id") Integer id,@Body Report report,Callback<Report> callback);

    //i.e. http://servicereport-m.azurewebsites.net/api/reports/
    //Create & Add report record and post content in HTTP request BODY
    @POST("/reports")
    public void addReport(@Body Report report,Callback<Report> callback);


}
