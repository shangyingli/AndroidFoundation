package com.example.picasodemo.model;

import com.example.picasodemo.bean.CatBean;

import java.util.List;

public interface Callback {

    void onSuccess(List<CatBean> catBeans);

    void onFailed(Throwable e);
}
