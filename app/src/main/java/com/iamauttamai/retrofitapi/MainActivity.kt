package com.iamauttamai.retrofitapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.iamauttamai.retrofitapi.api.HttpManager
import retrofit2.Call
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callService()

    }

    private fun callService() {
        val params = HashMap<String, String>()
        params["param"] = "value"
        val httpManager = HttpManager(this)
        val call: Call<JsonObject> = httpManager
            .init()
            .GetService()
        httpManager.call(call, object : HttpManager.callBack {
            override fun onResponse(responseCode: Call<JsonObject?>?, response: Response<JsonObject?>) {
                if (response.code() == HttpsURLConnection.HTTP_OK)
                    Log.i("RETROFIT", "SUCCESS")
                else
                    Log.i("RETROFIT", "FAIL")
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}