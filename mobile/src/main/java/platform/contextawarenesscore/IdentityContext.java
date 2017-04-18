package platform.contextawarenesscore;

import android.content.Context;


/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * IdentityContext is used to return user specific context
 */

public class IdentityContext extends PrimaryContext
{

    /**
     * Default Constructor for Identity Context
     * @param context
     */
    protected IdentityContext(Context context) {
        super(context);
    }

    /**
     * User Attributes
     */
    private int userAge;
    private String userSex;
    private int userHeightCM;
    private int userWeightKG = 0;

    /**
     * Getter for users age
     * @return
     */
    protected int getUserAge()
    {
        return userAge;
    }

    /**
     * Setter for users age
     * @param userAge
     */
    protected void setUserAge(int userAge)
    {
        this.userAge = userAge;
    }

    /**
     * Getter for users sex
     * @return
     */
    protected String getUserSex()
    {
        return userSex;
    }

    /**
     * Setter for user sex
     * @param userSex
     */
    protected void setUserSex(String userSex)
    {
        this.userSex = userSex;
    }

    /**
     * Getter for users height
     * @return
     */
    protected int getUserHeightCM()
    {
        return userHeightCM;
    }

    /**
     * Setter for users height
     * @param userHeightCM
     */
    protected void setUserHeightCM(int userHeightCM)
    {
        this.userHeightCM = userHeightCM;
    }

    /**
     * Getter for users weight
     * @return
     */
    protected int getUserWeightKG()
    {
        return userWeightKG;
    }

    /**
     * Setter for users weight
     * @param userWeightKG
     */
    protected void setUserWeightKG(int userWeightKG)
    {
        this.userWeightKG = userWeightKG;
    }
}
