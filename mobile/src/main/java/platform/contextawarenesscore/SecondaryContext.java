package platform.contextawarenesscore;

import android.content.Context;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * SecondaryContext - Abstracted secondary context functionality
 */

abstract class SecondaryContext
{
    /**
     * Primary Contexts attributes
     */
    private UserActivityContext userActivityContext;
    private TimeContext timeContext;
    private LocationContext locationContext;
    private IdentityContext identityContext;

    /**
     * Getter for UserActivitycontext
     * @return
     */
    protected UserActivityContext getUserActivityContext() {
        return userActivityContext;
    }

    /**
     * Getter for TimeContext
     * @return
     */
    protected TimeContext getTimeContext() {
        return timeContext;
    }

    /**
     * Getter for LocationContext
     * @return
     */
    protected LocationContext getLocationContext() {
        return locationContext;
    }

    /**
     * Getter for IdentityContext
     * @return
     */
    protected IdentityContext getIdentityContext() {
        return identityContext;
    }

    /**
     * Default constructor
     * @param context
     */
    protected SecondaryContext(Context context)
    {
        //Initialise primary contexts
        //User Activity Context
        userActivityContext = new UserActivityContext(context);
        //Time Context
        timeContext = new TimeContext(context);
        //Location Context
        locationContext = new LocationContext(context);
        //Identity Context
        identityContext = new IdentityContext(context);
    }
}
