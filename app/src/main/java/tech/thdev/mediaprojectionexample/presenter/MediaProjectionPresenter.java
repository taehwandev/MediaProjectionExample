package tech.thdev.mediaprojectionexample.presenter;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.mediaprojectionexample.base.presenter.BasePresenter;
import tech.thdev.mediaprojectionexample.presenter.view.MediaProjectionPresenterView;

/**
 * Created by Tae-hwan on 4/28/16.
 */
public class MediaProjectionPresenter extends BasePresenter<MediaProjectionPresenterView> {

    private boolean isStated = false;

    public MediaProjectionPresenter(MediaProjectionPresenterView view) {
        super(view);

        isStated = false;
    }

    public void startMediaProjection() {
        if (isStated) {
            getView().onStopMediaProjection();

        } else {
            getView().onCreateMediaProjection();
        }
    }

    public void mediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type) {
        isStated = false;

        switch (type) {
            case MediaProjectionConstant.PROJECTION_TYPE_STARTED:
                isStated = true;
                getView().onProjectionStarted();
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_STOPPED:
                getView().onProjectionStopped();
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_FAIL:
                getView().onProjectionFail();
                break;

            case MediaProjectionConstant.PROJECTION_TYPE_REJECT:
                getView().onProjectionReject();
                break;

            default:
                break;
        }
    }
}
