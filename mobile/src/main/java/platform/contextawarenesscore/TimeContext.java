package platform.contextawarenesscore;


import android.content.Context;

import java.util.Calendar;

import platform.contextawarenesscore.common.Time;


/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * TimeContext returns time specific context
 */
public class TimeContext extends PrimaryContext
{
    //Calendar object used to get time specifics
    private Calendar calendar;

    //Days array
    private String[] days = {"","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    //Months array
    private String[] months = {"","January","February","March","April","May","June","July","August","September","October","November","December"};
    /**
     * Getter for year.
     * @return - Year as an integer.
     */
    protected int Year()
    {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Getter for day of the year in terms of a 365 day year.
     * @return - Day of the year as an integer.
     */
    protected int DayOfTheYear()
    {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Getter for month.
     * @return - Month.
     */
    protected String Month()
    {
        return months[MonthAsInteger()];
    }

    /**
     * Month as integer
     * @return - Month as integer.
     */
    protected int MonthAsInteger()
    {
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * Getter for day of the month.
     * @return - Day of the month returned as an integer.
     */
    protected int DayOfTheMonth()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Getter for day of the week in a 7 day week monday-sunday.
     * @return - Day in the week in terms of a 7 day week as string.
     */
    protected String Day()
    {
        return days[DayOfTheWeek()];
    }

    /**
     * Day of the week as integer
     * @return - Day as integer
     */
    protected int DayOfTheWeek()
    {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Getter for LocalTime.
     * @return - Time as Time object.
     */
    protected Time Localtime()
    {
        Time time = new Time(calendar.get(Calendar.MILLISECOND));
        return time;
    }

    /**
     * TimeContext constructor
     * @param context - Context of ContextService.
     */
    protected TimeContext(Context context)
    {
        //Call super
        super(context);

        //Setup calendar
        calendar = Calendar.getInstance();
    }
}
