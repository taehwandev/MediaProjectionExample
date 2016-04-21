package tech.thdev.mediaprojectionexample.presenter;

import android.view.MotionEvent;

import tech.thdev.mediaprojectionexample.presenter.view.WindowTouchView;

/**
 * Created by Tae-hwan on 4/21/16.
 */
public class WindowTouchPresenter extends BasePresenter<WindowTouchView> {

    private float touchPrevX;
    private float touchPrevY;

    public WindowTouchPresenter(WindowTouchView view) {
        super(view);
    }

    public void onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
        }
    }

    private void actionDown(MotionEvent event) {
        touchPrevX = event.getRawX();
        touchPrevY = event.getRawY();
    }

    private void actionMove(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        float x = getDistance(rawX, touchPrevX);
        float y = getDistance(rawY, touchPrevY);

        touchPrevX = rawX;
        touchPrevY = rawY;

        getView().updateViewLayout((int) x, (int) y);
    }

    /**
     * @return Touch Distance
     */
    private float getDistance(float raw, float touchPrev) {
        return raw - touchPrev;
    }

    private void actionUp(MotionEvent event) {
        // Do noting...
    }
}
