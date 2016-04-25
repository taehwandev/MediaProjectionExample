package tech.thdev.media_projection_library.presenter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.media_projection_library.presenter.view.MediaProjectionAccessView;
import tech.thdev.media_projection_library.util.DisplayUtil;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public class MediaProjectionAccessPresenter {

    private MediaProjectionAccessView view;
    private Activity activity;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjectionControllerParams controllerParams;

    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    public MediaProjectionAccessPresenter(Activity activity, MediaProjectionAccessView view,
                                          MediaProjectionManager mediaProjectionManager) {
        this.view = view;
        this.activity = activity;
        this.mediaProjectionManager = mediaProjectionManager;
    }

    public void setControllerParams(MediaProjectionControllerParams controllerParams) {
        this.controllerParams = controllerParams;
    }

    public void initMediaProjection(int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            view.onMediaProjectionAccessStatus(MediaProjectionConstant.PROJECTION_TYPE_REJECT);
            return;
        }

        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection != null) {
            virtualDisplay = mediaProjection.createVirtualDisplay(controllerParams.getProjectionName(),
                    controllerParams.getWidth(),
                    controllerParams.getHeight(),
                    DisplayUtil.getDensityDpi(activity),
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    controllerParams.getSurface(),
                    null,
                    null);

            view.onMediaProjectionAccessStatus(MediaProjectionConstant.PROJECTION_TYPE_STARTED);

        } else {
            view.onMediaProjectionAccessStatus(MediaProjectionConstant.PROJECTION_TYPE_FAIL);
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

        view.onMediaProjectionAccessStatus(MediaProjectionConstant.PROJECTION_TYPE_STOPPED);
    }
}
