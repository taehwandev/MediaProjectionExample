package tech.thdev.media_projection_library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import tech.thdev.media_projection_library.constant.MediaProjectionConstant
import tech.thdev.media_projection_library.constant.MediaProjectionStatus
import tech.thdev.media_projection_library.util.getDensityDpi

/**
 * Created by Tae-hwan on 4/25/16.
 * @since 4/12/20 Edit kotlin.
 *
 * MediaProjection Abstract
 */
abstract class MediaProjectionAccessActivity(
) : AppCompatActivity() {

    companion object {
        private const val REQ_CODE_MEDIA_PROJECTION = 1000
    }

    private val mediaProjectionManager: MediaProjectionManager by lazy {
        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    private lateinit var mediaProjection: MediaProjection
    private lateinit var virtualDisplay: VirtualDisplay

    private var onAccessListener: (status: MediaProjectionStatus) -> Unit = {}

    fun mediaProjectionInit(
        onAccessListener: (status: MediaProjectionStatus) -> Unit
    ) {
        this.onAccessListener = onAccessListener

        startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(),
            REQ_CODE_MEDIA_PROJECTION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_CODE_MEDIA_PROJECTION -> {
                onAccessListener(
                    if (resultCode == Activity.RESULT_OK) {
                        mediaProjection =
                            mediaProjectionManager.getMediaProjection(resultCode, data!!)
                        MediaProjectionStatus.OnInitialize
                    } else {
                        MediaProjectionStatus.OnReject
                    }
                )
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun startMediaProjection(
        surface: Surface,
        projectionName: String = MediaProjectionConstant.VALUE_PROJECTION_NAME,
        width: Int = MediaProjectionConstant.VALUE_SIZE_WIDTH,
        height: Int = MediaProjectionConstant.VALUE_SIZE_HEIGHT
    ) {
        android.util.Log.d("TEMP", "getDensity ${getDensityDpi()}")
        if (::mediaProjection.isInitialized) {
            virtualDisplay = mediaProjection.createVirtualDisplay(
                projectionName,
                width,
                height,
                getDensityDpi(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface,
                null,
                null
            )
            onAccessListener(MediaProjectionStatus.OnStarted)
        } else {
            onAccessListener(MediaProjectionStatus.OnFail)
        }
    }

    fun stopMediaProjection() {
        try {
            if (::mediaProjection.isInitialized) {
                mediaProjection.stop()
            }
            if (::virtualDisplay.isInitialized) {
                virtualDisplay.release()
            }
        } catch (e: Exception) {
        }
        onAccessListener(MediaProjectionStatus.OnStop)
    }
}