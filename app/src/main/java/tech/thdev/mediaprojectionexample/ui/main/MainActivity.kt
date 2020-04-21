package tech.thdev.mediaprojectionexample.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import kotlinx.android.synthetic.main.content_main.*
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.base.ui.BaseActivity
import tech.thdev.mediaprojectionexample.service.VideoViewService
import tech.thdev.mediaprojectionexample.ui.mediaprojection.MediaProjectionActivity

/**
 * MediaProjection Sample main
 *
 *
 * Created by Tae-hwan on 4/8/16.
 */
class MainActivity : BaseActivity(
    layoutRes = R.layout.activity_main,
    titleRes = R.string.app_name
) {
    companion object {
        const val REQ_CODE_OVERLAY_PERMISSION = 9999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_start_media_projection_service.setOnClickListener {
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                ),
                REQ_CODE_OVERLAY_PERMISSION
            )
        }

        btn_start_media_projection_activity.setOnClickListener {
            startActivity(Intent(this, MediaProjectionActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_CODE_OVERLAY_PERMISSION -> {
                if (Settings.canDrawOverlays(this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(Intent(this, VideoViewService::class.java))
                    } else {
                        startService(Intent(this, VideoViewService::class.java))
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}