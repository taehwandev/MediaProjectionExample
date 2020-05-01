package tech.thdev.media_projection_library.constant

/**
 * Created by Tae-hwan on 4/22/16.
 * @since 4/12/20 Edit kotlin.
 */

const val VALUE_PROJECTION_NAME = "projection-name"
const val VALUE_SIZE_WIDTH = 1080
const val VALUE_SIZE_HEIGHT = 1920

sealed class MediaProjectionStatus {
    object OnInitialized : MediaProjectionStatus()
    object OnStarted : MediaProjectionStatus()
    object OnStop : MediaProjectionStatus()
    object OnFail : MediaProjectionStatus()
    object OnReject : MediaProjectionStatus()
}