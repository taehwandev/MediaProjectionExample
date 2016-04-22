package tech.thdev.media_projection_library.presenter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.Log;

import tech.thdev.media_projection_library.base.presenter.BasePresenter;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.media_projection_library.presenter.view.MediaProjectionView;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public class MediaProjectionPresenter extends BasePresenter<MediaProjectionView> {

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjectionControllerParams controllerParams;

    public MediaProjectionPresenter(MediaProjectionView view,
                                    MediaProjectionManager mediaProjectionManager,
                                    MediaProjectionControllerParams controllerParams) {
        super(view);

        this.mediaProjectionManager = mediaProjectionManager;
        this.controllerParams = controllerParams;
    }

    /**
     * MediaProjection init
     */
    public void initMediaProjection(int resultCode, Intent data, int densityDpi) {
        if (resultCode != Activity.RESULT_OK) {
            getView().rejectMediaProjection();
            return;
        }

        MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        Log.d("TAG", "width " + controllerParams.getWidth() + ", " + controllerParams.getHeight());
        if (mediaProjection != null) {
            VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay(controllerParams.getProjectionName(),
                    controllerParams.getWidth(),
                    controllerParams.getHeight(),
                    densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    controllerParams.getSurface(),
                    null,
                    null);

//            controllerParams.setMediaProjection(mediaProjection);
//            controllerParams.setVirtualDisplay(virtualDisplay);
            getView().startedMediaProjection();

        } else {
            getView().failMediaProjection();
        }
    }
}
