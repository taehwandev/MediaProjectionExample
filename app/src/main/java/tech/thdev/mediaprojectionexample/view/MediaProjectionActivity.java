package tech.thdev.mediaprojectionexample.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.mediaprojectionexample.R;
import tech.thdev.mediaprojectionexample.base.view.BaseMediaProjectionActivity;
import tech.thdev.mediaprojectionexample.presenter.MediaProjectionPresenter;
import tech.thdev.mediaprojectionexample.presenter.view.MediaProjectionPresenterView;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;


/**
 * Created by Tae-hwan on 4/8/16.
 * <p/>
 * MediaProjection example.
 */
public class MediaProjectionActivity extends BaseMediaProjectionActivity implements MediaProjectionPresenterView {

    @BindView(R.id.texture_view)
    TextureView textureView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private VideoSurfaceTextureListener videoSurfaceTextureListener;
    private MediaProjectionPresenter mediaProjectionPresenter;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_media_projection;
    }

    @Override
    protected void onCreate() {
        mediaProjectionPresenter = new MediaProjectionPresenter(this);
        videoSurfaceTextureListener = new VideoSurfaceTextureListener();
        textureView.setSurfaceTextureListener(videoSurfaceTextureListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_open_source_license) {
            startActivity(new Intent(MediaProjectionActivity.this, OpenSourceLicenseActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onClickFloatingActionButton(View view) {
        mediaProjectionPresenter.startMediaProjection();
    }

    @Override
    public void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type) {
        mediaProjectionPresenter.mediaProjectionAccessStatus(type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopMediaProjection();
    }

    @Override
    public void onStopMediaProjection() {
        stopMediaProjection();
    }

    @Override
    public void onCreateMediaProjection() {
        createMediaProjection(MediaProjectionControllerParams.create(videoSurfaceTextureListener.getSurface()));
    }

    @Override
    public void onProjectionStarted() {
        if (floatingActionButton != null) {
            floatingActionButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    public void onProjectionStopped() {
        if (floatingActionButton != null) {
            floatingActionButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public void onProjectionFail() {
        Snackbar.make(floatingActionButton, R.string.media_projection_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onProjectionReject() {
        Snackbar.make(floatingActionButton, R.string.media_projection_reject, Snackbar.LENGTH_SHORT).show();
    }
}
