package tech.thdev.mediaprojectionexample.ui.service

import android.view.MotionEvent
import android.view.View

class WindowTouchEvent(
    private val updateViewLayout: (x: Int, y: Int) -> Unit
) : View.OnTouchListener {

    private var touchPrevX: Float = 0.0f
    private var touchPrevY: Float = 0.0f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                actionUp(event)
            }
            else -> {

            }
        }
        return false
    }

    private fun actionDown(event: MotionEvent) {
        touchPrevX = event.rawX
        touchPrevY = event.rawY
    }

    private fun actionMove(event: MotionEvent) {
        val rawX = event.rawX
        val rawY = event.rawY

        val x = getDistance(rawX, touchPrevX)
        val y = getDistance(rawY, touchPrevY)

        touchPrevX = rawX
        touchPrevY = rawY

        updateViewLayout(x.toInt(), y.toInt())
    }

    private fun getDistance(raw: Float, touchPrev: Float): Float =
        raw - touchPrev

    private fun actionUp(event: MotionEvent) {
        // Do nothing.
    }
}