package tech.thdev.mediaprojectionexample.base.presenter;

import tech.thdev.mediaprojectionexample.base.presenter.view.BasePresenterView;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public abstract class BasePresenter<T extends BasePresenterView> {

    private T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    protected T getView() {
        return view;
    }
}
