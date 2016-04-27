package tech.thdev.mediaprojectionexample.presenter.view;

import tech.thdev.mediaprojectionexample.base.presenter.view.BasePresenterView;

/**
 * Created by Tae-hwan on 4/27/16.
 */
public interface MainPresenterView extends BasePresenterView {

    void onStartService();

    void onObtainingPermissionOverlayWindow();
}
