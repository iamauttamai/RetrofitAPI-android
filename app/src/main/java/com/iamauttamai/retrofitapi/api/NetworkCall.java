package com.iamauttamai.retrofitapi.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private HttpManager.callBack mCallBack;
    private Call<JsonObject> mCall;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    void onConnect(Context mContext, HttpManager.callBack callBack, Call<JsonObject> call){
        this.mContext = mContext;
        this.mCallBack = callBack;
        this.mCall = call;
        executor.execute(() -> {
            //Background work here
            try {
                mCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        mCallBack.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                        mCallBack.onFailure(call, t);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
