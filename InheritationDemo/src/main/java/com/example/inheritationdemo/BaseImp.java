package com.example.inheritationdemo;

public class BaseImp extends Base {

    @Override
    protected void getMe() {
//        super.getMe();
        System.out.println("BaseImp getMe " + this);
    }
}
