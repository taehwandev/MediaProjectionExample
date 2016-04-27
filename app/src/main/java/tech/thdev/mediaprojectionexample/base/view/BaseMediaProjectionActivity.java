package tech.thdev.mediaprojectionexample.base.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.thdev.media_projection_library.view.MediaProjectionAccessActivity;
import tech.thdev.mediaprojectionexample.R;

/**
 * Created by Tae-hwan on 4/25/16.
 */
public abstract class BaseMediaProjectionActivity extends MediaProjectionAccessActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentLayoutResource());

        ButterKnife.bind(this);

        setToolBar();
        onCreate();
    }

    /**
     * ToolBar init
     */
    protected void setToolBar() {
        if (toolBar != null) {
            if (getToolbarTitle() > 0) {
                toolBar.setTitle(getToolbarTitle());
            }
            setSupportActionBar(toolBar);
        }
    }

    @LayoutRes
    protected abstract int getContentLayoutResource();

    protected abstract void onCreate();

    protected
    @StringRes
    int getToolbarTitle() {
        return 0;
    }
}
