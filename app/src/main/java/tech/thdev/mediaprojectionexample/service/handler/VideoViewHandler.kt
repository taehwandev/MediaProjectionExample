package tech.thdev.mediaprojectionexample.service.handler

import android.os.Handler
import android.os.Message
import tech.thdev.media_projection_library.constant.MediaProjectionStatus

/**
 * Created by Tae-hwan on 4/25/16.
 */
class VideoViewHandler(
    private val statusChange: (status: MediaProjectionStatus) -> Unit
) : Handler() {

    override fun handleMessage(msg: Message) {
        statusChange(msg.obj as MediaProjectionStatus)
    }
}