package tech.thdev.media_projection_library.view;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.media_projection_library.presenter.MediaProjectionAccessPresenter;
import tech.thdev.media_projection_library.presenter.view.MediaProjectionAccessView;

/**
 * Created by Tae-hwan on 4/25/16.
 * <p/>
 * MediaProjection Abstract
 */
public abstract class MediaProjectionAccessActivity extends AppCompatActivity implements MediaProjectionAccessView {

    public static final int REQ_CODE_MEDIA_PROJECTION = 1000;

    private MediaProjectionAccessPresenter mediaProjectionAccessPresenter;
    private MediaProjectionManager mediaProjectionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mediaProjectionAccessPresenter = new MediaProjectionAccessPresenter(this, this, mediaProjectionManager);
    }

    /**
     * Projection start
     *
     * @param controllerParams
     */
    public void createMediaProjection(MediaProjectionControllerParams controllerParams) {
        mediaProjectionAccessPresenter.setControllerParams(controllerParams);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQ_CODE_MEDIA_PROJECTION);
    }

    /**
     * MediaProjection stop.
     */
    public void stopMediaProjection() {
        mediaProjectionAccessPresenter.destroyMediaProjection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_MEDIA_PROJECTION:
                mediaProjectionAccessPresenter.initMediaProjection(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
