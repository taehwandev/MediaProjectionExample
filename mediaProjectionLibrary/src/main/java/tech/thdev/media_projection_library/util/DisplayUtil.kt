package tech.thdev.media_projection_library.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Tae-hwan on 4/21/16.
 * @since 4/12/20 Edit kotlin.
 */
fun Activity.getDensityDpi(): Int {
    return DisplayMetrics().run {
        this@getDensityDpi.windowManager.defaultDisplay.getMetrics(this)
        this.densityDpi
    }
}