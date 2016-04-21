package tech.thdev.mediaprojectionexample.presenter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;

import tech.thdev.mediaprojectionexample.presenter.view.MediaProjectionView;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;
import tech.thdev.mediaprojectionexample.util.DisplayUtil;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public class MediaProjectionPresenter extends BasePresenter<MediaProjectionView> {

    public static final int SIZE_WIDTH = 1080;
    public static final int SIZE_HEIGHT = 1920;
    public static final String MEDIA_PROJECTION_NAME = "media-projection-example";

    private boolean isStart = false;

    private Activity activity;
    private VideoSurfaceTextureListener videoSurfaceTextureListener;
    private MediaProjectionManager mediaProjectionManager;

    /**
     * MediaProjection
     */
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    public MediaProjectionPresenter(MediaProjectionView view,
                                    Activity activity,
                                    MediaProjectionManager mediaProjectionManager,
                                    VideoSurfaceTextureListener videoSurfaceTextureListener) {
        super(view);

        this.activity = activity;
        this.videoSurfaceTextureListener = videoSurfaceTextureListener;
        this.mediaProjectionManager = mediaProjectionManager;

        isStart = false;
    }

    /**
     * Start/Stop mediaProjection control
     */
    public void onMediaProjection() {
        if (isStart) {
            getView().stopMediaProjection();

        } else {
            getView().initMediaProjection();
        }
    }

    /**
     * MediaProjection start
     */
    public void initMediaProjection(int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            getView().failMediaProjection();
            return;
        }

        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection != null) {
            virtualDisplay = mediaProjection.createVirtualDisplay(MEDIA_PROJECTION_NAME,
                    SIZE_WIDTH, SIZE_HEIGHT, DisplayUtil.getDensityDpi(activity),
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    videoSurfaceTextureListener.getSurface(), null, null);

            isStart = true;

            getView().startedMediaProjection();

        } else {
            getView().failMediaProjection();
        }
    }

    /**
     * MediaProjection release
     */
    public void destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }

        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }

        isStart = false;
    }
}
