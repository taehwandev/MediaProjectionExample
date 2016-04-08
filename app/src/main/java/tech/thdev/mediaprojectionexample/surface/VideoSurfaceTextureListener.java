package tech.thdev.mediaprojectionexample.surface;

import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.TextureView;

/**
 * Created by Tae-hwan on 4/8/16.
 */
public class VideoSurfaceTextureListener implements TextureView.SurfaceTextureListener {

    private Surface surface;

    public VideoSurfaceTextureListener() {
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = new Surface(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Nullable
    public Surface getSurface() {
        return surface;
    }
}
