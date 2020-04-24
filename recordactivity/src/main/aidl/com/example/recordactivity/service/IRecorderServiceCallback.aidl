// IRecorderServiceCallback.aidl
package com.example.recordactivity.service;

// Declare any non-default types here with import statements

interface IRecorderServiceCallback {

 	void onStateChanged(int state);
 	void onError(int error);
 	void onPlayChanged(int state);
 	void onWaveChanged(out float[] waves,int availableWave);
}
