package com.example.contentproviderdemo.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.contentproviderdemo.R;
import com.example.contentproviderdemo.adapter.FreezeAdapter;
import com.example.contentproviderdemo.bean.FreezeBean;
import com.example.contentproviderdemo.present.BasePresenter;
import com.example.contentproviderdemo.present.FreezePresentImp;
import com.example.contentproviderdemo.view.IView;

import java.util.ArrayList;
import java.util.List;

public class FreezeMainActivity extends BaseActivity<IView, BasePresenter<IView>> implements IView{

    private FreezeAdapter freezeAdapter;
    private Button jump;
    private List<FreezeBean> mFreezeBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.view_freeze_grid);
        jump = findViewById(R.id.jump);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        freezeAdapter = new FreezeAdapter(mFreezeBeans);
        recyclerView.setAdapter(freezeAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(8, 16, 8, 16);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreezeMainActivity.this, UnFreezeActivity.class);
                startActivity(intent);
            }
        });
        presenter.fetchFreezeData();
    }

    @Override
    public BasePresenter<IView> createPresenter() {
        return new FreezePresentImp(getApplicationContext());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void fillView(List<FreezeBean> freezeBeans) {
        mFreezeBeans.clear();
        mFreezeBeans.addAll(freezeBeans);
        freezeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
