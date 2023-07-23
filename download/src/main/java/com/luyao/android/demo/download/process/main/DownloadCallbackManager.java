package com.luyao.android.demo.download.process.main;

import com.luyao.android.demo.download.core.DownloadCallback;
import com.luyao.android.demo.download.util.DownloadUtils;

import java.util.HashMap;
import java.util.Map;

public class DownloadCallbackManager {

    private static volatile DownloadCallbackManager mInstance = null;

    private final Map<String, DownloadCallback> mDownloadCallbacks = new HashMap<>();

    static {
        if (mInstance == null) {
            synchronized (DownloadCallbackManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCallbackManager();
                }
            }
        }
    }

    private DownloadCallbackManager() { }

    public static DownloadCallbackManager getInstance() {
        return mInstance;
    }

    public String addDownloadCallback(DownloadCallback downloadCallback) {
        String callbackId = DownloadUtils.makeUUID();
        mDownloadCallbacks.put(callbackId, downloadCallback);
        return callbackId;
    }

    public DownloadCallback getDownloadCallback(String id) {
        return mDownloadCallbacks.get(id);
    }

    public void removeDownloadCallback(String id) {
        mDownloadCallbacks.remove(id);
    }
}
