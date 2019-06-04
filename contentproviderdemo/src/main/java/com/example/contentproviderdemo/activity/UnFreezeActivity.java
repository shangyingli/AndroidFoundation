package com.example.contentproviderdemo.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.contentproviderdemo.R;
import com.example.contentproviderdemo.adapter.FreezeAdapter;
import com.example.contentproviderdemo.bean.FreezeBean;
import com.example.contentproviderdemo.present.BasePresenter;
import com.example.contentproviderdemo.present.FreezePresentImp;
import com.example.contentproviderdemo.view.IView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UnFreezeActivity extends BaseActivity<IView, BasePresenter<IView>> implements IView, FreezeAdapter.OnItemClickListener {

    private Button freezeBtn;
    private FreezeAdapter unfreezeAdapter;
    private List<FreezeBean> mUnFreezeBeans = new ArrayList<>();
    private List<String> selectedList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.view_freeze_grid);
        freezeBtn = findViewById(R.id.jump);
        freezeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String pkgName : selectedList) {
                    if (presenter.freezeApps(pkgName)) {
                        selectedList.remove(pkgName);
                        int position = getFreezeBean(pkgName);
                        mUnFreezeBeans.remove(position);
                        unfreezeAdapter.notifyItemRemoved(position);
                    }
                }
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        unfreezeAdapter = new FreezeAdapter(mUnFreezeBeans);
        unfreezeAdapter.setItemClickListener(this);
        recyclerView.setAdapter(unfreezeAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(8, 16, 8, 16);
            }
        });
        presenter.fetchUnFreezeData();

    }

    private int getFreezeBean(String pkgName) {
        for (int i = 0; i < mUnFreezeBeans.size(); i++) {
            if (pkgName.equalsIgnoreCase(mUnFreezeBeans.get(i).getPkgName())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public BasePresenter createPresenter() {
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
        mUnFreezeBeans.clear();
        mUnFreezeBeans.addAll(freezeBeans);
        unfreezeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position) {
        String pkgName = mUnFreezeBeans.get(position).getPkgName();
        ImageView notifyIcon = (ImageView) view.getTag();
        if (selectedList.contains(pkgName)) {
            notifyIcon.setImageResource(R.drawable.unclick);
            selectedList.remove(pkgName);
        } else {
            notifyIcon.setImageResource(R.drawable.clicked);
            selectedList.add(pkgName);
        }
    }
}
