package com.example.picasodemo.presenter;

import com.example.picasodemo.bean.CatBean;
import com.example.picasodemo.model.Callback;
import com.example.picasodemo.model.CatsModel;
import com.example.picasodemo.model.CatsModelImp;
import com.example.picasodemo.view.BaseView;

import java.util.List;

public class CatsPresenterImp extends CatsPresent<BaseView> {

    private CatsModel catsModel = new CatsModelImp();

    public CatsPresenterImp() {

    }

    @Override
    public void fetchData() {
        final BaseView view = super.getView();
        view.showLoading();
        catsModel.getCatsData(new Callback() {
            @Override
            public void onSuccess(List<CatBean> catBeans) {
                view.fill(catBeans);
                view.hideLoading();
            }

            @Override
            public void onFailed(Throwable e) {
                view.hideLoading();
                view.showError("加载数据失败！");
            }
        });
    }
}
