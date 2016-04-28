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
import tech.thdev.mediaprojectionexample.presenter.TransparentMediaProjectionPresenter;
import tech.thdev.mediaprojectionexample.presenter.view.TransparentMediaProjectionPresenterView;

/**
 * Created by Tae-hwan on 4/25/16.
 * <p/>
 * MediaProjection Access activity
 */
public class TransparentMediaProjectionActivity extends BaseMediaProjectionActivity implements TransparentMediaProjectionPresenterView {

    private static final String TAG = "TransparentActivity";

    private Messenger messenger;
    private TransparentMediaProjectionPresenter transparentMediaProjectionPresenter;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_media_projection_transparent;
    }

    @Override
    protected void onCreate() {
        transparentMediaProjectionPresenter = new TransparentMediaProjectionPresenter(this);

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

    private void createMediaProjection() {
        boolean isStarted = getIntent().getBooleanExtra(Constant.KEY_INTENT_IS_STARTED, false);
        transparentMediaProjectionPresenter.createMediaProjection(isStarted);

        Log.d("TAG", "isStarted " + isStarted);
    }

    @Override
    public void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type) {
        transparentMediaProjectionPresenter.sendMessage(type);
        moveTaskToBack(true);
    }

    @Override
    public void onStopMediaProjection() {
        stopMediaProjection();

        Log.i(TAG, "StopMediaProjection");
    }

    @Override
    public void onCreateMediaProjection() {
        Surface surface = getIntent().getParcelableExtra(Constant.KEY_INTENT_SURFACE);
        createMediaProjection(MediaProjectionControllerParams.create(surface));

        Log.i(TAG, "createMediaProjection");
    }

    @Override
    public void onSendMessage(Message message) {
        try {
            messenger.send(message);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
