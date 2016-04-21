package tech.thdev.mediaprojectionexample.presenter.view;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public interface MediaProjectionView extends BaseView {

    void startedMediaProjection();

    void initMediaProjection();

    void stopMediaProjection();

    void failMediaProjection();
}
