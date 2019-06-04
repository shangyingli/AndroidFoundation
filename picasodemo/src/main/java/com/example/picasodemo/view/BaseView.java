package com.example.picasodemo.view;

import com.example.picasodemo.bean.CatBean;

import java.util.List;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showError(String msg);

    void fill(List<CatBean> catBeans);
}
