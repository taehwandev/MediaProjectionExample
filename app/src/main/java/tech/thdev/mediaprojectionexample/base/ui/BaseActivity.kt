package tech.thdev.mediaprojectionexample.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import tech.thdev.mediaprojectionexample.R

/**
 * Created by Tae-hwan on 4/8/16.
 * @since 4/12/20 Edit kotlin.
 *
 * Toolbar Base activity
 */
abstract class BaseActivity(
    @LayoutRes private val layoutRes: Int,
    @StringRes private val titleRes: Int
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        setToolbar()
    }

    private fun setToolbar() {
        findViewById<Toolbar>(R.id.toolbar).run {
            setTitle(titleRes)
            setSupportActionBar(this)
        }
    }
}