package platform.contextawarenesscore;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import platform.contextawarenesscore.common.Constants;

import static org.junit.Assert.assertTrue;
import static platform.contextawarenesscore.common.Constants.ACTIVITY_RECOGNITION_INTENT;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * UserActivityContext - Unit Tests
 */
public class UserActivityContextTest
{

    //Context used in unit test
    private Context context;

    //User Activity Context instance
    private UserActivityContext userContext;

    /**
     * Setup the Unit Test and mock values
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {

        //Set context for unit test
        context = InstrumentationRegistry.getTargetContext().getApplicationContext();

        //Setup usercontext insert dummy values for testing
        userContext = new UserActivityContext(context);

        //Simulate onReceive STILL activity for testing
        Intent activityIntent = new Intent();
        activityIntent.setAction(ACTIVITY_RECOGNITION_INTENT);
        activityIntent.putExtra(ACTIVITY_RECOGNITION_INTENT, "STILL");
        userContext.onReceive(context,activityIntent);

        //Wait 2 second and send another onReceive event
        Thread.sleep(2000);

        //Simulate onReceive STILL activity for testing
        userContext.onReceive(context,activityIntent);

        //Simulate headphones and microphone connected
        Intent headMicIntent = new Intent();
        headMicIntent.setAction(Intent.ACTION_HEADSET_PLUG);
        headMicIntent.putExtra("state", 1);
        headMicIntent.putExtra("microphone", 1);
        userContext.onReceive(context,headMicIntent);
    }

    /**
     * Unit test to check the activity time spent is correctly returned
     * @throws Exception
     */
    @Test
    public void getActivityTimeSpentTest() throws Exception
    {
        //Assert if activity is returned successfully and has registered a time change
        assertTrue(userContext.getActivityTimeSpent(Constants.Still).getSeconds() > 0);
    }


    /**
     * Test that the activity recognition is working
     * @throws Exception
     */
    @Test
    public void onReceiveActivityRecognitionTest() throws Exception
    {
        assertTrue(userContext.getUser_Activity() == "STILL");

    }

    /**
     * Test that the activity recognition is working
     * @throws Exception
     */
    @Test
    public void onReceiveHeadphoneChange() throws Exception
    {
        assertTrue(userContext.HeadphonesIn());
        assertTrue(userContext.isMicrophoneIn());
    }
}