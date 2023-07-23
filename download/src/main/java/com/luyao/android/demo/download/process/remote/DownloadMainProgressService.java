package com.luyao.android.demo.download.process.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.luyao.android.demo.download.process.main.DownloadCallbackServerImpl;

/**
 * 下载进程连接主进程的Service，该Service在主进程
 */
public class DownloadMainProgressService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return DownloadCallbackServerImpl.getInstance();
    }
}
