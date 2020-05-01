package tech.thdev.media_projection_library.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class MediaProjectionBroadcastReceiver(
    private val onReceive: (intent: Intent?) -> Unit
) : BroadcastReceiver() {
    companion object {
        private const val KEY_ACTION = "action"
        const val INTENT_FILTER_MEDIA_PROJECTION = "media_projection_permission_event"

        fun newInstance(
            resultCode: Int,
            requestData: Intent
        ): Intent =
            Intent(INTENT_FILTER_MEDIA_PROJECTION).apply {
                putExtra(MediaProjectionAccessService.EXTRA_RESULT_CODE, resultCode)
                putExtra(MediaProjectionAccessService.EXTRA_REQUEST_DATA, requestData)
                putExtra(KEY_ACTION, MediaProjectionAccessService.ACTION_PERMISSION_INIT)
            }

        fun newReject(): Intent =
            Intent(INTENT_FILTER_MEDIA_PROJECTION).apply {
                putExtra(KEY_ACTION, MediaProjectionAccessService.ACTION_REJECT)
            }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action = intent?.getStringExtra(KEY_ACTION)
        onReceive(intent)
    }
}