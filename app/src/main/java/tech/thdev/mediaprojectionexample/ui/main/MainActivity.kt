package tech.thdev.mediaprojectionexample.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.databinding.ActivityMainBinding
import tech.thdev.mediaprojectionexample.databinding.ContentMainBinding
import tech.thdev.mediaprojectionexample.ui.mediaprojection.MediaProjectionActivity
import tech.thdev.mediaprojectionexample.ui.service.VideoViewService

/**
 * MediaProjection Sample main
 *
 * Created by Tae-hwan on 4/8/16.
 */
class MainActivity : AppCompatActivity() {

    private val canOverlay = registerForActivityResult(OverlayActivityResultContract()) {
        runService()
    }

    private fun runService() {
        if (Settings.canDrawOverlays(this)) {
            startService()
        } else {
            showRejectDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val contentMainBinding = ContentMainBinding.inflate(layoutInflater, binding.root, true)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        contentMainBinding.btnStartMediaProjectionService.setOnClickListener {
            runService()
        }

        contentMainBinding.btnStartMediaProjectionActivity.setOnClickListener {
            startActivity(MediaProjectionActivity.newInstance(this))
        }
    }

    private fun showRejectDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reject_dialog_title)
            .setMessage(R.string.reject_dialog_message)
            .setPositiveButton("Ok") { _, _ ->
                canOverlay.launch("package:$packageName")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(VideoViewService.newService(this))
        } else {
            startService(VideoViewService.newService(this))
        }
    }
}

class OverlayActivityResultContract : ActivityResultContract<String, Boolean>() {

    override fun createIntent(context: Context, input: String?): Intent =
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(input))

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        resultCode == Activity.RESULT_OK
}