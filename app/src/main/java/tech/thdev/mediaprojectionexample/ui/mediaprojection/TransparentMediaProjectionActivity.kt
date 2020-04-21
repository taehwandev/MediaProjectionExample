package tech.thdev.mediaprojectionexample.ui.mediaprojection

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.Surface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.base.ui.BaseMediaProjectionActivity
import tech.thdev.mediaprojectionexample.constant.KEY_INTENT_MESSENGER
import tech.thdev.mediaprojectionexample.constant.KEY_INTENT_SURFACE

/**
 * Created by Tae-hwan on 4/25/16.
 *
 * MediaProjection Access activity
 */
class TransparentMediaProjectionActivity : BaseMediaProjectionActivity(
    layoutRes = R.layout.activity_media_projection
) {

    companion object {
        private const val TAG = "TransparentActivity"
    }

    private val viewModel: MediaProjectionViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MediaProjectionViewModel() as T
            }
        }).get(MediaProjectionViewModel::class.java)
    }

    private val messenger: Messenger by lazy {
        intent.extras!!.get(KEY_INTENT_MESSENGER) as Messenger
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createMediaProjection()

        viewModel.init.observe(this, Observer {
            mediaProjectionInit(viewModel::onMediaProjectionListener)
        })
        viewModel.stop.observe(this, Observer {
            stopMediaProjection()
        })

        viewModel.message.observe(this, Observer {
            send(it)
        })

        viewModel.statusInitialize.observe(this, Observer {
            startMediaProjection(intent.extras!!.get(KEY_INTENT_SURFACE) as Surface)
        })
    }

    private fun send(message: Message) {
        try {
            moveTaskToBack(true)
            messenger.send(message)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.d(TAG, "onNewIntent")
        createMediaProjection()
    }

    private fun createMediaProjection() {
        viewModel.restartMediaProjection()
    }
}