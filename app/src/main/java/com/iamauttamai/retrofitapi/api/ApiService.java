package com.iamauttamai.retrofitapi.api;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("https://api.example.com/")
    Call<JsonObject> ExampleService(@QueryMap Map<String, String> params);

    @GET("todos/1")
    Call<JsonObject> GetService();

}

