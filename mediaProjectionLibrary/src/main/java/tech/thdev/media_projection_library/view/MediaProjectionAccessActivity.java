package tech.thdev.media_projection_library.view;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tech.thdev.media_projection_library.R;
import tech.thdev.media_projection_library.constant.Constant;
import tech.thdev.media_projection_library.listener.OnMediaProjectionAccessListener;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.media_projection_library.presenter.MediaProjectionPresenter;
import tech.thdev.media_projection_library.presenter.view.MediaProjectionView;
import tech.thdev.media_projection_library.util.DisplayUtil;

public class MediaProjectionAccessActivity extends AppCompatActivity implements MediaProjectionView {

    private static final int REQ_CODE_MEDIA_PROJECTION = 1000;

    private MediaProjectionPresenter mediaProjectionPresenter;
    private MediaProjectionControllerParams controllerParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_projection_access);

        controllerParams = (MediaProjectionControllerParams) getIntent().getParcelableExtra(Constant.KEY_INTENT_CONTROL_PARAMS);
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mediaProjectionPresenter = new MediaProjectionPresenter(this, mediaProjectionManager, controllerParams);

        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQ_CODE_MEDIA_PROJECTION);
    }

    @Override
    public void startedMediaProjection() {
        setType(OnMediaProjectionAccessListener.TYPE_SUCCESS);
    }

    @Override
    public void rejectMediaProjection() {
        setType(OnMediaProjectionAccessListener.TYPE_REJECT);
    }

    @Override
    public void failMediaProjection() {
        setType(OnMediaProjectionAccessListener.TYPE_FAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_MEDIA_PROJECTION:
                mediaProjectionPresenter.initMediaProjection(resultCode, data, DisplayUtil.getDensityDpi(this));
                return;
        }
//        setType(MediaProjectionControllerParams.TYPE_FAIL);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setType(int type) {
        Log.d("TAG", "getMediaProjectionListener " + controllerParams.getMediaProjectionListener());
        if (controllerParams.getMediaProjectionListener() != null) {
            controllerParams.getMediaProjectionListener().onMediaProjectionEvent(type);
        }
        finish();
    }
}
