package tech.thdev.media_projection_library.listener;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public class OnMediaProjectionAccessListener implements Serializable {

    public static final int TYPE_SUCCESS = 1000;
    public static final int TYPE_FAIL = 2000;
    public static final int TYPE_REJECT = 3000;

    private IMediaProjectionControlListener mediaProjectionControlListener;

    public OnMediaProjectionAccessListener() {

    }

    public OnMediaProjectionAccessListener(IMediaProjectionControlListener mediaProjectionControlListener) {
        this.mediaProjectionControlListener = mediaProjectionControlListener;
    }

    public void onMediaProjectionEvent(int type) {
        Log.d("TAG", "onMediaProjectionEvent : " + type);
        if (mediaProjectionControlListener != null) {
            mediaProjectionControlListener.onMediaProjectionEvent(type);
        }
    }

    //    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }

//    public final Parcelable.Creator<OnMediaProjectionAccessListener>
//            CREATOR = new Parcelable.Creator<OnMediaProjectionAccessListener>() {
//
//        @Override
//        public OnMediaProjectionAccessListener createFromParcel(Parcel source) {
//            return create(source);
//        }
//
//        @Override
//        public OnMediaProjectionAccessListener[] newArray(int size) {
//            return new OnMediaProjectionAccessListener[size];
//        }
//    };
}
