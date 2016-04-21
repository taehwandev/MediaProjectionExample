package tech.thdev.mediaprojectionexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import tech.thdev.mediaprojectionexample.base.BaseActivity;
import tech.thdev.mediaprojectionexample.presenter.view.MediaProjectionView;
import tech.thdev.mediaprojectionexample.service.VideoViewService;
import tech.thdev.mediaprojectionexample.util.DisplayUtil;

/**
 * MediaProjection Sample main
 *
 * Created by Tae-hwan on 4/8/16.
 */
public class MainActivity extends BaseActivity implements MediaProjectionView {

    public static final int REQ_CODE_MEDIA_PROJECTION = 1000;
    public static final int REQ_CODE_OVERLAY_PERMISSION = 9999;

    private static final int SCREEN_WIDTH = 1080;
    private static final int SCREEN_HEIGHT = 1920;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unbind from the service
        try {
            if (isBound) {
                unbindService(serviceConnection);
                isBound = false;
            }
        } catch (IllegalArgumentException e) {
            // Do noting...

            // Example Exception.
        }
    }

    @OnClick(R.id.btn_start_media_projection_service)
    public void onClickStartMediaProjection(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // User rights acquired
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);

        } else {
            startVideoViewService();
        }
    }

    @OnClick(R.id.btn_start_media_projection_activity)
    public void onClickStartMediaProjectionActivity(View view) {
        startActivity(new Intent(this, MediaProjectionActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_OVERLAY_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(this)) {
                        startVideoViewService();
                    }
                }
                break;

            case REQ_CODE_MEDIA_PROJECTION:
                if (resultCode != RESULT_OK) {
                    Snackbar.make(floatingActionButton, "Reject media projection", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                if (mediaProjection != null && isBound) {
                    mediaProjection.registerCallback(mediaProjectionCallback, null);
                    virtualDisplay = mediaProjection.createVirtualDisplay("MediaProjection", SCREEN_WIDTH, SCREEN_HEIGHT, DisplayUtil.getDensityDpi(this),
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, videoViewService.getSurface(), null, null);

                    videoViewService.setMediaProjectionStatus(true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void startVideoViewService() {
        // Bind to VideoViewService
        Intent intent = new Intent(this, VideoViewService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * MediaProjection acquired rights
     */
    private void startMediaProjection() {
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQ_CODE_MEDIA_PROJECTION);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            VideoViewService.VideoViewBinder binder = (VideoViewService.VideoViewBinder) service;
            videoViewService = binder.getService();
            videoViewService.setVideoViewListener(videoViewListener);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    /**
     * MediaProjection.Callback
     */
    private MediaProjection.Callback mediaProjectionCallback = new MediaProjection.Callback() {

        @Override
        public void onStop() {
            super.onStop();

            stop();
        }
    };

    @Override
    public void startedMediaProjection() {

    }

    @Override
    public void initMediaProjection() {

    }

    @Override
    public void stopMediaProjection() {
        stop();
    }

    @Override
    public void failMediaProjection() {

    }

    private void stop() {
        if (!isFinishing()) {
            Snackbar.make(floatingActionButton, "MediaProjection stop", Snackbar.LENGTH_SHORT).show();
        }
    }
}
