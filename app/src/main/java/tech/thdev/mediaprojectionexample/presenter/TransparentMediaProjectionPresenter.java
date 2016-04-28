package tech.thdev.mediaprojectionexample.presenter;

import android.os.Message;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.mediaprojectionexample.base.presenter.BasePresenter;
import tech.thdev.mediaprojectionexample.presenter.view.TransparentMediaProjectionPresenterView;

/**
 * Created by Tae-hwan on 4/28/16.
 */
public class TransparentMediaProjectionPresenter extends BasePresenter<TransparentMediaProjectionPresenterView> {

    public TransparentMediaProjectionPresenter(TransparentMediaProjectionPresenterView view) {
        super(view);
    }

    public void createMediaProjection(boolean isStarted) {
        if (isStarted) {
            getView().onStopMediaProjection();

        } else {
            getView().onCreateMediaProjection();
        }
    }

    public void sendMessage(@MediaProjectionConstant.ProjectionStatusType int type) {
        Message message = Message.obtain();
        message.what = type;
        getView().onSendMessage(message);
    }
}
