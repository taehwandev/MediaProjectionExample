package tech.thdev.mediaprojectionexample.view;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.TextureView;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.mediaprojectionexample.R;
import tech.thdev.mediaprojectionexample.base.view.BaseMediaProjectionActivity;
import tech.thdev.mediaprojectionexample.surface.VideoSurfaceTextureListener;


/**
 * Created by Tae-hwan on 4/8/16.
 * <p/>
 * MediaProjection example.
 */
public class MediaProjectionActivity extends BaseMediaProjectionActivity {

    @BindView(R.id.texture_view)
    TextureView textureView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private VideoSurfaceTextureListener videoSurfaceTextureListener;
    private boolean isStated = false;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_media_projection;
    }

    @Override
    protected void onCreate() {
        isStated = false;

        videoSurfaceTextureListener = new VideoSurfaceTextureListener();
        textureView.setSurfaceTextureListener(videoSurfaceTextureListener);
    }

    @OnClick(R.id.fab)
    public void onClickFloatingActionButton(View view) {
        if (isStated) {
            stopMediaProjection();

        } else {
            createMediaProjection(MediaProjectionControllerParams.create(videoSurfaceTextureListener.getSurface()));
        }
    }

    @Override
    public void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type) {
        isStated = false;
        switch (type) {
            case MediaProjectionConstant.PROJECTION_TYPE_STARTED:
                isStated = true;

                if (floatingActionButton != null) {
                    floatingActionButton.setImageResource(android.R.drawable.ic_media_pause);
                }
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_STOPPED:
                if (floatingActionButton != null) {
                    floatingActionButton.setImageResource(android.R.drawable.ic_media_play);
                }
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_FAIL:
                Snackbar.make(floatingActionButton, R.string.media_projection_fail, Snackbar.LENGTH_SHORT).show();
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_REJECT:
                Snackbar.make(floatingActionButton, R.string.media_projection_reject, Snackbar.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopMediaProjection();
    }
}
