package tech.thdev.media_projection_library.presenter.view;


import tech.thdev.media_projection_library.constant.MediaProjectionConstant;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public interface MediaProjectionAccessView {

    /**
     * MediaProjection access status
     */
    void onMediaProjectionAccessStatus(@MediaProjectionConstant.ProjectionStatusType int type);
}
