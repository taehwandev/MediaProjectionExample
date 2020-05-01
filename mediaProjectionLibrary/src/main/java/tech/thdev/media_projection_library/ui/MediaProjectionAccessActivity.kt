package tech.thdev.media_projection_library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import tech.thdev.media_projection_library.R

/**
 * Created by Tae-hwan on 4/25/16.
 * @since 4/12/20 Edit kotlin.
 *
 * MediaProjection mediaProjection manager
 */
internal class MediaProjectionAccessActivity : AppCompatActivity() {

    companion object {
        private const val REQ_CODE_MEDIA_PROJECTION = 1000

        fun newInstance(context: Context): Intent =
            Intent(context, MediaProjectionAccessActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_projection)
        createMediaProjection()
    }

    private fun createMediaProjection() {
        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val result: ActivityResultLauncher<Intent> = registerForActivityResult(MediaProjectionResultContract()) {
            sendBroadcast(it)
            finish()
        }
        result.launch(mediaProjectionManager.createScreenCaptureIntent())

        // Old forResult
//        startActivityForResult(
//            mediaProjectionManager.createScreenCaptureIntent(),
//            REQ_CODE_MEDIA_PROJECTION
//        )
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when (requestCode) {
//            REQ_CODE_MEDIA_PROJECTION -> sendBroadcast(resultCode, data)
//            else -> super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
}

internal class MediaProjectionResultContract : ActivityResultContract<Intent, Intent>() {

    override fun createIntent(context: Context, input: Intent?): Intent =
        input!!

    override fun parseResult(resultCode: Int, intent: Intent?): Intent {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            MediaProjectionBroadcastReceiver.newInstance(resultCode, intent)
        } else {
            MediaProjectionBroadcastReceiver.newReject()
        }
    }
}