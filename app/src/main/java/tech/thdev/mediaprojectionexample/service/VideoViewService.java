package tech.thdev.mediaprojectionexample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;
import tech.thdev.mediaprojectionexample.R;
import tech.thdev.mediaprojectionexample.listener.IVideoViewListener;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;

/**
 * Created by Tae-hwan on 4/8/16.
 */
public class VideoViewService extends Service implements View.OnClickListener {

    private View windowView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowViewLayoutParams;

    private float touchPrevX;
    private float touchPrevY;

    private VideoSurfaceTextureListener videoSurfaceTextureListener;
    private boolean isStart = false;

    private final IBinder binder = new VideoViewBinder();
    private IVideoViewListener videoViewListener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setVideoViewListener(IVideoViewListener videoViewListener) {
        this.videoViewListener = videoViewListener;
    }

    private void onStartMediaProjection() {
        if (videoViewListener != null) {
            videoViewListener.onStartProjection();
        }
    }

    private void onStopMediaProjection() {
        if (videoViewListener != null) {
            videoViewListener.onStopProjection();
        }
    }

    /**
     * VideoView service stop
     */
    private void onDestroyService() {
        if (videoViewListener != null) {
            videoViewListener.onDestroyService();
        }
    }

    @Nullable
    public Surface getSurface() {
        return videoSurfaceTextureListener != null ? videoSurfaceTextureListener.getSurface() : null;
    }

    /**
     * Set Media Projection status
     *
     * @param isStart started - true / stop - false
     */
    public void setMediaProjectionStatus(boolean isStart) {
        this.isStart = isStart;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        initWindowLayout((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        initCreate();
    }

    private void initCreate() {
        videoSurfaceTextureListener = new VideoSurfaceTextureListener();
        final TextureView textureView = (TextureView) windowView.findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(videoSurfaceTextureListener);

        windowView.findViewById(R.id.btn_stop_service).setOnClickListener(this);

        PlayPauseButton playPauseButton = (PlayPauseButton) windowView.findViewById(R.id.main_play_pause_button);
        playPauseButton.setColor(Color.DKGRAY);
        playPauseButton.setOnClickListener(this);
        playPauseButton.setOnControlStatusChangeListener(new PlayPauseButton.OnControlStatusChangeListener() {
            @Override
            public void onStatusChange(View view, boolean state) {
                if (isStart) {
                    onStopMediaProjection();
                } else {
                    onStartMediaProjection();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stop_service:
                onDestroyService();
                break;

            default:
                break;
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchPrevX = event.getRawX();
                    touchPrevY = event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();

                    // 이동한 위치에서 처음 위치를 빼서 이동한 거리를 구한다
                    float x = rawX - touchPrevX;
                    float y = rawY - touchPrevY;

                    setCoordinateUpdate(x, y);

                    touchPrevX = rawX;
                    touchPrevY = rawY;
                    break;

                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    };

    private void setCoordinateUpdate(float x, float y) {
        if (windowViewLayoutParams != null) {
            windowViewLayoutParams.x += (int) x;
            windowViewLayoutParams.y += (int) y;

            windowManager.updateViewLayout(windowView, windowViewLayoutParams);
        }
    }

    /**
     * Window View 를 초기화 한다. X, Y 좌표는 0, 0으로 지정한다.
     */
    private void initWindowLayout(LayoutInflater layoutInflater) {
        windowView = layoutInflater.inflate(R.layout.window_video_view, null);
        windowViewLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                30, 30, // X, Y 좌표
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        windowViewLayoutParams.gravity = Gravity.TOP | Gravity.START;
        windowManager.addView(windowView, windowViewLayoutParams);
        windowView.setOnTouchListener(touchListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (windowView != null) {
            if (windowManager != null) {
                windowManager.removeView(windowView);
            }
            windowView = null;
        }

        onStopMediaProjection();
    }

    public class VideoViewBinder extends Binder {
        public VideoViewService getService() {
            return VideoViewService.this;
        }
    }
}
