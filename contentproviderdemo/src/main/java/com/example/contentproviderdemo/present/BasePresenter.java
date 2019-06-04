package com.example.contentproviderdemo.present;


import android.content.pm.PackageInfo;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class BasePresenter<T> {

    private WeakReference<T> viewReference;

    public abstract void fetchFreezeData();

    public abstract void fetchUnFreezeData();

    public abstract boolean freezeApps(String freezeItems);

    public abstract boolean unFreezeApps(String unFreezeItems);

    public void attachView(T view) {
        viewReference = new WeakReference<>(view);
    }

    public void detachView() {
        if (viewReference != null) {
            viewReference.clear();
            viewReference = null;
        }
    }

    public T getView() {
        return viewReference.get();
    }
}
