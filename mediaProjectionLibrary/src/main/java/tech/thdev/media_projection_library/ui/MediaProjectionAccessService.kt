package tech.thdev.media_projection_library.ui

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.view.Surface
import tech.thdev.media_projection_library.R
import tech.thdev.media_projection_library.constant.MediaProjectionStatus
import tech.thdev.media_projection_library.constant.VALUE_PROJECTION_NAME
import tech.thdev.media_projection_library.constant.VALUE_SIZE_HEIGHT
import tech.thdev.media_projection_library.constant.VALUE_SIZE_WIDTH


open class MediaProjectionAccessService : Service() {

    private lateinit var mediaProjection: MediaProjection
    private lateinit var virtualDisplay: VirtualDisplay

    companion object {
        private const val FOREGROUND_SERVICE_ID = 1000

        private const val ACTION_INIT = "action_init"
        internal const val ACTION_PERMISSION_INIT = "action_permission_init"
        private const val ACTION_START = "action_start"
        private const val ACTION_STOP = "action_stop"
        internal const val ACTION_REJECT = "action_reject"

        const val EXTRA_RESULT_CODE = "result_code"
        const val EXTRA_REQUEST_DATA = "request_data"
        private const val EXTRA_SURFACE = "surface"
        private const val EXTRA_INTENT_MESSENGER = "messenger"

        private const val CHANNEL_ID = "MediaProjectionService"

        private fun newService(context: Context): Intent =
            Intent(context, MediaProjectionAccessService::class.java)

        fun newService(
            context: Context,
            messenger: Messenger
        ): Intent =
            newService(context).apply {
                putExtra(EXTRA_INTENT_MESSENGER, messenger)
                action = ACTION_INIT
            }

        fun newStartService(
            context: Context,
            surface: Surface
        ): Intent =
            newService(context).apply {
                putExtra(EXTRA_SURFACE, surface)
                action = ACTION_START
            }

        fun newStopService(
            context: Context
        ): Intent =
            newService(context).apply {
                action = ACTION_STOP
            }
    }

    private var messenger: Messenger? = null

    private val mediaProjectionManager: MediaProjectionManager by lazy {
        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    private val broadcastReceiver: MediaProjectionBroadcastReceiver by lazy {
        MediaProjectionBroadcastReceiver(::action)
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    open fun startForegroundService() {
        createNotificationChannel()
        val notificationIntent = Intent(this, MediaProjectionAccessActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }
            .setContentTitle("Foreground Service")
            .setSmallIcon(R.drawable.ic_baseline_fiber_manual_record_24)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(FOREGROUND_SERVICE_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        action(intent)
        return START_REDELIVER_INTENT
    }

    private fun action(intent: Intent?) {
        when (intent?.action) {
            ACTION_INIT -> getPermission(intent)
            ACTION_PERMISSION_INIT -> permissionInitMediaProjection(intent)
            ACTION_REJECT -> rejectMediaProjection()
            ACTION_START -> startMediaProjection(intent)
            ACTION_STOP -> stopMediaProjection()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun createMediaProjection() {
        getPermission()
    }

    private fun getPermission(intent: Intent? = null) {
        messenger = intent?.getParcelableExtra(EXTRA_INTENT_MESSENGER)
        initRegisterReceiver()
        startActivity(MediaProjectionAccessActivity.newInstance(this))
    }

    private fun initRegisterReceiver() {
        registerReceiver(
            broadcastReceiver,
            IntentFilter(MediaProjectionBroadcastReceiver.INTENT_FILTER_MEDIA_PROJECTION)
        )
    }

    private fun unregisterReceiver() {
        unregisterReceiver(broadcastReceiver)
    }

    private fun permissionInitMediaProjection(intent: Intent) {
        unregisterReceiver()
        val resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)
        val data = intent.getParcelableExtra<Intent>(EXTRA_REQUEST_DATA)
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data!!)
        mediaProjection.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
                sendMessage(MediaProjectionStatus.OnStop)
            }
        }, null)
        sendMessage(MediaProjectionStatus.OnInitialized)
    }

    private fun rejectMediaProjection() {
        unregisterReceiver()
        sendMessage(MediaProjectionStatus.OnReject)
    }

    private fun startMediaProjection(intent: Intent) {
        startMediaProjection(
            intent.getParcelableExtra(EXTRA_SURFACE) as Surface
        )
    }

    fun startMediaProjection(
        surface: Surface,
        projectionName: String = VALUE_PROJECTION_NAME,
        width: Int = VALUE_SIZE_WIDTH,
        height: Int = VALUE_SIZE_HEIGHT
    ) {
        if (::mediaProjection.isInitialized) {
            virtualDisplay = mediaProjection.createVirtualDisplay(
                projectionName,
                width,
                height,
                application.resources.displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface,
                null,
                null
            )
            sendMessage(MediaProjectionStatus.OnStarted)
        } else {
            sendMessage(MediaProjectionStatus.OnFail)
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
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(serviceChannel)
            }
        }
    }

    private fun sendMessage(status: MediaProjectionStatus) {
        try {
            val message = Message.obtain().also { it.obj = status }
            messenger?.send(message)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        onChangeStatus(status)
    }

    open fun onChangeStatus(status: MediaProjectionStatus) {
        // Do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMediaProjection()
    }
}