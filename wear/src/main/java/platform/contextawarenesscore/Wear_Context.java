package platform.contextawarenesscore;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import static platform.contextawarenesscore.common.Constants.*;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * Wear_Context class used to return wear context from devices such as Smartwatches
 */
public class Wear_Context extends Activity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    //Wearable sensor manager
    private SensorManager sensorManager;

    //Integer list arribute used to calculate average heart rate
    private List<Integer> heartRateArray;

    private TextView mTextView;

    /**
     * Method used to send data using Google API data layer to phone
     * @param path    is the path of the message in the pair (eg. "/Heartrate").
     * @param key     is the unique key of the message used to retrieve it from the data layer.
     * @param message is the message being sent to the data layer.
     */
    private void sendDataToPhone(String path, String key, String message)
    {
        //Attempt to connect to mobile phone
        try {

            //Connect through google api
            if (!mobileGoogleAPIClient.isConnected()) mobileGoogleAPIClient.connect();

            //Put heart rate in data layer
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
            putDataMapRequest.getDataMap().putString(key, message);
            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent();
            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(mobileGoogleAPIClient, putDataRequest);

        } catch (Exception ex) {
            Log.d("Error: ", "Problem sending data to phone: " + ex.getMessage());
        }

    }

    //Google API Client
    private GoogleApiClient mobileGoogleAPIClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear__context);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener()
        {
            @Override
            public void onLayoutInflated(WatchViewStub stub)
            {
                mTextView = (TextView) stub.findViewById(R.id.text);

            }
        });

        //Build Google API Client
        mobileGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Connect to Google API Client
        mobileGoogleAPIClient.connect();

        //Initiate sensor service
        initiateService();
    }

    //Initiate sensor service
    private void initiateService()
    {
        //Create new heart rate list
        heartRateArray = new ArrayList<Integer>();

        //Set sensor manager and bind to this service
        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));

        //Heart rate sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        //Get sensor event
        Sensor sensor = sensorEvent.sensor;

        //Determine type of sensor
        switch (sensor.getType()) {
            //If heart rate sensor
            case Sensor.TYPE_HEART_RATE:
                if (sensorEvent.values[0] > 0 && heartRateArray.size() < 10) {
                    heartRateArray.add((int) sensorEvent.values[0]);
                }
                break;
        }

        //If heart rate has been analysed send to phone
        if (heartRateArray.size() == 10) {

            //Get Average heart rate
            int averageHeartRate = 0;
            for (Integer hrValue : heartRateArray)
                averageHeartRate += hrValue;

            //Calculate average heart rate
            String averageHeartBeat = String.valueOf(averageHeartRate / 10);

            //Send average heart rate to data layer
            sendDataToPhone(HEARTRATE_PATH,HEARTRATE_KEY,averageHeartBeat);

            //Clear heart beat array
            //heartRateArray.clear();
            System.exit(0);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Google API Connection: ", "Conntected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}