package tech.thdev.media_projection_library.presenter.view;

import tech.thdev.media_projection_library.base.presenter.view.BaseView;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public interface MediaProjectionView extends BaseView {

    void startedMediaProjection();

    void rejectMediaProjection();

    void failMediaProjection();
}
