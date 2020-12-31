package tech.thdev.mediaprojectionexample.ui.mediaprojection

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import tech.thdev.media_projection_library.MediaProjectionStatus
import tech.thdev.media_projection_library.MediaProjectionStatusData
import tech.thdev.media_projection_library.ui.MediaProjectionAccessService
import tech.thdev.media_projection_library.ui.MediaProjectionAccessServiceBroadcastReceiver
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.databinding.ActivityMediaProjectionBinding
import tech.thdev.mediaprojectionexample.databinding.ContentMediaProjectionBinding
import tech.thdev.mediaprojectionexample.ui.surface.SurfaceViewHolder
import tech.thdev.mediaprojectionexample.ui.util.DeviceUtil

/**
 * Created by Tae-hwan on 4/8/16.
 *
 * MediaProjection example.
 */
class MediaProjectionActivity : AppCompatActivity() {

    companion object {

        fun newInstance(context: Context): Intent =
            Intent(context, MediaProjectionActivity::class.java)
    }

    private val surfaceViewHolder: SurfaceViewHolder by lazy {
        SurfaceViewHolder()
    }

    private var isStart = false

    private lateinit var binding: ActivityMediaProjectionBinding
    private lateinit var contentMainBinding: ContentMediaProjectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaProjectionBinding.inflate(layoutInflater)
        contentMainBinding = ContentMediaProjectionBinding.inflate(layoutInflater, binding.root, true)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        contentMainBinding.surfaceView.holder.addCallback(surfaceViewHolder)

        binding.fab.setOnClickListener {
            if (isStart) {
                stopMediaProjection()
            } else {
                mediaProjectionInit()
            }
        }
    }

    private fun onChangeStatus(statusData: MediaProjectionStatusData) {
        when (statusData.status) {
            MediaProjectionStatus.OnInitialized -> {
                isStart = false
                startMediaProjection()
            }
            MediaProjectionStatus.OnStarted -> {
                isStart = true
                binding.fab.setImageResource(android.R.drawable.ic_media_pause)
            }
            MediaProjectionStatus.OnStop -> {
                isStart = false
                binding.fab.setImageResource(android.R.drawable.ic_media_play)
                stopService()
            }
            MediaProjectionStatus.OnFail -> {
                isStart = false
                binding.fab.setImageResource(android.R.drawable.ic_media_play)
                Snackbar.make(binding.fab, R.string.media_projection_fail, Snackbar.LENGTH_SHORT).show()
                stopService()
            }
            MediaProjectionStatus.OnReject -> {
                isStart = false
                Snackbar.make(binding.fab, R.string.media_projection_reject, Snackbar.LENGTH_SHORT).show()
                stopService()
            }
        }
    }

    private fun mediaProjectionInit() {
        runService(MediaProjectionAccessService.newService(this))
        MediaProjectionAccessServiceBroadcastReceiver.register(this, ::onChangeStatus)
    }

    private fun startMediaProjection() {
        val deviceSize = DeviceUtil.getDeviceSize(this)
        runService(MediaProjectionAccessService.newStartMediaProjection(
            context = this,
            surface = contentMainBinding.surfaceView.holder.surface,
            width = deviceSize.width,
            height = deviceSize.height
        ))
    }

    private fun stopMediaProjection() {
        runService(MediaProjectionAccessService.newStopMediaProjection(this))
    }

    private fun stopService() {
        runService(MediaProjectionAccessService.newStopService(this))
        MediaProjectionAccessServiceBroadcastReceiver.unregister(this)
    }

    private fun runService(service: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service)
        } else {
            startService(service)
        }
    }
}