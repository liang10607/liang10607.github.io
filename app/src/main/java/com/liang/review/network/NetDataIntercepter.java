package com.liang.review.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bernie-mac
 * Data:2021/5/27 15:49
 * Description: com.liang.review.network
 */
public class NetDataIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        long endTime=0;
        String responseBodyStr="";
        String responseHeaderStr="";
        String requestBodyStr="";
        String requestHeaderStr="";
        String url="";
        long startTime=0;
        Response response=null;
        try {
            startTime = System.currentTimeMillis();
            Request request = chain.request();
            url = request.url().toString();
            requestHeaderStr = request.headers().toString();
            if (request.body()!=null){
                requestBodyStr = request.body().toString();
            }
            response = chain.proceed(request);
            responseHeaderStr = response.headers().toString();
            if (response.body()!=null){
                responseBodyStr = response.body().string();
            }
            endTime = System.currentTimeMillis();
            if (response ==null){
                throw new IOException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
         long duration = endTime -startTime;
            Log.d("Net","duration:"+duration +"\n"+
                    "url:"+url +"\n"+
                    "requestHeaderStr:"+requestHeaderStr +"\n"+
                    "requestBodyStr:"+requestBodyStr +"\n"+
                    "responseHeaderStr:"+responseHeaderStr +"\n"+
                    "responseBodyStr:"+responseBodyStr +"\n");
        }

        return response;
    }
}
