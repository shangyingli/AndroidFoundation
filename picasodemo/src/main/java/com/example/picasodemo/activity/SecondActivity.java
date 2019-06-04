package com.example.picasodemo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.picasodemo.R;
import com.example.picasodemo.adapter.CustomRecyclerView;
import com.example.picasodemo.adapter.HorizonRecyclerAdapter;
import com.example.picasodemo.bean.CatBean;
import com.example.picasodemo.presenter.CatsPresent;
import com.example.picasodemo.presenter.CatsPresenterImp;
import com.example.picasodemo.view.BaseView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends BaseActivity<BaseView, CatsPresent<BaseView>> implements BaseView,
        CustomRecyclerView.OnItemScrollChangeListener, HorizonRecyclerAdapter.OnItemClickListener {

    private ProgressDialog progressDialog;
    private List<CatBean> responseData = new ArrayList<>();
    public final static String TAG = SecondActivity.class.getSimpleName();
    private HorizonRecyclerAdapter adapter;
    private CustomRecyclerView customRecyclerView;
    private ImageView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        contentView = findViewById(R.id.id_content);
        customRecyclerView = findViewById(R.id.id_recyclerview_horizontal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        customRecyclerView.setLayoutManager(layoutManager);
        adapter = new HorizonRecyclerAdapter(this, responseData);
        customRecyclerView.setAdapter(adapter);
        customRecyclerView.setOnItemScrollChangeListener(this);
        adapter.setOnItemClicked(this);
        mPresenter.fetchData();
    }

    @Override
    public CatsPresent<BaseView> createPresenter() {
        return new CatsPresenterImp();
    }

    @Override
    public void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载中");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(SecondActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void fill(List<CatBean> catBeans) {
        Log.d(TAG, catBeans.toString());
        responseData.clear();
        responseData.addAll(catBeans);
        responseData.addAll(catBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChange(View view, int position) {
        Log.d(TAG, "onChange : " + position);
        String url  = responseData.get(position).getImageUrl();
        Glide.with(this).load(url).into(contentView);

    }

    @Override
    public void onItemClicked(View view, int position) {
        Log.d(TAG, "onItemClicked : " + position);
        String url  = responseData.get(position).getImageUrl();
        Glide.with(this).load(url).into(contentView);
    }
}
