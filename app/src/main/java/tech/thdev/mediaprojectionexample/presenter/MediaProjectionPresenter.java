package tech.thdev.mediaprojectionexample.presenter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;

import tech.thdev.mediaprojectionexample.presenter.view.MediaProjectionView;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public class MediaProjectionPresenter extends BasePresenter<MediaProjectionView> {

    public static final int SIZE_WIDTH = 1080;
    public static final int SIZE_HEIGHT = 1920;
    public static final String MEDIA_PROJECTION_NAME = "media-projection-example";

    private boolean isStart = false;

    private VideoSurfaceTextureListener videoSurfaceTextureListener;
    private MediaProjectionManager mediaProjectionManager;

    /**
     * MediaProjection
     */
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    public MediaProjectionPresenter(MediaProjectionView view,
                                    MediaProjectionManager mediaProjectionManager,
                                    VideoSurfaceTextureListener videoSurfaceTextureListener) {
        super(view);

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
     * MediaProjection init
     */
    public void initMediaProjection(int resourceCode, Intent data, int densityDpi) {
        initMediaProjection(resourceCode, data, SIZE_WIDTH, SIZE_HEIGHT, densityDpi);
    }

    /**
     * MediaProjection init
     */
    public void initMediaProjection(int resultCode, Intent data, int width, int height, int densityDpi) {
        if (resultCode != Activity.RESULT_OK) {
            getView().failMediaProjection();
            return;
        }

        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection != null) {
            virtualDisplay = mediaProjection.createVirtualDisplay(MEDIA_PROJECTION_NAME,
                    width, height, densityDpi,
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
