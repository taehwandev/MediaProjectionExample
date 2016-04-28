package tech.thdev.mediaprojectionexample.presenter.view;

import tech.thdev.mediaprojectionexample.base.presenter.view.BasePresenterView;

/**
 * Created by Tae-hwan on 4/28/16.
 */
public interface MediaProjectionPresenterView extends BasePresenterView {

    /**
     * Projection stop event
     */
    void onStopMediaProjection();

    /**
     * Projection create event
     */
    void onCreateMediaProjection();

    /**
     * Projection start event
     */
    void onProjectionStarted();

    /**
     * Projection stop event
     */
    void onProjectionStopped();

    /**
     * Projection fail event
     */
    void onProjectionFail();

    /**
     * Projection reject
     */
    void onProjectionReject();
}
