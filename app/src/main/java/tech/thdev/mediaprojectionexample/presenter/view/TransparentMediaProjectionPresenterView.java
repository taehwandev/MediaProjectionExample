package tech.thdev.mediaprojectionexample.presenter.view;

import android.os.Message;

import tech.thdev.mediaprojectionexample.base.presenter.view.BasePresenterView;

/**
 * Created by Tae-hwan on 4/28/16.
 */
public interface TransparentMediaProjectionPresenterView extends BasePresenterView {

    void onStopMediaProjection();

    void onCreateMediaProjection();

    void onSendMessage(Message message);
}
