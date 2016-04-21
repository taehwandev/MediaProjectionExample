package tech.thdev.mediaprojectionexample.service.connection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import tech.thdev.mediaprojectionexample.service.listener.IVideoViewListener;
import tech.thdev.mediaprojectionexample.service.VideoViewService;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public class VideoServiceConnection implements ServiceConnection {

    private VideoViewService videoViewService;
    private boolean isBound = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        VideoViewService.VideoViewBinder binder = (VideoViewService.VideoViewBinder) service;
        videoViewService = binder.getService();
        videoViewService.setVideoViewListener(videoViewListener);
        isBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private IVideoViewListener videoViewListener = new IVideoViewListener() {

        @Override
        public void onStartProjection() {
            startMediaProjection();
        }

        @Override
        public void onStopProjection() {
            if (mediaProjection != null) {
                mediaProjection.stop();
                mediaProjection = null;
            }

            if (virtualDisplay != null) {
                virtualDisplay.release();
                virtualDisplay = null;
            }

            videoViewService.setMediaProjectionStatus(false);
        }

        @Override
        public void onDestroyService() {
            unbindService(serviceConnection);
        }
    };
}
