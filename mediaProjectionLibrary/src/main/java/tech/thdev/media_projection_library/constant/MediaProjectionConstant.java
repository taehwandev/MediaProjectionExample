package tech.thdev.media_projection_library.constant;

import android.support.annotation.IntDef;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public class MediaProjectionConstant {

    public static final String VALUE_PROJECTION_NAME = "projection-name";
    public static final int VALUE_SIZE_WIDTH = 1080;
    public static final int VALUE_SIZE_HEIGHT = 1920;

    @IntDef({PROJECTION_TYPE_STARTED, PROJECTION_TYPE_STOPPED, PROJECTION_TYPE_REJECT, PROJECTION_TYPE_FAIL})
    public @interface ProjectionStatusType {
    }

    public static final int PROJECTION_TYPE_STARTED = 1000;
    public static final int PROJECTION_TYPE_STOPPED = 2000;
    public static final int PROJECTION_TYPE_REJECT = 4000;
    public static final int PROJECTION_TYPE_FAIL = 3000;
}
