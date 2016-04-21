package tech.thdev.mediaprojectionexample.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public class DisplayUtil {

    public static int getDensityDpi(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }
}
