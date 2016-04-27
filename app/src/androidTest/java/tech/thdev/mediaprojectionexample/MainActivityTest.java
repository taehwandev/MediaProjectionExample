package tech.thdev.mediaprojectionexample;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import tech.thdev.mediaprojectionexample.view.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Tae-hwan on 4/25/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // create  a signal to let us know when our task is done.
    final CountDownLatch signal = new CountDownLatch(1);

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Test example.
     * MediaProjection Don't show again checked test.
     *
     * @throws Exception
     */
    @Test
    public void testStartActivity() throws Exception {
        onView(withId(R.id.btn_start_media_projection_activity)).perform(click());

        onView(withId(R.id.fab)).perform(click());

        /* The testing thread will wait here until the UI thread releases it
        * above with the countDown() or 5 seconds passes and it times out.
        */
        signal.await(5, TimeUnit.SECONDS);

        onView(withId(R.id.fab)).perform(click());
    }
}