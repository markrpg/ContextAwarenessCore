package platform.contextawarenesscore;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by Mark McLaughlin
 * Context Awareness Platform for Android (Honours Project)
 * Edinburgh Napier University (2017)
 * LocationContext returns context used in determining location context
 */
public class LocationContext extends PrimaryContext implements LocationListener
{

    /**
     * Getter for locatinon info list, containing full location information
     * @return
     */
    protected List<Address> getLocationInfo()
    {
        return locationInfo;
    }

    /**
     * Getter for City
     * @return
     */
    protected String getCity()
    {
        return locationInfo.get(0).getLocality();
    }

    /**
     * Getter for Country
     * @return
     */
    protected String getCountry()
    {
        return locationInfo.get(0).getCountryName();
    }

    /**
     * Getter for Postcode
     * @return
     */
    protected String getPostcode()
    {
        return locationInfo.get(0).getPostalCode();
    }

    /**
     * Getter for Address
     * @return
     */
    protected String getAddress()
    {
        return locationInfo.get(0).getAddressLine(0);
    }

    /**
     * Getter for GPS Latitude
     * @return
     */
    protected Double getGPSLatitude()
    {
        return locationInfo.get(0).getLatitude();
    }

    /**
     * Getter for GPS Longitude
     * @return
     */
    protected Double getGPSLongitude()
    {
        return locationInfo.get(0).getLongitude();
    }

    //Geocoder to retrieve location data from location api
    private Geocoder geocoder;

    //Location request
    private LocationRequest mLocationRequest;

    //List to hold retrieved location data
    private List<Address> locationInfo;

    //Location update interval
    private long UPDATE_INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2000;

    /**
     * Default constructor for LocationContext
     * @param context
     */
    protected LocationContext(Context context)
    {
        super(context);

        //Setup Geocoder
        geocoder = new Geocoder(Context, Locale.getDefault());
    }

    /**
     * Overriden onConnected to setup location listener
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);

        // Get last known recent location.
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        // if location isnt null
        if (mCurrentLocation != null)
        {
            //Get location data
            try {
                locationInfo = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates()
    {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                mLocationRequest, this);
    }

    /**
     * Keep location updated on changing location
     * @param location
     */
    @Override
    public void onLocationChanged(Location location)
    {
        //Get location data
        try {
            locationInfo = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
