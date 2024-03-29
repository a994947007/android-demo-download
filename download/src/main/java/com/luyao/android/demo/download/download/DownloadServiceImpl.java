package com.luyao.android.demo.download.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.Lifecycle;

import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.schedule.Schedules;
import com.luyao.android.demo.download.core.DownloadCallback;
import com.luyao.android.demo.download.process.DownloadCenter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadServiceImpl implements DownloadService {
    @Override
    public Bitmap downloadImage(URL url) {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Observable<Bitmap> downloadImageObservable(URL url) {
        return Observable.just(url)
                .map(URL::openStream)
                .map(inputStream -> {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bitmap;
                })
                .subscribeOn(Schedules.IO);
    }

    @Override
    public void downloadFile(Lifecycle lifecycle, String url, DownloadCallback downloadCallback) {
        DownloadCenter.getInstance().download(url, lifecycle, new DownloadCallback() {
            @Override
            public void onStart() {
                downloadCallback.onStart();
            }

            @Override
            public void onDownload(float progress) {
                downloadCallback.onDownload(progress);
            }

            @Override
            public void onSuccess(String resultPath) {
                downloadCallback.onSuccess(resultPath);
            }

            @Override
            public void onError(Throwable r) {
                downloadCallback.onError(r);
            }
        });
    }

    @Override
    public Observable<String> observeDownloadFile(Lifecycle lifecycle, String url) {
        return Observable.create(subscriber -> DownloadCenter.getInstance().download(url, lifecycle,
                new DownloadCallback() {
            @Override
            public void onStart() { }

            @Override
            public void onDownload(float progress) { }

            @Override
            public void onSuccess(String resultPath) {
                try {
                    subscriber.onNext(resultPath);
                    subscriber.onComplete();
                } catch (Throwable r) {
                    subscriber.onError(r);
                }
            }

            @Override
            public void onError(Throwable r) {
                subscriber.onError(r);
            }
        }));
    }
}
