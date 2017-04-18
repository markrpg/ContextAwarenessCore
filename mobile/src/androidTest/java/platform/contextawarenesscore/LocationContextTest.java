package platform.contextawarenesscore;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * LocationContextTest - Unit Tests for LocationContext
 */
public class LocationContextTest
{

    //Context used in unit test
    private Context context;

    //User Activity Context instance
    private LocationContext locationContext;

    /**
     * Setup the Unit Test and mock values
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        //Set context for unit test
        context = InstrumentationRegistry.getTargetContext().getApplicationContext();

        //Instantiate a LocationContext for testing
        locationContext = new LocationContext(context);

        while(locationContext.getLocationInfo() == null);
    }

    /**
     * Test if city returned
     * @throws Exception
     */
    @Test
    public void getCity() throws Exception
    {
        //Assert if city is returned
        assertNotNull(locationContext.getCity());
    }

    /**
     * Test if country is returned
     * @throws Exception
     */
    @Test
    public void getCountry() throws Exception
    {
        //Assert if country is returned
        assertNotNull(locationContext.getCountry());
    }

    /**
     * Test if postcode is returned
     * @throws Exception
     */
    @Test
    public void getPostcode() throws Exception
    {
        //Assert if postcode is returned
        assertNotNull(locationContext.getPostcode());
    }

    /**
     * Test if address is returned
     * @throws Exception
     */
    @Test
    public void getAddress() throws Exception
    {
        //Assert if address is returned
        assertNotNull(locationContext.getAddress());
    }

    /**
     * Test if GPS latitude is returned
     * @throws Exception
     */
    @Test
    public void getGPSLatitude() throws Exception
    {
        //Assert if Latitude is returned
        assertNotNull(locationContext.getGPSLatitude());
    }

    /**
     * Test if GPS longitude is returned
     * @throws Exception
     */
    @Test
    public void getGPSLongitude() throws Exception
    {
        //Assert if longitude is returned
        assertNotNull(locationContext.getGPSLongitude());
    }

}