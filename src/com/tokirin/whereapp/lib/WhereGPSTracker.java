package com.tokirin.whereapp.lib;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public final class WhereGPSTracker {
	public static final double DEFALT_LATITUDE = 37.541;
	public static final double DEFALT_LONGITUDE = 126.986; 
	
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private String provider = "";
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute
	private Context _context;
	
	protected LocationManager locationManager;
	
	public WhereGPSTracker(Context context) {
		_context = context;

		locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);

		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (isAvailable() == false) {
			showSettingsAlert();
		}
	}

	public void requestLocation() {
		if (locationManager != null) {
			if (isGPSEnabled) {
				provider = LocationManager.GPS_PROVIDER;
			} else if (isNetworkEnabled) {
				provider = LocationManager.NETWORK_PROVIDER;
			} else {
				provider = LocationManager.PASSIVE_PROVIDER;
			}

			locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onLocationChanged(Location location) {
				}
			});
		}
	}
	
	public WhereLocation getLastLocation() {
		Location lastLocation = locationManager.getLastKnownLocation(provider);
		
		if (lastLocation != null) {
			return new WhereLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
		}

		return new WhereLocation(DEFALT_LATITUDE, DEFALT_LONGITUDE);
	}
	
	private boolean isAvailable() {
		return isGPSEnabled || isNetworkEnabled; 
	}

	private void showSettingsAlert() {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		_context.startActivity(intent);
			
	}

	public class WhereLocation {
		public double latitude;
		public double longitude;

		public WhereLocation(double _latitude, double _longitude) {
			latitude = _latitude;
			longitude = _longitude;
		}
	}
}