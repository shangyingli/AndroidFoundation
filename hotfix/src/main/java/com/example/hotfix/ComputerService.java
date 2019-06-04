package com.example.hotfix;

import android.content.Context;
import android.widget.Toast;

public class ComputerService {

    public void compute(Context context) {
        int i = 10;
        int j = 0;
        Toast.makeText(context, "计算结果是 : " + (i / j), Toast.LENGTH_LONG).show();
    }
}
