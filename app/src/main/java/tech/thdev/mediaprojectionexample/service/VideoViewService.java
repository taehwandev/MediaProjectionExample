package tech.thdev.mediaprojectionexample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;
import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.mediaprojectionexample.R;
import tech.thdev.mediaprojectionexample.constant.Constant;
import tech.thdev.mediaprojectionexample.presenter.VideoViewServicePresenter;
import tech.thdev.mediaprojectionexample.presenter.WindowTouchPresenter;
import tech.thdev.mediaprojectionexample.presenter.view.VideoViewServiceView;
import tech.thdev.mediaprojectionexample.presenter.view.WindowTouchView;
import tech.thdev.mediaprojectionexample.service.handler.VideoViewHandler;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;
import tech.thdev.mediaprojectionexample.view.TransparentMediaProjectionActivity;

/**
 * Created by Tae-hwan on 4/8/16.
 */
public class VideoViewService extends Service implements View.OnClickListener, WindowTouchView, VideoViewServiceView {

    private View windowView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowViewLayoutParams;

    @Bind(R.id.texture_view)
    TextureView textureView;

    @Bind(R.id.btn_stop_service)
    Button btnStopService;

    @Bind(R.id.main_play_pause_button)
    PlayPauseButton playPauseButton;

    private WindowTouchPresenter windowTouchPresenter;

    private VideoSurfaceTextureListener videoSurfaceTextureListener;

    private VideoViewServicePresenter videoViewServicePresenter;
    private VideoViewHandler videoViewHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        initWindowLayout((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        ButterKnife.bind(this, windowView);

        videoViewServicePresenter = new VideoViewServicePresenter(this);
        videoViewHandler = new VideoViewHandler(videoViewServicePresenter);
        windowTouchPresenter = new WindowTouchPresenter(this);

        initCreate();
    }

    private void initCreate() {
        videoSurfaceTextureListener = new VideoSurfaceTextureListener();
        textureView.setSurfaceTextureListener(videoSurfaceTextureListener);
        btnStopService.setOnClickListener(this);

        playPauseButton.setColor(Color.DKGRAY);
        playPauseButton.setOnClickListener(this);
        playPauseButton.setOnControlStatusChangeListener(new PlayPauseButton.OnControlStatusChangeListener() {

            @Override
            public void onStatusChange(View view, boolean state) {
                videoViewServicePresenter.startMediaProjection();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stop_service:
                stopSelf();
                break;

            default:
                break;
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            windowTouchPresenter.onTouch(event);
            return false;
        }
    };

    @Override
    public void updateViewLayout(int x, int y) {
        if (windowViewLayoutParams != null) {
            windowViewLayoutParams.x += x;
            windowViewLayoutParams.y += y;

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
    public void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type) {
        switch (type) {
            case MediaProjectionConstant.PROJECTION_TYPE_STARTED:
                setPlayed(true);
                showToast(R.string.media_projection_started);
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_STOPPED:
                setPlayed(false);
                showToast(R.string.media_projection_stopped);
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_FAIL:
                setPlayed(false);
                showToast(R.string.media_projection_fail);
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_REJECT:
                setPlayed(false);
                showToast(R.string.media_projection_reject);
                break;

            default:
                break;
        }
    }

    /**
     * Play button design change
     */
    private void setPlayed(boolean isPlayed) {
        if (playPauseButton != null) {
            if (playPauseButton.isPlayed() != isPlayed) {
                playPauseButton.setPlayed(isPlayed);
                playPauseButton.startAnimation();
            }
        }
    }

    private void showToast(@StringRes int stringRes) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMediaProjectionCreate(boolean isStarted) {
        Intent intent = new Intent(VideoViewService.this, TransparentMediaProjectionActivity.class);
        intent.putExtra(Constant.KEY_INTENT_MESSENGER, new Messenger(videoViewHandler));
        intent.putExtra(Constant.KEY_INTENT_SURFACE, videoSurfaceTextureListener.getSurface());
        intent.putExtra(Constant.KEY_INTENT_IS_STARTED, isStarted);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        videoViewServicePresenter.stopMediaProjection();

        if (windowView != null) {
            if (windowManager != null) {
                windowManager.removeView(windowView);
            }
            windowView = null;
        }

        ButterKnife.unbind(this);
    }
}
