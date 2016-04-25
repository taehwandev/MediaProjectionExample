package tech.thdev.mediaprojectionexample.service.handler;

import android.os.Handler;
import android.os.Message;

import tech.thdev.mediaprojectionexample.presenter.VideoViewServicePresenter;

/**
 * Created by Tae-hwan on 4/25/16.
 */
public class VideoViewHandler extends Handler {

    private VideoViewServicePresenter videoViewServicePresenter;

    public VideoViewHandler(VideoViewServicePresenter videoViewServicePresenter) {
        this.videoViewServicePresenter = videoViewServicePresenter;
    }

    @Override
    public void handleMessage(Message msg) {
        videoViewServicePresenter.projectionStatus(msg.what);
    }
}
