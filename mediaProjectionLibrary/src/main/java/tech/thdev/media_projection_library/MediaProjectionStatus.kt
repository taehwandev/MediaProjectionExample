package tech.thdev.media_projection_library

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class MediaProjectionStatusData(
    val status: MediaProjectionStatus
) : Parcelable

enum class MediaProjectionStatus {
    OnInitialized,
    OnStarted,
    OnStop,
    OnFail,
    OnReject
}