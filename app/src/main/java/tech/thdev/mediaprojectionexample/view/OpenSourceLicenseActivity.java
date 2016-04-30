package tech.thdev.mediaprojectionexample.view;

import tech.thdev.mediaprojectionexample.R;
import tech.thdev.mediaprojectionexample.base.view.BaseActivity;

/**
 * Created by taehwankwon on 4/30/16.
 */
public class OpenSourceLicenseActivity extends BaseActivity {

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_open_source_license;
    }

    @Override
    protected void onCreate() {

    }


    @Override
    protected int getToolbarTitle() {
        return R.string.open_source_license;
    }
}
