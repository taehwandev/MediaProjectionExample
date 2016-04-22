package tech.thdev.media_projection_library.listener;

import java.io.Serializable;

/**
 * Created by Tae-hwan on 4/22/16.
 */
public interface IMediaProjectionControlListener extends Serializable {

    void onMediaProjectionEvent(int type);
}
