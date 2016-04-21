package tech.thdev.mediaprojectionexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tae-hwan on 4/18/16.
 * <p>
 * MediaProjection Access
 */
public class MediaProjectionAccessActivity extends AppCompatActivity {

    @Override
    public void finish() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);

        super.finish();
    }
}
