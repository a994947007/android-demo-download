package com.luyao.android.demo.download.download;

import android.content.Context;

public class LuDownload {

    private Context context;
    private DownloadService service;

    private LuDownload() {}

    private static class Instance {
        private static final LuDownload instance = new LuDownload();
    }

    public static LuDownload getInstance() {
        return Instance.instance;
    }

    public void init(Context context, DownloadService downloadService) {
        this.context = context;
        this.service = downloadService;
    }

    public Context getContext() {
        return this.context;
    }

    public DownloadService getDownloadService() {
        return service;
    }
}
