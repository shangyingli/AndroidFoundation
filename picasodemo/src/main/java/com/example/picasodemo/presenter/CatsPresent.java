package com.example.picasodemo.presenter;

import com.example.picasodemo.view.BaseView;

import java.lang.ref.WeakReference;

public abstract class CatsPresent<T> {

    private WeakReference<T> mViewReference;
    public abstract void fetchData();

    public void attachView(T view) {
        mViewReference = new WeakReference<T>(view);
    }

    public void detachView() {
        if (mViewReference != null) {
            mViewReference.clear();
            mViewReference = null;
        }
    }

    public T getView() {
        if (mViewReference != null) {
            return mViewReference.get();
        }
        return null;
    }
}
