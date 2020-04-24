package com.example.inheritationdemo;

public class MyClass {

    public static void main(String[] args) {
        new MyClass();
    }

    public MyClass() {
        Base baseImp = new BaseImp();
        baseImp.get();
    }
}
