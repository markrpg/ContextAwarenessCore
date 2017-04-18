package platform.contextawarenesscore;


import android.content.Context;

import java.util.ArrayList;

import platform.contextawarenesscore.common.Constants;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * FitnessContext returns fitness specific secondary context
 */
public class FitnessContext extends SecondaryContext
{
    //Calories Counting variables based on MET formula
    private double runningMET = 7.5;
    private double walkingMET = 2.5;
    private double bikeMET = 4.5;
    private double idleMET = 1;

    //Variable to hold last heart rate measured
    private ArrayList<Integer> heartRatesGathered;

    //Variable to hold average heart rate
    private int averageHeartRate;

    /**
     * Get average heart rate
     * @return - Returns average of heartrate
     */
    public int getAverageHeartRate()
    {
        //Local variable to hold heart rate data
        int heartrate = getUserActivityContext().getHeartRate();

        //Get average heart rate from new value
        if(heartrate > 0)
        {
            //Reset average
            averageHeartRate = 0;
            heartRatesGathered.add(heartrate);
            for(Integer hr : heartRatesGathered) {
                averageHeartRate += hr;
            }

            //Return average
            averageHeartRate = (averageHeartRate / heartRatesGathered.size());
            return averageHeartRate;
        }

        //Return last average
        return averageHeartRate;
    }

    //Holds calories burned
    private int caloriesBurned;

    /**
     * Returns total calories burned
     * @return - Calories burned as integer
     */
    public int getCaloriesBurned()
    {
        double totalCal = 0.0175 * runningMET * getIdentityContext().getUserWeightKG() * getUserActivityContext().getActivityTimeSpent(Constants.Running).getMinutesTotal();
        totalCal += 0.0175 * walkingMET * getIdentityContext().getUserWeightKG() * getUserActivityContext().getActivityTimeSpent(Constants.Walking).getMinutesTotal();
        totalCal += 0.0175 * bikeMET * getIdentityContext().getUserWeightKG() * getUserActivityContext().getActivityTimeSpent(Constants.Cycling).getMinutesTotal();
        totalCal += 0.0175 * idleMET * getIdentityContext().getUserWeightKG() * getUserActivityContext().getActivityTimeSpent(Constants.Still).getMinutesTotal();
        return (int) totalCal;
    }

    /**
     * Default constructor for fitness context
     * @param context - the context of the running service.
     */
    public FitnessContext(Context context)
    {
        super(context);

        //Initialise heartrates arraylist
        heartRatesGathered = new ArrayList<Integer>();
    }

}
