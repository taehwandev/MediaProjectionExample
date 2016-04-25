package tech.thdev.mediaprojectionexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import tech.thdev.mediaprojectionexample.base.view.BaseActivity;
import tech.thdev.mediaprojectionexample.service.VideoViewService;

/**
 * MediaProjection Sample main
 * <p/>
 * Created by Tae-hwan on 4/8/16.
 */
public class MainActivity extends BaseActivity {

    public static final int REQ_CODE_OVERLAY_PERMISSION = 9999;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Override
    protected int getContentLayoutResource() {
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

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startVideoViewService() {
        startService(new Intent(this, VideoViewService.class));
    }
}
