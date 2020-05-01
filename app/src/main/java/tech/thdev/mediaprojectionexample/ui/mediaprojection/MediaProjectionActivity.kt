package tech.thdev.mediaprojectionexample.ui.mediaprojection

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Messenger
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import tech.thdev.media_projection_library.constant.MediaProjectionStatus
import tech.thdev.media_projection_library.ui.MediaProjectionAccessService
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.databinding.ActivityMediaProjectionBinding
import tech.thdev.mediaprojectionexample.databinding.ContentMediaProjectionBinding
import tech.thdev.mediaprojectionexample.ui.surface.SurfaceViewHolder

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

    private val messenger: Messenger by lazy {
        Messenger(MediaProjectionHandler(::onChangeStatus))
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

    private fun onChangeStatus(status: MediaProjectionStatus) {
        when (status) {
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
            }
            MediaProjectionStatus.OnFail -> {
                isStart = false
                binding.fab.setImageResource(android.R.drawable.ic_media_play)
                Snackbar.make(binding.fab, R.string.media_projection_fail, Snackbar.LENGTH_SHORT).show()
            }
            MediaProjectionStatus.OnReject -> {
                isStart = false
                Snackbar.make(binding.fab, R.string.media_projection_reject, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun mediaProjectionInit() {
        runService(MediaProjectionAccessService.newService(this, messenger))
    }

    private fun startMediaProjection() {
        runService(MediaProjectionAccessService.newStartService(this, contentMainBinding.surfaceView.holder.surface))
    }

    private fun stopMediaProjection() {
        runService(MediaProjectionAccessService.newStopService(this))
    }

    private fun runService(service: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service)
        } else {
            startService(service)
        }
    }
}