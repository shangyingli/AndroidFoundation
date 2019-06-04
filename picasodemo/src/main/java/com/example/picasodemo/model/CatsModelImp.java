package com.example.picasodemo.model;

import com.example.picasodemo.api.ApiRetrofit;
import com.example.picasodemo.api.ApiService;
import com.example.picasodemo.bean.CatBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CatsModelImp implements CatsModel {

    private ApiRetrofit apiRetrofit = ApiRetrofit.getInstance();
    private Disposable disposable; //// TODO: 2019/4/23  何时关闭

    @Override
    public void getCatsData(final Callback callback) {
        ApiService apiService = apiRetrofit.getApiService();
        Observable<List<CatBean>> observable = apiService.requestByRx();
        // TODO: 2019/4/24 失败重试
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CatBean>>() {
                    @Override
                    public void accept(List<CatBean> o) throws Exception {
                        callback.onSuccess(o);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onFailed(throwable);
                    }
                });
    }
}
