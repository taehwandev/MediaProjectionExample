package tech.thdev.mediaprojectionexample.ui.util

import android.app.Service
import android.content.Context
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager

object DeviceUtil {
    fun getDeviceSize(context: Context): Size {
        val dm = DisplayMetrics()
        val display =
            (context.getSystemService(Service.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getMetrics(dm)

        return Size(dm.widthPixels, dm.heightPixels)
    }
}