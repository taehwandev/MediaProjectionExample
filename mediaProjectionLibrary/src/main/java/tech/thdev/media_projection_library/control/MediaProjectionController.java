package tech.thdev.media_projection_library.control;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Surface;

import tech.thdev.media_projection_library.constant.Constant;
import tech.thdev.media_projection_library.listener.IMediaProjectionControlListener;
import tech.thdev.media_projection_library.listener.OnMediaProjectionAccessListener;
import tech.thdev.media_projection_library.param.MediaProjectionControllerParams;
import tech.thdev.media_projection_library.view.MediaProjectionAccessActivity;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public class MediaProjectionController {

    private MediaProjectionControllerParams mediaProjectionControllerParams;

    public MediaProjectionController(Activity activity, IMediaProjectionControlListener projectionControlListener) {
        mediaProjectionControllerParams = new MediaProjectionControllerParams(activity, new OnMediaProjectionAccessListener(projectionControlListener));
    }

    public void destoryProjection() {

    }

    public static class Builder {

        private MediaProjectionController mediaProjectionAccess;

        public Builder(Activity activity) {
            mediaProjectionAccess = new MediaProjectionController(activity, new IMediaProjectionControlListener() {
                @Override
                public void onMediaProjectionEvent(int type) {
                    Log.d("TAG", "aaa");
                }
            });
        }

        public Builder setWidth(int width) {
            mediaProjectionAccess.mediaProjectionControllerParams.setWidth(width);
            return this;
        }

        public Builder setHeight(int height) {
            mediaProjectionAccess.mediaProjectionControllerParams.setHeight(height);
            return this;
        }

        public Builder setSurface(Surface surface) {
            mediaProjectionAccess.mediaProjectionControllerParams.setSurface(surface);
            return this;
        }

        public Builder setMediaProjectionName(String projectionName) {
            mediaProjectionAccess.mediaProjectionControllerParams.setProjectionName(projectionName);
            return this;
        }

        public MediaProjectionController create() {
            Activity activity = mediaProjectionAccess.mediaProjectionControllerParams.getActivity();
            Intent intent = new Intent(activity, MediaProjectionAccessActivity.class);
            intent.putExtra(Constant.KEY_INTENT_CONTROL_PARAMS, mediaProjectionAccess.mediaProjectionControllerParams);
            activity.startActivity(intent);
            return mediaProjectionAccess;
        }
    }
}
