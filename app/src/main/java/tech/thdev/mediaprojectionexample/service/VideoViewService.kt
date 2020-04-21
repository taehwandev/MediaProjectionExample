package tech.thdev.mediaprojectionexample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.os.Messenger
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.window_video_view.view.*
import tech.thdev.media_projection_library.constant.MediaProjectionStatus
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.constant.KEY_INTENT_MESSENGER
import tech.thdev.mediaprojectionexample.constant.KEY_INTENT_SURFACE
import tech.thdev.mediaprojectionexample.service.handler.VideoViewHandler
import tech.thdev.mediaprojectionexample.surface.SurfaceViewHolder
import tech.thdev.mediaprojectionexample.ui.mediaprojection.TransparentMediaProjectionActivity

/**
 * Created by Tae-hwan on 4/8/16.
 */
class VideoViewService : Service() {

    private lateinit var windowView: View

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private lateinit var windowViewLayoutParams: WindowManager.LayoutParams
    private lateinit var videoViewHandler: VideoViewHandler

    private val surfaceViewHolder: SurfaceViewHolder by lazy {
        SurfaceViewHolder()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initWindowLayout(getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        videoViewHandler = VideoViewHandler(::onMediaProjectionAccessStatus)
        windowView.initCreate()
    }

    private fun View.initCreate() {
        surface_view.holder.addCallback(surfaceViewHolder)
        setOnTouchListener(WindowTouchEvent(::updateViewPosition))
        btn_stop_service.setOnClickListener {
            stopSelf()
        }
        main_play_pause_button.run {
            setColor(Color.DKGRAY)
            setOnClickListener {
                // Do nothing.
            }
            setOnControlStatusChangeListener { _, _ ->
                mediaProjectionCreate()
            }
        }
    }

    private fun updateViewPosition(x: Int, y: Int) {
        windowViewLayoutParams.x += x
        windowViewLayoutParams.y += y
        windowManager.updateViewLayout(windowView, windowViewLayoutParams)
    }

    /**
     * Window View 를 초기화 한다. X, Y 좌표는 0, 0으로 지정한다.
     */
    private fun initWindowLayout(layoutInflater: LayoutInflater) {
        windowView = layoutInflater.inflate(R.layout.window_video_view, null).also {
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
        windowManager.addView(windowView, windowViewLayoutParams)
    }

    private fun onMediaProjectionAccessStatus(status: MediaProjectionStatus) {
        when (status) {
            MediaProjectionStatus.OnInitialize -> {

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

    /**
     * Play button design change
     */
    private fun setPlayed(isPlayed: Boolean) {
        windowView.main_play_pause_button.run {
            if (this.isPlayed != isPlayed) {
                this.isPlayed = isPlayed
                startAnimation()
            }
        }
    }

    private fun mediaProjectionCreate() {
        Intent(this, TransparentMediaProjectionActivity::class.java).apply {
            putExtra(KEY_INTENT_MESSENGER, Messenger(videoViewHandler))
            putExtra(KEY_INTENT_SURFACE, windowView.surface_view.holder.surface)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::windowView.isInitialized) {
            windowManager.removeView(windowView)
        }
    }
}