package tech.thdev.media_projection_library.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import tech.thdev.media_projection_library.MediaProjectionStatusData

class MediaProjectionAccessServiceBroadcastReceiver private constructor(
    private val onReceive: (statusData: MediaProjectionStatusData) -> Unit
) : BroadcastReceiver() {

    companion object {
        private const val INTENT_FILTER_ACCESS_SERVICE = "media_projection_access_service"
        private const val EXTRA_STATUS_DATA = "status_data"

        internal fun newInstance(statusData: MediaProjectionStatusData): Intent =
            Intent(INTENT_FILTER_ACCESS_SERVICE).apply {
                putExtra(EXTRA_STATUS_DATA, statusData)
            }

        private var receiver: MediaProjectionAccessServiceBroadcastReceiver? = null

        fun register(
            context: Context,
            onReceive: (statusData: MediaProjectionStatusData) -> Unit
        ) {
            if (receiver == null) {
                receiver = MediaProjectionAccessServiceBroadcastReceiver(onReceive)
                context.registerReceiver(receiver, IntentFilter(INTENT_FILTER_ACCESS_SERVICE))
            }
        }

        fun unregister(context: Context) {
            receiver?.let(context::unregisterReceiver)
            receiver = null
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getParcelableExtra<MediaProjectionStatusData>(EXTRA_STATUS_DATA)?.let(onReceive)
    }
}