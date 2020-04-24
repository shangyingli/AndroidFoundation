// IOpRecordService.aidl
package com.example.recordactivity.service;

import com.example.recordactivity.service.IRecorderServiceCallback;

// Declare any non-default types here with import statements

interface IOpRecordService {

    void startRecord(long size);
    void stopRecorder();
    void pauseRecorder();
    int getState();
    int setState(int state);
    long getProgress();

    String getRecordName();
    String getSampleFilePath();
    boolean isRecording();

    void playRecord(String path);
    void pauseOrResum();
    boolean isPlaying();
    long getCurrentPosition();
    long getDuration();
    void seekPlay(long pos);
    int getPlayState();

    void registerCallback(IRecorderServiceCallback callback);
    void unregisterRecorderCallback(IRecorderServiceCallback callback);
}
