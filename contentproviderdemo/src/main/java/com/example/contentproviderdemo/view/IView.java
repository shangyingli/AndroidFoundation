package com.example.contentproviderdemo.view;

import com.example.contentproviderdemo.bean.FreezeBean;

import java.util.List;

public interface IView {

    void showLoading();

    void hideLoading();

    void fillView(List<FreezeBean> freezeBeans);
}
