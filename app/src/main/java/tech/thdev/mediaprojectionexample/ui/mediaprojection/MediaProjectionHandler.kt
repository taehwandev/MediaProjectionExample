package tech.thdev.mediaprojectionexample.ui.mediaprojection

import android.os.Handler
import android.os.Message
import tech.thdev.media_projection_library.constant.MediaProjectionStatus

/**
 * Created by Tae-hwan on 4/25/16.
 */
class MediaProjectionHandler(
    private val statusChange: (status: MediaProjectionStatus) -> Unit
) : Handler() {

    override fun handleMessage(msg: Message) {
        statusChange(msg.obj as MediaProjectionStatus)
    }
}