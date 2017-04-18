package platform.contextawarenesscore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

import static platform.contextawarenesscore.common.Constants.CUSTOM_BROADCAST_COMMAND;


/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * PrimaryContext is an interface to primary contexts
 */
abstract class PrimaryContext extends BroadcastReceiver implements DataApi.DataListener, SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    //Custom message sending class to send messages to wearable or any other activity
    private MessageSenderClass messageSenderClass;

    /**
     * Getter for messagesenderclass instance use to send messages to activities
     * @return - instance of messagesenderclass
     */
    protected MessageSenderClass getMessageSenderClass() {return messageSenderClass;}

    //Android sensor manager
    private SensorManager sensorManager;

    /**
     * Getter for SensorManager instance
     * @return - instance of SensorManager
     */
    protected SensorManager getSensorManager()
    {
        return sensorManager;
    }

    //Google API client for connecting mobile to wearable
    protected GoogleApiClient googleApiClient;

    //Context of ContextService
    protected Context Context;

    /**
     * Constructor for primary context sets up google api etc
     * @param context - Context of ContextService
     */
    protected PrimaryContext(Context context)
    {
        //Assign context incase something might use it
        Context = context;

        //New instance of sensor manager while binding to context service
        sensorManager = (SensorManager) Context.getSystemService(Context.SENSOR_SERVICE);

        //Instantiate message sender class for communicating between activities
        messageSenderClass = new MessageSenderClass(context);

        //New instance of Google API Client
        //Setup data layer connection to wear device
        googleApiClient = new GoogleApiClient.Builder(Context)
                .addApiIfAvailable(Wearable.API)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Connext to API
        googleApiClient.connect();
    }

    /**
     * onConnected method called when google api connected
     * @param bundle
     */
    public void onConnected(@Nullable Bundle bundle)
    {
        //On connecting to the data layer make this the listener
        Wearable.DataApi.addListener(googleApiClient, this);
        Log.d("Google API Connection: ", "Connected Successfully");
    }

    /**
     * onConnectionSuspended called when google api connection suspended
     * @param i
     */
    public void onConnectionSuspended(int i)
    {
        //Remove this as listener for Google API
        googleApiClient.unregisterConnectionCallbacks(this);
        googleApiClient.unregisterConnectionFailedListener(this);
        //On connecting to the data layer make this the listener
        Wearable.DataApi.removeListener(googleApiClient, this);
    }

    /**
     * onConnectionFailed called when google api connection fails
     * @param connectionResult
     */
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d("Google API Connection: ", "Failed");
    }

    /**
     * onAccuracyChanged method used in Android Sensor API, called when significant change in accuracy detected for sensors.
     * @param sensor
     * @param accuracy
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * onReceive called when a broadcast is received.
     * @param context - Context of the sender
     * @param intent - intent of the sender
     */
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            //If broadcast received is to kill all receivers then do so
            if(intent.getStringExtra(CUSTOM_BROADCAST_COMMAND).equals("Unregister.Receivers"))
                context.unregisterReceiver(this);

        } catch (Exception ex)
        {
            //No custom command
        }
    }

    /**
     * onDataChanged called when google api data layer changes.
     * @param dataEventBuffer - Data layer event
     */
    public void onDataChanged(DataEventBuffer dataEventBuffer) {}

    /**
     * onSensorChanged called when a sensor change occurs.
     * @param event - event that changed.
     */
    public void onSensorChanged(SensorEvent event) {}
}
