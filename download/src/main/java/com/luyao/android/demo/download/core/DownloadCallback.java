package com.luyao.android.demo.download.core;

public interface DownloadCallback {
    void onStart();

    void onDownload(float progress);

    void onSuccess(String resultPath);

    void onError(Throwable r);
}
