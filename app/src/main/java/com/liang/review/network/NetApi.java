package com.liang.review.network;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Author:bernie-mac
 * Data:2021/5/27 16:05
 * Description: com.liang.review.network
 */
public interface NetApi {

    @GET("recommendPoetry")
    public Observable<String> getPoetry();

}


