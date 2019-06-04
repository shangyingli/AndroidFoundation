package com.example.picasodemo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.picasodemo.presenter.CatsPresent;
import com.example.picasodemo.view.BaseView;

public abstract class BaseActivity<V, P extends CatsPresent<V>> extends Activity {
    protected CatsPresent<V> mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V)this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public abstract P createPresenter();
}
