package com.example.inheritationdemo;

public class Base {

    final void get() {
        System.out.println("Base get" + this);
        getMe();
        String a = "aa ";
        a.length();
    }

    protected void getMe() {
        System.out.println("Base getMe" + this);
    }

}
