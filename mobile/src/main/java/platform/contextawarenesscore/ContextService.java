package platform.contextawarenesscore;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.android.gms.location.LocationListener;

import static platform.contextawarenesscore.common.Constants.NOTIFICATION_ID;

public class ContextService extends Service
{
    /**
     * Getter for fitness context
     * @return - Fitness context
     */
    protected FitnessContext getFitnessContext()
    {
        return fitnessContext;
    }

    //Private FitnessContext attribute
    private FitnessContext fitnessContext;

    //Binder used to bind to clients
    private final IBinder contextServiceBinder = new LocalBinder();


    @Override
    public void onCreate()
    {

        super.onCreate();

        //Start notification in foreground
        this.startForeground();

        //Initiate context service variables
        initiateService();


    }

    //Initiate context service
    private void initiateService()
    {
        //Initiate secondary contexts
        fitnessContext = new FitnessContext(this);
    }

    @Override
    public void onDestroy()
    {
        //Call destroy for super
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return contextServiceBinder;
    }

    //Local service binder class used to get service reference for accessing it
    public class LocalBinder extends Binder
    {
        ContextService getService()
        {
            return ContextService.this;
        }
    }

    /**
     * Method used to start notification
     */
    private void startForeground() {
        startForeground(NOTIFICATION_ID, getMyActivityNotification("Context Awareness Service running."));
    }

    /**
     * Method used to build notification and set title, text etc
     * @param notificationText - Text for notification
     * @return - Returns built notification
     */
    private Notification getMyActivityNotification(String notificationText){
        // The PendingIntent to launch our activity if the user selects
        // this notification
        CharSequence title = getText(R.string.service_title);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, FitnessContextDemo.class), 0);

        //Build notification
        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_expand_00014)
                .setContentIntent(contentIntent).getNotification();
    }

    /**
     * Method to update notification text
     */
    protected void updateNotification(String notificationText)
    {

        Notification notification = getMyActivityNotification(notificationText);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}