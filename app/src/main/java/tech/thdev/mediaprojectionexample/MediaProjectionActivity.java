package tech.thdev.mediaprojectionexample;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.TextureView;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import tech.thdev.mediaprojectionexample.base.BaseActivity;
import tech.thdev.mediaprojectionexample.presenter.MediaProjectionPresenter;
import tech.thdev.mediaprojectionexample.presenter.view.MediaProjectionView;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;
import tech.thdev.mediaprojectionexample.util.DisplayUtil;


/**
 * Created by Tae-hwan on 4/8/16.
 * <p/>
 * MediaProjection example.
 */
public class MediaProjectionActivity extends BaseActivity implements MediaProjectionView {

    private static final int REQUEST_MEDIA_PROJECTION = 100;

    @Bind(R.id.texture_view)
    TextureView textureView;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private MediaProjectionPresenter mediaProjectionPresenter;

    /**
     * MediaProjection
     */
    private MediaProjectionManager projectionManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_media_projection_access;
    }

    @Override
    protected void onCreate() {
        projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        VideoSurfaceTextureListener videoSurfaceTextureListener = new VideoSurfaceTextureListener();
        textureView.setSurfaceTextureListener(videoSurfaceTextureListener);

        mediaProjectionPresenter = new MediaProjectionPresenter(this, projectionManager, videoSurfaceTextureListener);
    }

    @OnClick(R.id.fab)
    public void onClickFloatingActionButton(View view) {
        mediaProjectionPresenter.onMediaProjection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                mediaProjectionPresenter.initMediaProjection(resultCode, data, DisplayUtil.getDensityDpi(this));
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaProjectionPresenter.destroyMediaProjection();
    }

    @Override
    public void initMediaProjection() {
        startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }

    @Override
    public void startedMediaProjection() {
        floatingActionButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public void stopMediaProjection() {
        mediaProjectionPresenter.destroyMediaProjection();
        floatingActionButton.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void failMediaProjection() {
        Snackbar.make(floatingActionButton, R.string.media_projection_reject, Snackbar.LENGTH_SHORT).show();
    }
}
