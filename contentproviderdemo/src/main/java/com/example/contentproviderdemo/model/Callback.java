package com.example.contentproviderdemo.model;

import com.example.contentproviderdemo.bean.FreezeBean;

import java.util.List;

public interface Callback {
    void onSuccess(List<FreezeBean> freezeBeans);

    void onFailed(Throwable throwable);
}
