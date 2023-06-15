package com.iamauttamai.retrofitapi.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Locale;

public class Config {

    private final Context context;
    public Config(Context context){
        this.context = context;
    }

    // @Link API
    public String apiURL(){
        return "https://jsonplaceholder.typicode.com/";
    }

}
