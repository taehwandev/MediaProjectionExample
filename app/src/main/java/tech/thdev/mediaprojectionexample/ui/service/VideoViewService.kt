package tech.thdev.mediaprojectionexample.ui.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import tech.thdev.media_projection_library.MediaProjectionStatus
import tech.thdev.media_projection_library.MediaProjectionStatusData
import tech.thdev.media_projection_library.ui.MediaProjectionAccessService
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.databinding.WindowVideoViewBinding
import tech.thdev.mediaprojectionexample.ui.surface.SurfaceViewHolder
import tech.thdev.mediaprojectionexample.ui.util.DeviceUtil

/**
 * Created by Tae-hwan on 4/8/16.
 */
class VideoViewService : MediaProjectionAccessService() {

    companion object {
        fun newService(context: Context): Intent =
            Intent(context, VideoViewService::class.java)
    }

    private lateinit var windowBinding: WindowVideoViewBinding

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private lateinit var windowViewLayoutParams: WindowManager.LayoutParams

    private val surfaceViewHolder: SurfaceViewHolder by lazy {
        SurfaceViewHolder()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initWindowLayout(getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        windowBinding.initCreate()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun WindowVideoViewBinding.initCreate() {
        surfaceView.holder.addCallback(surfaceViewHolder)
        root.setOnTouchListener(WindowTouchEvent(updateViewLayout = ::updateViewPosition))
        btnStopService.setOnClickListener { stopSelf() }
        mainPlayPauseButton.run {
            setColor(Color.DKGRAY)
            setOnClickListener {
                // Do nothing.
            }
            setOnControlStatusChangeListener { _, state ->
                if (state) {
                    createMediaProjection()
                } else {
                    stopMediaProjection()
                }
            }
        }
    }

    private fun updateViewPosition(x: Int, y: Int) {
        windowViewLayoutParams.x += x
        windowViewLayoutParams.y += y
        windowManager.updateViewLayout(windowBinding.root, windowViewLayoutParams)
    }

    /**
     * Window View 를 초기화 한다. X, Y 좌표는 0, 0으로 지정한다.
     */
    private fun initWindowLayout(layoutInflater: LayoutInflater) {
        windowBinding = WindowVideoViewBinding.inflate(layoutInflater, null, false).also {
            windowViewLayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                30, 30,  // X, Y 좌표
                WindowManager.LayoutParams.TYPE_TOAST
                    .takeIf { Build.VERSION.SDK_INT < Build.VERSION_CODES.O }
                    ?: WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
            windowViewLayoutParams.gravity = Gravity.TOP or Gravity.START
        }
        windowManager.addView(windowBinding.root, windowViewLayoutParams)
    }

    /**
     * Play button design change
     */
    private fun setPlayed(isPlayed: Boolean) {
        windowBinding.mainPlayPauseButton.run {
            if (this.isPlayed != isPlayed) {
                this.isPlayed = isPlayed
                startAnimation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::windowBinding.isInitialized) {
            windowManager.removeView(windowBinding.root)
        }
    }

    override fun onChangeStatus(statusData: MediaProjectionStatusData) {
        super.onChangeStatus(statusData)

        when (statusData.status) {
            MediaProjectionStatus.OnInitialized -> {
                val deviceSize = DeviceUtil.getDeviceSize(this)
                startMediaProjection(
                    surface = windowBinding.surfaceView.holder.surface,
                    width = deviceSize.width,
                    height = deviceSize.height
                )
            }
            MediaProjectionStatus.OnStarted -> {
                setPlayed(true)
                Toast.makeText(this, R.string.media_projection_started, Toast.LENGTH_SHORT).show()
            }
            MediaProjectionStatus.OnStop -> {
                setPlayed(false)
                Toast.makeText(this, R.string.media_projection_stopped, Toast.LENGTH_SHORT).show()
            }
            MediaProjectionStatus.OnFail -> {
                setPlayed(false)
                Toast.makeText(this, R.string.media_projection_fail, Toast.LENGTH_SHORT).show()
            }
            MediaProjectionStatus.OnReject -> {
                setPlayed(false)
                Toast.makeText(this, R.string.media_projection_reject, Toast.LENGTH_SHORT).show()
            }
        }
    }
}