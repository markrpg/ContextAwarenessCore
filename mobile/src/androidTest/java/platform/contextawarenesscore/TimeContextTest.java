package platform.contextawarenesscore;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * TimeContextTest - Unit Tests for TimeContext
 */
public class TimeContextTest
{
    //Context used in unit test
    private Context context;

    //Private TimeContext instance
    private TimeContext timeContext;

    @Before
    public void setUp() throws Exception
    {
        //Set context for unit test
        context = InstrumentationRegistry.getTargetContext().getApplicationContext();

        //Set time context instance
        timeContext = new TimeContext(context);

    }

    /**
     * Test if a year is returned
     * @throws Exception
     */
    @Test
    public void year() throws Exception
    {
        //Assert valid year
        assertTrue((timeContext.Year() > 1900) && (timeContext.Year() < 2018));
    }

    /**
     * Test if day returned as int
     * @throws Exception
     */
    @Test
    public void dayOfTheYear() throws Exception
    {
        //Make sure a valid day is returned
        assertTrue(timeContext.DayOfTheYear() > 0);
    }

    /**
     * Test if month is returned
     * @throws Exception
     */
    @Test
    public void month() throws Exception
    {
        //assert that a month is returned
        assertNotNull(timeContext.Month());
    }

    /**
     * Test if month returned as int
     * @throws Exception
     */
    @Test
    public void monthAsInteger() throws Exception
    {
        //Assert a valid month range is returned
        assertTrue((timeContext.MonthAsInteger()) > 0 && (timeContext.MonthAsInteger() < 13));
    }

    /**
     * Test if day of the month is returned
     * @throws Exception
     */
    @Test
    public void dayOfTheMonth() throws Exception
    {
        //Assert a valid day of the month range returned
        assertTrue((timeContext.DayOfTheMonth() > 0) && (timeContext.DayOfTheMonth() < 31));
    }

    /**
     * Test if day is returned
     * @throws Exception
     */
    @Test
    public void day() throws Exception
    {
        //Assert if a day is returned
        assertNotNull(timeContext.Day());
    }

    /**
     * Test if day of the week returned
     * @throws Exception
     */
    @Test
    public void dayOfTheWeek() throws Exception
    {
        //Assert if valid day of the week range is returned
        assertTrue((timeContext.DayOfTheWeek() > 0) && (timeContext.DayOfTheWeek() < 8));
    }

    /**
     * Test if local time is returned
     * @throws Exception
     */
    @Test
    public void localtime() throws Exception
    {
        //Assert if a Time object is returned
        assertNotNull(timeContext.Localtime());
    }
}