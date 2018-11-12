package com.tory.servicetest;

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onPause();
    void onFailed();
    void onCancelled();

}
