package com.liang.review.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:bernie-mac
 * Data:2021/5/27 15:22
 * Description: com.liang.review.network
 */
public class RetrofitFactory {
    private static volatile RetrofitFactory instance;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public <T>T create(Class<T> clazz){
        return getRetrofit().create(clazz);
    }

    public static RetrofitFactory getInstance() {
        if (instance==null){
            synchronized (RetrofitFactory.class){
                if (instance==null){
                    instance=new RetrofitFactory();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        if (retrofit==null){
            synchronized (RetrofitFactory.class){
                retrofit=  new  Retrofit.Builder()
                       .baseUrl("https://api.apiopen.top/")
                       .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient())
                       .build();
            }
        }
        return retrofit;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient==null){
            synchronized (RetrofitFactory.class){
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30,TimeUnit.SECONDS)
                        .writeTimeout(30,TimeUnit.SECONDS)
                        .addInterceptor(new NetDataIntercepter())
                        .build();
            }
        }
        return okHttpClient;
    }
}
