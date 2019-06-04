package com.example.contentproviderdemo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.contentproviderdemo.present.BasePresenter;
import com.example.contentproviderdemo.view.IView;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends Activity {

    protected BasePresenter<V> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView((V)this);
    }

    public abstract T createPresenter();
}
