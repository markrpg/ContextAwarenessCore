package platform.contextawarenesscore;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * ContextClientActivity is a class used to access the ContextService class like a client to a server to access context data
 */
abstract class ContextClientActivity extends Activity
{
    //Context service instance
    protected ContextService contextService;

    //attribute to hold refresh rate of context updating
    protected int contextRefreshTime = 0;

    //Is the service bound to this activity
    protected boolean contextServiceBound = false;

    /**
     * Method used to bind context client to context server if its found to be running
     */
    protected void bindToRunningService()
    {
        //If service is running bind, if not start and check for binding again
        if(isMyServiceRunning("platform.contextawarenesscore.ContextService")) {
            Intent intent = new Intent(this, ContextService.class);
            bindService(intent, mConnection, 0);
            contextServiceBound = true;
        } else
        {
            //Start service
            startContextService();
            //Use method recursion to bind to newly running service
            bindToRunningService();
        }
    }


    //Method to start context awareness service
    protected void startContextService()
    {
        if(!contextServiceBound)
        {
            Intent intent = new Intent(this, ContextService.class);
            startService(intent);
        }

    }

    //Method to stop context awareness service
    protected void stopContextService()
    {
        Intent intent = new Intent(this, ContextService.class);

        //Unbind service
        if(contextServiceBound)
        {
            unbindService(mConnection);
            stopService(intent);
            contextServiceBound = false;
        }
    }

    protected void onStart()
    {
        super.onStart();
        //Bind to running service
        bindToRunningService();
        //Create new timer and override run to enable automated contexts updating on the UI thread
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateContextsTimer();
            }

        }, 0, contextRefreshTime);

    }

    @Override
    protected void onStop()
    {
        if(contextServiceBound) {
            unbindService(mConnection);
            contextServiceBound = false;
        }
        super.onStop();
    }

    //Method used that defines callbacks for context service binding
    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            //If bound to PrimaryContext service, cast IBinder and get PrimaryContext service
            ContextService.LocalBinder binder = (ContextService.LocalBinder) service;
            contextService = binder.getService();
            contextServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            contextServiceBound = false;
        }
    };

    private boolean isMyServiceRunning(String serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.equals(service.service.getClassName())) {
                Log.i("service running", "YES");
                return true;
            }
        }
        Log.i("service running", "NO");
        return false;
    }

    /**
     * Method used to execute contexts update thread
     */
    private void updateContextsTimer()
    {
        this.runOnUiThread(updateContextsRunnable);
    }


    /**
     * Contexts update thread
     */
    private Runnable updateContextsRunnable = new Runnable()
    {
        public void run()
        {
            if(contextService != null) updateContexts();
        }
    };

    /**
     * Abstract method to make sure users implement automated contexts updating on the UI
     */
    protected abstract void updateContexts();

    /**
     * Method to update notification for context service
     * @param notificationText
     */
    protected void changeNotification(String notificationText)
    {
        contextService.updateNotification(notificationText);
    }

}
