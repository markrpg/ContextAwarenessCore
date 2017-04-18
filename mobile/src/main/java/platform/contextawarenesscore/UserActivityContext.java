package platform.contextawarenesscore;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import platform.contextawarenesscore.common.Constants;
import platform.contextawarenesscore.common.Time;

import static platform.contextawarenesscore.common.Constants.ACTIVITY_RECOGNITION_INTENT;
import static platform.contextawarenesscore.common.Constants.HEARTRATE_KEY;
import static platform.contextawarenesscore.common.Constants.HEARTRATE_PATH;
import static platform.contextawarenesscore.common.Constants.TimeStampValue;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * UserActivityContext - Used to get user activity primary context
 */
public class UserActivityContext extends PrimaryContext
{

    //Getter for steps taken
    protected int getStepsTaken() { if(initialStepsTaken > 0) return (stepsTaken - initialStepsTaken); else return stepsTaken; }

    //Getter for heart rate
    protected int getHeartRate()
    {
        //Check if timestamp for heartrate check has elapsed enough time to check again
        //Get Calendar instance to retrieve accurate timing
        Calendar rightNow = Calendar.getInstance();

        //Get current elapsed real time
        long elapsedStart = rightNow.getTimeInMillis();

        //Check if activity has happened update total elapsed time
        if(elapsedTimeMap.containsKey(Constants.Heartrate))
        {
            long newTime = (elapsedStart - (elapsedTimeMap.get(Constants.Heartrate)));

            //Only update Heartrate if 1min has passed since the last check
            if (newTime >= Constants.HeartRateUpdateTime)
            {
                getMessageSenderClass().sendToast(Constants.HeartRateMessage);
                elapsedTimeMap.put(Constants.Heartrate, elapsedStart);
            }
        }else if(!elapsedTimeMap.containsKey(Constants.Heartrate))
        {
            getMessageSenderClass().sendToast(Constants.HeartRateMessage);
            elapsedTimeMap.put(Constants.Heartrate,elapsedStart);
        }

        //Reset heartrate and return
        if(heartRate > 0)
        {
            int hr = heartRate;
            heartRate = 0;
            return hr;
        }
        else
        {
            return 0;
        }
    }

    //Getter for User Activity
    protected String getUser_Activity() { return user_Activity; }

    /**
     * Getter for if headset is connected
     * @return returns true if headset plugged in
     */
    protected boolean HeadphonesIn(){ return headPhonesIn; }

    /**
     * Getter for microphone connected
     * @return microphone connection state
     */
    protected boolean isMicrophoneIn() { return microphoneIn; }

    /**
     * Method used to get the time spend for each activity in seconds
     * @param activity - Name of the activity (walking, running etc.)
     * @return - Total time spent for activity as Time object
     */
    protected Time getActivityTimeSpent(String activity)
    {
        long timeSpent = 0;
        if(elapsedTimeMap.containsKey(activity)) {

            timeSpent = elapsedTimeMap.get(activity);
            Time time = new Time(((int) timeSpent));
            return time;
        }
        return new Time(0);
    }

    //Initial steps (Sensor can be reset only by restarting it) minus this from stepstaken to get current steps
    private int initialStepsTaken;

    //Attribute to hold steps taken data
    private int stepsTaken;

    //Attribute for heart rate
    private int heartRate;

    //Attribute for headphones connected
    private boolean headPhonesIn;

    //Attribute for Microphone connected
    private boolean microphoneIn;

    //Attribute for user activity
    private String user_Activity = "";

    //LinkedHashMap of user activities used to track elapsed time of each activites and heartrate
    private Map<String, Long> elapsedTimeMap;

    //Default Constructor
    protected UserActivityContext(Context context)
    {
        //Call abstracted parent passing context
        super(context);
        elapsedTimeMap = new HashMap<String,Long>();
        //Initialise Sensors
        initialiseContext();
        //Initial steps taken is 0
        initialStepsTaken = 0;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);

