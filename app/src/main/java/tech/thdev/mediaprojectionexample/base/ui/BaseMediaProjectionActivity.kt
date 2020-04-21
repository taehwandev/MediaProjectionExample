package tech.thdev.mediaprojectionexample.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import tech.thdev.media_projection_library.ui.MediaProjectionAccessActivity
import tech.thdev.mediaprojectionexample.R

/**
 * Created by Tae-hwan on 4/25/16.
 * @since 4/12/20 Edit kotlin.
 */
abstract class BaseMediaProjectionActivity(
    @LayoutRes private val layoutRes: Int,
    @StringRes private val titleRes: Int = 0
) : MediaProjectionAccessActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        setToolbar()
    }

    private fun setToolbar() {
        if (titleRes > 0) {
            findViewById<Toolbar>(R.id.toolbar).run {
                setTitle(titleRes)
                setSupportActionBar(this)
            }
        }
    }
}