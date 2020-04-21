package tech.thdev.mediaprojectionexample.ui.mediaprojection

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_media_projection.*
import kotlinx.android.synthetic.main.content_media_projection.*
import tech.thdev.mediaprojectionexample.R
import tech.thdev.mediaprojectionexample.base.ui.BaseMediaProjectionActivity
import tech.thdev.mediaprojectionexample.surface.SurfaceViewHolder

/**
 * Created by Tae-hwan on 4/8/16.
 *
 * MediaProjection example.
 */
class MediaProjectionActivity : BaseMediaProjectionActivity(
    layoutRes = R.layout.activity_media_projection
) {

    private val viewModel: MediaProjectionViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MediaProjectionViewModel() as T
            }
        }).get(MediaProjectionViewModel::class.java)
    }

    private val surfaceViewHolder: SurfaceViewHolder by lazy {
        SurfaceViewHolder()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surface_view.holder.addCallback(surfaceViewHolder)

        fab.setOnClickListener {
            viewModel.restartMediaProjection()
        }

        viewModel.init.observe(this, Observer {
            mediaProjectionInit(viewModel::onMediaProjectionListener)
        })
        viewModel.stop.observe(this, Observer {
            stopMediaProjection()
        })

        viewModel.statusInitialize.observe(this, Observer {
            startMediaProjection(surface_view.holder.surface)
        })
        viewModel.statusStarted.observe(this, Observer {
            fab.setImageResource(android.R.drawable.ic_media_pause)
        })
        viewModel.statusStop.observe(this, Observer {
            fab.setImageResource(android.R.drawable.ic_media_play)
        })
        viewModel.statusFail.observe(this, Observer {
            Snackbar.make(fab, R.string.media_projection_fail, Snackbar.LENGTH_SHORT).show()
        })
        viewModel.statusReject.observe(this, Observer {
            Snackbar.make(fab, R.string.media_projection_reject, Snackbar.LENGTH_SHORT).show()
        })
    }
}