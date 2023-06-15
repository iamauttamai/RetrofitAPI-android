package com.iamauttamai.retrofitapi.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.google.gson.JsonObject;
import com.iamauttamai.retrofitapi.config.Config;

import org.jetbrains.annotations.Nullable;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpManager {
    @Nullable
    public static Callback<JsonObject> response() {
        return null;
    }

    public interface callBack {
        void onResponse(Call<JsonObject> responseCode, Response<JsonObject> result);
        void onFailure(Call<JsonObject> call,Throwable t) ;
    }

    private final Context context;
    private ApiService service;

    public HttpManager(Context mContext) {
        context = mContext;
    }
    public HttpManager() {
        context = null;
    }

    public ApiService init() {
        try {
            OkHttpClient client = new OkHttpClient();
            try {
                X509TrustManager[] trustManagers;
                trustManagers = getTrust();
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(new TLSSocketFactory(), trustManagers[0])
                        .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                        .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                        .readTimeout(1, TimeUnit.MINUTES)
                        .addInterceptor(chain -> {
                            final String boundary = "*****" + System.currentTimeMillis() + "*****";
                            Request.Builder newRequest = chain.request().newBuilder();
                            newRequest.addHeader("Content-Type", "application/json");
                            return chain.proceed(newRequest.build());
                        })
                        .build();
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            assert context != null;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(new Config(context).apiURL())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            service = retrofit.create(ApiService.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return service;
    }
    @SuppressLint("CustomX509TrustManager")
    public X509TrustManager[] getTrust() throws KeyStoreException, NoSuchAlgorithmException {
        X509ExtendedTrustManager trustManager;
        X509TrustManager trustManagers1 = new EasyX509TrustManager(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            trustManager = new RootTrustManager(null);
        }else{
            return new X509TrustManager[]{trustManagers1};
        }
        return new X509TrustManager[]{trustManager};
    }

    public void call( Call<JsonObject> call,callBack callback) {
        new NetworkCall().onConnect(context,  callback, call);
    }
}