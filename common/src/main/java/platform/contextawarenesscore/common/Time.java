package platform.contextawarenesscore.common;


/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * Time - Custom time class used to store time of activities
 */
public class Time
{
    //Private attributes for time
    private int days = 0;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int milliseconds = 0;

    //Getters and setters
    public int getDays()
    {
        return days;
    }

    public void setDays(int days)
    {
        this.days = days;
    }

    public int getHours()
    {
        return hours;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getMinutesTotal() { if(milliseconds > 0) return ((milliseconds /= 1000) / 60); else return 0;}

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }

    public void setMilliseconds(int milliseconds) { this.milliseconds = milliseconds;}

    /**
     * Set time by milliseconds in default constructor
     * @param ms - Time as milliseconds.
     */
    public Time(int ms)
    {
        if(ms > 0) {
            //Convert to minutes
            int time = ms / 1000;

            //Set milliseconds
            milliseconds = ms;
            //Set local attributes
            seconds = time % 60;
            time /= 60;
            minutes = time % 60;
            time /= 60;
            hours = time % 24;
            time /= 24;
            days = time;
        }
    }

    /**
     * Overritten tostring to return time
     * @return
     */
    @Override
    public String toString()
    {
        String strDay = "D: " + days;
        String strHour = " H: " + hours;
        String strMin = " M: "+minutes;
        String strSec = " S: "+seconds;
        return String.format("%s:%s:%s:%s",strDay,strHour,strMin,strSec);
    }
}
