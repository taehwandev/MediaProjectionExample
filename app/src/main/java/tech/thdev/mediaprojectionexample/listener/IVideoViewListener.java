package tech.thdev.mediaprojectionexample.listener;

/**
 * Created by Tae-hwan on 4/8/16.
 */
public interface IVideoViewListener {

    void onStartProjection();

    void onStopProjection();

    void onDestroyService();
}
