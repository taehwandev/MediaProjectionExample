package tech.thdev.media_projection_library.param;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Surface;

import tech.thdev.media_projection_library.constant.Constant;
import tech.thdev.media_projection_library.listener.OnMediaProjectionAccessListener;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public class MediaProjectionControllerParams implements Parcelable {

    private Activity activity;
    private Surface surface;
    private String projectionName = "media-projection";
    private int width = Constant.DEFAULT_SIZE_WIDTH;
    private int height = Constant.DEFAULT_SIZE_HEIGHT;

    private OnMediaProjectionAccessListener mediaProjectionListener;

    public MediaProjectionControllerParams(Activity activity, OnMediaProjectionAccessListener mediaProjectionListener) {
        this.activity = activity;
        this.mediaProjectionListener = mediaProjectionListener;
    }

    public MediaProjectionControllerParams(Parcel source) {
        surface = source.readParcelable(Surface.class.getClassLoader());
        projectionName = source.readString();
        width = source.readInt();
        height = source.readInt();

//        mediaProjectionListener = source.readParcelable(OnMediaProjectionAccessListener.class.getClassLoader());
        mediaProjectionListener = (OnMediaProjectionAccessListener) source.readSerializable();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(surface, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(projectionName);
        dest.writeInt(width);
        dest.writeInt(height);

//        dest.writeParcelable(mediaProjectionListener, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeSerializable(mediaProjectionListener);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setProjectionName(String projectionName) {
        this.projectionName = projectionName;
    }

    public String getProjectionName() {
        return projectionName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public OnMediaProjectionAccessListener getMediaProjectionListener() {
        return mediaProjectionListener;
    }

    public static final Parcelable.Creator<MediaProjectionControllerParams>
            CREATOR = new Parcelable.Creator<MediaProjectionControllerParams>() {

        @Override
        public MediaProjectionControllerParams createFromParcel(Parcel source) {
            return new MediaProjectionControllerParams(source);
        }

        @Override
        public MediaProjectionControllerParams[] newArray(int size) {
            return new MediaProjectionControllerParams[size];
        }
    };
}
