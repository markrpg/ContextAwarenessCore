package platform.contextawarenesscore;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import static platform.contextawarenesscore.common.Constants.ACTIVITY_RECOGNITION_INTENT;
import static platform.contextawarenesscore.common.Constants.Cycling;
import static platform.contextawarenesscore.common.Constants.Running;
import static platform.contextawarenesscore.common.Constants.Standing;
import static platform.contextawarenesscore.common.Constants.Still;
import static platform.contextawarenesscore.common.Constants.Tilting;
import static platform.contextawarenesscore.common.Constants.Unknown;
import static platform.contextawarenesscore.common.Constants.Vehicle;
import static platform.contextawarenesscore.common.Constants.Walking;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * AndroidRecognisedService - Intent Service used to gather recognised activities such as walking, cycling etc
 */
public class ActivityRecognisedService extends IntentService
{
    //Activity result attribute
    private ActivityRecognitionResult activityRecognised;

    //running or walking sensitivity
    int sensitivity = 50;

    /**
     * Default Constructor for activity service
     */
    public ActivityRecognisedService()
    {
        super("ActivityRecognisedService");
    }

    /**
     * Handles user activity and broadcasts it
     * @param intent override to detect user activity
     */
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(ActivityRecognitionResult.hasResult(intent))
        {
            //Extract activity result
            activityRecognised = ActivityRecognitionResult.extractResult(intent);

            //Detect most probable activity
            DetectedActivity detectedActivity = activityRecognised.getMostProbableActivity();

            //Broadcast most probable activity
            Intent activityIntent = new Intent();
            activityIntent.setAction(ACTIVITY_RECOGNITION_INTENT);
            activityIntent.putExtra(ACTIVITY_RECOGNITION_INTENT, getActivityName(detectedActivity));
            this.sendBroadcast(activityIntent);
        }
    }

    /**
     * Method used to get specific activity names
     * @param detectedActivity - Detected Activity object
     * @return - Returns the activity name as a string
     */
    private String getActivityName(DetectedActivity detectedActivity)
    {

        //Switch statement to determine, activity
        switch (detectedActivity.getType())
        {
            //Is user driving?
            case DetectedActivity.IN_VEHICLE:
            {
                return Vehicle;
            }

            //Is user on a bike?
            case DetectedActivity.ON_BICYCLE:
            {
                return Cycling;
            }


            //Is user still?
            case DetectedActivity.STILL:
            {
                return Still;
            }


            //Is user on a on foot?
            case DetectedActivity.ON_FOOT:
            {

                int walkingConfidence = 0;
                int runningConfidence = 0;
                String activity = Standing;

                //Get confidence values for walking and running
                for (DetectedActivity d : activityRecognised.getProbableActivities())
                {
                    //Check for walking first
                    if(d.getType() == DetectedActivity.WALKING)
                        walkingConfidence = d.getConfidence();
                    //Check for running secondly
                    if(d.getType() == DetectedActivity.RUNNING)
                        runningConfidence = d.getConfidence();
                }

                //Determine the highest probable movement activity against sensitivity
                runningConfidence -= sensitivity;
                walkingConfidence -= sensitivity;
                if(walkingConfidence > runningConfidence && walkingConfidence > 0)
                    return Walking;
                else if(runningConfidence > walkingConfidence && runningConfidence > 0)
                    return Running;

                //Return default activity
                return activity;
            }

            //Is the user looking at the phone?
            case DetectedActivity.TILTING:
            {
                return Tilting;
            }

            //Unknown activity
            case DetectedActivity.UNKNOWN:
            {
                return Unknown;
            }

        }

        //If no activities are found, return unknown activity
        return Unknown;
    }

}
