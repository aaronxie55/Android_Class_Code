package com.demo.irewards.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.demo.irewards.activity.CreateProfileActivity;

import java.io.IOException;
import java.util.List;

public class LocationUtil {

    private Context context;
    private LocationCallback callback;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public LocationUtil(Context context) {
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
    }

    public void setCallback(LocationCallback callback) {
        this.callback = callback;
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(context);
            try {
                addressList = geocoder.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
//                callback.onSuccess(address.getLocality(), address.getAdminArea(), );
                System.out.println(address.getLocality() + ", " + address.getAdminArea());
                locationManager.removeUpdates(locationListener);
                callback.onSuccess("Mountain View", "California");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public interface LocationCallback {
        void onSuccess(String City, String state);
    }
}
