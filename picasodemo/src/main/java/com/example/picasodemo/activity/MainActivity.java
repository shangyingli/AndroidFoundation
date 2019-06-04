package com.example.picasodemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picasodemo.R;
import com.example.picasodemo.adapter.NormalItemDecoration;
import com.example.picasodemo.adapter.NormalRecycleAdapter;
import com.example.picasodemo.adapter.SimpleItemTouchHelper;
import com.example.picasodemo.bean.CatBean;
import com.example.picasodemo.presenter.CatsPresent;
import com.example.picasodemo.presenter.CatsPresenterImp;
import com.example.picasodemo.view.BaseView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity<BaseView, CatsPresent<BaseView>> implements BaseView, NormalRecycleAdapter.OnItemClickListener {

    private ProgressDialog progressDialog;
    private List<CatBean> responseData = new ArrayList<>();
    private NormalRecycleAdapter adapter;
    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.normal_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NormalItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NormalRecycleAdapter(getApplicationContext(), responseData);
        adapter.setOnItemClickListener(this);
        SimpleItemTouchHelper simpleItemTouchHelper = new SimpleItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
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
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void fill(List<CatBean> catBeans) {
        Log.d(TAG, catBeans.toString());
        responseData.clear();
        responseData.addAll(catBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item :
                adapter.addData(1);
                break;
            case R.id.remove_item:
                adapter.removeData(1);
                break;
            case R.id.jump_activity :
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "position : " + position, Toast.LENGTH_LONG).show();
    }
}
