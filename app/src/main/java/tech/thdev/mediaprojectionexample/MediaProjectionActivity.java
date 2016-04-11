package tech.thdev.mediaprojectionexample;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import butterknife.Bind;
import tech.thdev.mediaprojectionexample.base.BaseActivity;


public class MediaProjectionActivity extends BaseActivity implements SurfaceHolder.Callback {

    public static final int REQUEST_MEDIA_PROJECTION = 100;

    @Bind(R.id.surface_view)
    SurfaceView surfaceView;

    private SurfaceHolder holder;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    @Override
    protected int getContentView() {
        return R.layout.activity_media_projection;
    }

    @Override
    protected void onCreate() {
        holder = surfaceView.getHolder();
        holder.addCallback(this);

        projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "Reject media projection", Toast.LENGTH_SHORT).show();
                    return;
                }

                mediaProjection = projectionManager.getMediaProjection(resultCode, data);
                if (mediaProjection != null) {
                    virtualDisplay = mediaProjection.createVirtualDisplay("TEST", 720, 1280, getDensityDpi(), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, holder.getSurface(), null, null);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getDensityDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }

        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }
}
