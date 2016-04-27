package tech.thdev.mediaprojectionexample.presenter.view;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;
import tech.thdev.mediaprojectionexample.base.presenter.view.BasePresenterView;

/**
 * Created by Tae-hwan on 4/25/16.
 */
public interface VideoViewServiceView extends BasePresenterView {

    void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type);

    void onMediaProjectionCreate(boolean isStart);
}
