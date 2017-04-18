package platform.contextawarenesscore;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * MessageSenderClass - Abstraction of the MessageAPI
 */
public class MessageSenderClass
{

    //Timeout for connecting to GoogleAPI
    private static final long CONNECTION_TIME_OUT_MS = 100;

    //Google API Client
    private GoogleApiClient mobileGoogleAPIClient;

    //Node ID for wearable
    private String nodeId;

    /**
     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
     */
    public MessageSenderClass(Context context)
    {
        mobileGoogleAPIClient = getGoogleApiClient(context);
        retrieveDeviceNode();
    }


    /**
     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mobileGoogleAPIClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mobileGoogleAPIClient).await();

                List<Node> nodes = result.getNodes();

                if (nodes.size() > 0)
                {
                    nodeId = nodes.get(0).getId();
                }

                mobileGoogleAPIClient.disconnect();
            }
        }).start();

    }

    /**
     * Returns a GoogleApiClient that can access the Wear API.
     * @param context the context of the GoogleAPIClient
     * @return A GoogleApiClient that can make calls to the Wear API
     */
    private GoogleApiClient getGoogleApiClient(Context context)
    {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();

    }

    /**
     * Sends a message to the connected mobile device, telling it to show a Toast.
     */
    public void sendToast(String message)
    {
        final String messageToSend = message;

        if (nodeId != null) {

            new Thread(new Runnable() {

                @Override

                public void run() {

                    mobileGoogleAPIClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);

                    Wearable.MessageApi.sendMessage(mobileGoogleAPIClient, nodeId, messageToSend, null);

                    mobileGoogleAPIClient.disconnect();

                }

            }).start();

        } else
        {
            retrieveDeviceNode();
        }

    }
}
