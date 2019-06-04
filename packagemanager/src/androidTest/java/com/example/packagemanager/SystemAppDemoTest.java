package com.example.packagemanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SystemAppDemoTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void getNoneSystemApp() {
        SystemAppDemo appDemo = new SystemAppDemo();
        List<String> al = appDemo.getNoneSystemApp(context);
        assert al.size() != 0;
        assertNotNull(al);
        assertNotEquals(0, al.size());
    }
}