package tech.thdev.mediaprojectionexample.view;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.mediaprojectionexample.R;
import tech.thdev.mediaprojectionexample.base.view.BaseMediaProjectionActivity;
import tech.thdev.mediaprojectionexample.constant.Constant;

/**
 * Created by Tae-hwan on 4/25/16.
 * <p/>
 * MediaProjection Access activity
 */
public class TransparentMediaProjectionActivity extends BaseMediaProjectionActivity {

    private static final String TAG = "TransparentActivity";

    private Messenger messenger;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_media_projection_transparent;
    }

    @Override
    protected void onCreate() {
        messenger = (Messenger) getIntent().getExtras().get(Constant.KEY_INTENT_MESSENGER);

        createMediaProjection();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Log.d(TAG, "onNewIntent");
        createMediaProjection();
    }

    /**
     * Start / Stop mediaProjection
     */
    private void createMediaProjection() {
        Surface surface = getIntent().getParcelableExtra(Constant.KEY_INTENT_SURFACE);
        boolean isStarted = getIntent().getBooleanExtra(Constant.KEY_INTENT_IS_STARTED, false);
        Log.d("TAG", "isStarted " + isStarted);

        if (isStarted) {
            stopMediaProjection();

            Log.i(TAG, "StopMediaProjection");

        } else {
            createMediaProjection(MediaProjectionControllerParams.create(surface));

            Log.i(TAG, "createMediaProjection");
        }
    }

    @Override
    public void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type) {
        sendMessage(type);

        moveTaskToBack(true);
    }

    private void sendMessage(int type) {
        Message message = Message.obtain();
        message.what = type;

        try {
            messenger.send(message);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
