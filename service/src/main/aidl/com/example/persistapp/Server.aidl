// Server.aidl
package com.example.persistapp;

// Declare any non-default types here with import statements

interface Server {

    int getPid();

    void setBinder(IBinder binder);
}
