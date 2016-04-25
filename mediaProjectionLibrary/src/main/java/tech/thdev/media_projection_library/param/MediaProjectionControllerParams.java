package tech.thdev.media_projection_library.param;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Surface;

import tech.thdev.media_projection_library.constant.MediaProjectionConstant;

/**
 * Created by Tae-hwan on 4/22/16.
 * <p/>
 * MediaProjection init params.
 */
public class MediaProjectionControllerParams implements Parcelable {

    private Surface surface;
    private String projectionName = MediaProjectionConstant.VALUE_PROJECTION_NAME;
    private int width = MediaProjectionConstant.VALUE_SIZE_WIDTH;
    private int height = MediaProjectionConstant.VALUE_SIZE_HEIGHT;

    public static MediaProjectionControllerParams create(Surface surface) {
        return create(surface, MediaProjectionConstant.VALUE_PROJECTION_NAME, MediaProjectionConstant.VALUE_SIZE_WIDTH, MediaProjectionConstant.VALUE_SIZE_HEIGHT);
    }

    public static MediaProjectionControllerParams create(Surface surface, int width, int height) {
        return create(surface, MediaProjectionConstant.VALUE_PROJECTION_NAME, width, height);
    }

    /**
     * MediaProjection params create
     *
     * @param surface TextureView
     * @param projectionName Create projection name
     * @param width VirtualDisplay width
     * @param height VirtualDisplay height
     * @return
     */
    public static MediaProjectionControllerParams create(Surface surface, String projectionName, int width, int height) {
        return new MediaProjectionControllerParams(surface, projectionName, width, height);
    }

    public MediaProjectionControllerParams(Surface surface, String projectionName, int width, int height) {
        this.surface = surface;
        this.projectionName = projectionName;
        this.width = width;
        this.height = height;
    }

    public MediaProjectionControllerParams(Parcel source) {
        surface = source.readParcelable(Surface.class.getClassLoader());
        projectionName = source.readString();
        width = source.readInt();
        height = source.readInt();
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
