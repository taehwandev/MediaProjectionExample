package tech.thdev.mediaprojectionexample.presenter;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import tech.thdev.mediaprojectionexample.base.presenter.BasePresenter;
import tech.thdev.mediaprojectionexample.presenter.view.MainPresenterView;

/**
 * Created by Tae-hwan on 4/27/16.
 */
public class MainPresenter extends BasePresenter<MainPresenterView> {

    public MainPresenter(MainPresenterView view) {
        super(view);
    }

    public void startOverlayWindowService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(context)) {
            getView().onObtainingPermissionOverlayWindow();

        } else {
            getView().onStartService();
        }
    }

    public void onOverlayResult(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                getView().onStartService();
            }
        }
    }
}