        //Create pending intent for actively gathering user activity every 1 seconds
        Intent intent = new Intent(Context, ActivityRecognisedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( Context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( googleApiClient, 1000, pendingIntent );
    }

    //Method to initialise sensor listeners for
    private void initialiseContext()
    {
        //Initiate sensor listeners
        //Step Sensor
        getSensorManager().registerListener(this, getSensorManager().getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL);

        //Setup broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction(ACTIVITY_RECOGNITION_INTENT);
        Context.registerReceiver(this,intentFilter);
    }

    //Sensor changed event
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //Get sensor event
        Sensor sensor = event.sensor;

        try {
            //Determine type of sensor
            switch (sensor.getType()) {
                //If step sensor
                case Sensor.TYPE_STEP_COUNTER:
                    if (initialStepsTaken == 0)
                        initialStepsTaken = ((int) event.values[0]);
                    stepsTaken = ((int) event.values[0]);
                    break;
            }
        } catch (Exception ex) {
            //Throw exception
            throw ex;
        }

    }

    /**
     * Method to receive data from smartwatch
     * @param dataEventBuffer - Data layer event
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer)
    {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                //If heart rate has been gathered
                if (item.getUri().getPath().compareTo(HEARTRATE_PATH) == 0)
                {
                    heartRate = Integer.parseInt(dataMap.getString(HEARTRATE_KEY));
                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    /**
     * Broadcast Reciever
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Always call super first
        super.onReceive(context, intent);


        //If custom intent is of an activity recognised
        if(intent.getAction().equals(ACTIVITY_RECOGNITION_INTENT))
        {
            //Temp string to hold current activity
            String tempActivityVar = intent.getStringExtra(ACTIVITY_RECOGNITION_INTENT);

            //Update activity time
            updateActivityTime(tempActivityVar);

            //Set current activity
            user_Activity = tempActivityVar;
        }

        //Check for intent action types
        switch (intent.getAction())
        {

            //if intent is action headset changes
            case Intent.ACTION_HEADSET_PLUG:
            {
                headPhonesIn = (intent.getIntExtra("state", 0) == 1);
                microphoneIn = (intent.getIntExtra("microphone", 0) == 1) && headPhonesIn;
                break;
            }

        }

        //Return
        return;
    }

    /**
     * Method used to update activity time
     * @param activity - Name of activity to update time
     */
    private void updateActivityTime(String activity)
    {
        //Get Calendar instance to retrieve accurate timing
        Calendar rightNow = Calendar.getInstance();

        //Get current elapsed real time
        long elapsedStart = rightNow.getTimeInMillis();

        //Check if activity has happened update total elapsed time
        if(elapsedTimeMap.containsKey(user_Activity) && activity.equals(user_Activity))
        {
            long newTime = (elapsedStart - (elapsedTimeMap.get(TimeStampValue+user_Activity)));

            //Only update time if updated less then or equal to 60seconds and greater than 0
            if(newTime > 0 && newTime <= 60000) {
                elapsedTimeMap.put(user_Activity, newTime + (elapsedTimeMap.get(user_Activity)));
                elapsedTimeMap.put(TimeStampValue + user_Activity, elapsedStart);
            }
            //if something wasnt right counting time set elapsed time again
            else
            {
                elapsedTimeMap.put(TimeStampValue + user_Activity, elapsedStart);
            }
        }
        //If new activity detected from last, clear last activity elapsed time and start elapsed for new one
        else if(elapsedTimeMap.containsKey(user_Activity) && !activity.equals(user_Activity))
        {
            //Reset elapsed of last activity
            elapsedTimeMap.put(TimeStampValue+user_Activity,(long)0);
            //Start Time for new activity
            elapsedTimeMap.put(TimeStampValue+activity,elapsedStart);
        }
        //If activity doesnt exist in map add it and set elapsed time
        else if(!elapsedTimeMap.containsKey(user_Activity) && !user_Activity.equals(""))
        {
            elapsedTimeMap.put(user_Activity,(long)0);
            elapsedTimeMap.put(TimeStampValue+user_Activity,elapsedStart);
        }
    }
}
