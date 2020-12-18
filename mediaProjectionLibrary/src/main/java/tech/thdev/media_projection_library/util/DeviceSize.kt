package tech.thdev.media_projection_library.util

import android.app.Service
import android.content.Context
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager

class DeviceSize(context: Context) {

    val size: Size

    init {
        val display = (context.getSystemService(Service.WINDOW_SERVICE) as WindowManager).defaultDisplay

        val dm = DisplayMetrics()
        display.getMetrics(dm)

        this.size = Size(dm.widthPixels, dm.heightPixels);
    }
}