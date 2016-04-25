package tech.thdev.mediaprojectionexample.presenter;

import android.util.Log;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.mediaprojectionexample.base.presenter.BasePresenter;
import tech.thdev.mediaprojectionexample.presenter.view.VideoViewServiceView;

/**
 * Created by Tae-hwan on 4/25/16.
 */
public class VideoViewServicePresenter extends BasePresenter<VideoViewServiceView> {

    private boolean isStarted = false;

    public VideoViewServicePresenter(VideoViewServiceView view) {
        super(view);

        isStarted = false;
    }

    /**
     * MediaProjection create
     */
    public void startMediaProjection() {
        getView().onMediaProjectionCreate(isStarted);
    }

    /**
     * MediaProjection release
     */
    public void stopMediaProjection() {
        // Started status : true
        getView().onMediaProjectionCreate(true);
    }

    public void projectionStatus(int type) {
        setIsStarted(type);

        getView().onMediaProjectionAccessStatus(type);
    }

    private void setIsStarted(int type) {
        switch (type) {
            case MediaProjectionConstant.PROJECTION_TYPE_STARTED:
                isStarted = true;
                Log.d("TAG", "setIsStarted Projection type started");
                break;

            default:
                isStarted = false;
                break;
        }
    }
}
