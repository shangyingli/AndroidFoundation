package com.example.contentproviderdemo.model;

import com.example.contentproviderdemo.bean.FreezeBean;

import java.util.List;

public interface FreezeModel {

    void getFreezeInfo(Callback callback);

    void getUnfreezeInfo(Callback callback);

    boolean doFreeze(String freezeItems);

    boolean doUnfreeze(String unFreezeItems);
}
