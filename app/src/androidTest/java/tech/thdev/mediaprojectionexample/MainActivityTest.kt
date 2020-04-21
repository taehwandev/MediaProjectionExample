package tech.thdev.mediaprojectionexample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.thdev.mediaprojectionexample.ui.main.MainActivity
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Tae-hwan on 4/25/16.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    // create  a signal to let us know when our task is done.
    val signal = CountDownLatch(1)

    @Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    /**
     * Test example.
     * MediaProjection Don't show again checked test.
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun testStartActivity() {
        onView(withId(R.id.btn_start_media_projection_activity)).perform(click())
        onView(withId(R.id.fab)).perform(click())

        /* The testing thread will wait here until the UI thread releases it
        * above with the countDown() or 5 seconds passes and it times out.
        */signal.await(5, TimeUnit.SECONDS)
        onView(withId(R.id.fab)).perform(click())
    }
}