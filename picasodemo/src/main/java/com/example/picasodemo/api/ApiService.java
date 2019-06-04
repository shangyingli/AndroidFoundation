package com.example.picasodemo.api;

import com.example.picasodemo.bean.CatBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

//retrofit网络请求接口
public interface ApiService {

    @Streaming
    @GET("catsDemo/CatList.json")
    Observable<List<CatBean>> requestByRx();
}
