package com.luyao.android.demo.download.process;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.luyao.android.demo.download.core.DownloadCallback;
import com.luyao.android.demo.download.download.LuDownload;
import com.luyao.android.demo.download.process.main.DownloadCallbackManager;

/**
 * 下载中心，跨进程下载
 */
public class DownloadCenter {

    private static volatile DownloadCenter mInstance = null;

    static {
        if (mInstance == null) {
            synchronized (DownloadCenter.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCenter();
                }
            }
        }
    }

    private DownloadCenter() { }

    public static DownloadCenter getInstance() {
        return mInstance;
    }

    public void download(String url, Lifecycle lifecycle, DownloadCallback downloadCallback) {
        String id = DownloadCallbackManager.getInstance().addDownloadCallback(downloadCallback);
        Intent intent = new Intent(LuDownload.getInstance().getContext(), DownloadService.class);
        intent.putExtra(DownloadService.URL_KEY, url);
        intent.putExtra(DownloadService.DOWNLOAD_CALLBACK_ID, id);
        DownloadService.enqueueWork(LuDownload.getInstance().getContext(), intent);
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    lifecycle.removeObserver(this);
                    DownloadCallbackManager.getInstance().removeDownloadCallback(id);
                }
            }
        });
    }

    public DownloadDisposable download(String url, DownloadCallback downloadCallback) {
        String id = DownloadCallbackManager.getInstance().addDownloadCallback(downloadCallback);
        Intent intent = new Intent(LuDownload.getInstance().getContext(), DownloadService.class);
        intent.putExtra(DownloadService.URL_KEY, url);
        intent.putExtra(DownloadService.DOWNLOAD_CALLBACK_ID, id);
        DownloadService.enqueueWork(LuDownload.getInstance().getContext(), intent);
        return () -> DownloadCallbackManager.getInstance().removeDownloadCallback(id);
    }
}
