package com.lwm.videoinfoapp.api;

import com.lwm.videoinfoapp.entity.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 新闻请求API
 */
public interface NewsAPI {
    /**
     * 使用get请求，根据接口要求传递参数
     *
     * @param type 新闻类型
     * @param key  聚合APPkey
     * @return 返回结果
     */
    @GET("index")
    Call<News> getNews(@Query("type") String type, @Query("key") String key);
}