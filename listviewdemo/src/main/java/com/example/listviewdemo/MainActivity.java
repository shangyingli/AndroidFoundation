package com.example.listviewdemo;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private CatAdapter catAdapter;
    private List<Drawable> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Drawable d1 = getResources().getDrawable(R.drawable.app1);
        Drawable d2 = getResources().getDrawable(R.drawable.app2);
        Drawable d3 = getResources().getDrawable(R.drawable.app3);
        images = new ArrayList<>();
        images.add(d1);
        images.add(d2);
        images.add(d3);
        listView = findViewById(R.id.list_view);
        catAdapter = new CatAdapter(images);
        listView.setAdapter(catAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "item position : " + position);
                Drawable drawable = (Drawable) parent.getItemAtPosition(position);
            }
        });
    }
}
