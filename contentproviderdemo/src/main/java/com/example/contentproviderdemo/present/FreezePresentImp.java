package com.example.contentproviderdemo.present;

import android.content.Context;

import com.example.contentproviderdemo.bean.FreezeBean;
import com.example.contentproviderdemo.model.Callback;
import com.example.contentproviderdemo.model.FreezeModel;
import com.example.contentproviderdemo.model.FreezeModelImp;
import com.example.contentproviderdemo.view.IView;

import java.util.List;

public class FreezePresentImp extends BasePresenter<IView> {

    private FreezeModel freezeModel;

    public FreezePresentImp(Context context) {
        freezeModel = new FreezeModelImp(context);
    }

    @Override
    public void fetchFreezeData() {
        freezeModel.getFreezeInfo(new Callback() {
            @Override
            public void onSuccess(List<FreezeBean> freezeBeans) {
                getView().fillView(freezeBeans);
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

    @Override
    public void fetchUnFreezeData() {
        freezeModel.getUnfreezeInfo(new Callback() {
            @Override
            public void onSuccess(List<FreezeBean> freezeBeans) {
                getView().fillView(freezeBeans);
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

    @Override
    public boolean freezeApps(String freezeItem) {
        return freezeModel.doFreeze(freezeItem);
    }

    @Override
    public boolean unFreezeApps(String unFreezeItem) {
        return true;
    }
}
