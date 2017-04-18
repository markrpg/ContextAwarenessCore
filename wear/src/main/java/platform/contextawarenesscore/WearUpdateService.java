package platform.contextawarenesscore;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import platform.contextawarenesscore.common.Constants;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * WearUpdateService class used to listen for messages from phone
 */
public class WearUpdateService extends WearableListenerService
{
    //Google API Client
    private GoogleApiClient mobileGoogleAPIClient;

    /**
     * onMessageReceived method override, captures messages sent from mobile phone.
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent)
    {
        if(messageEvent.getPath().equals(Constants.HeartRateMessage)) {
            startContextActivity();
        }
    }

    /**
     * Method used to start the context wear activity to get new context values
     */
    private void startContextActivity()
    {
        Intent intent = new Intent(this , Wear_Context.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
