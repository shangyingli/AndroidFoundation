package com.example.hotfix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void compute(View view) {
        ComputerService computerService = new ComputerService();
        computerService.compute(this);
    }

    public void fix(View view) {
        FixDexUtils.loadFixedDex(this);
    }
}
