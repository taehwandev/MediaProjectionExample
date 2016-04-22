package tech.thdev.media_projection_library.base.presenter;


import tech.thdev.media_projection_library.base.presenter.view.BaseView;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public abstract class BasePresenter<T extends BaseView> {

    private T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    protected T getView() {
        return view;
    }
}
