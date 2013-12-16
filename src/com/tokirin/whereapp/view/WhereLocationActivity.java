package com.tokirin.whereapp.view;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokirin.whereapp.R;
import com.tokirin.whereapp.lib.WhereGPSTracker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WhereLocationActivity extends FragmentActivity implements OnMapClickListener{

	GoogleMap googleMap;
	LatLng myPoint = null;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		context = this.getBaseContext();
		 // Getting Google Play availability status
	    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

	    // Showing status
	    if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available

	        int requestCode = 10;
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	        dialog.show();

	    } else {
	        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	        googleMap = fm.getMap();
	        googleMap.setMyLocationEnabled(true);

	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	        Criteria criteria = new Criteria();

	        String bestProvider = locationManager.getBestProvider(criteria, false);
	        Location location = locationManager.getLastKnownLocation(bestProvider);

			Double mLat = getIntent().getDoubleExtra("latitude", WhereGPSTracker.DEFALT_LATITUDE);
	        Double mLong = getIntent().getDoubleExtra("longitude", WhereGPSTracker.DEFALT_LONGITUDE);

	        CameraUpdate seletedLocation = CameraUpdateFactory.newLatLng(new LatLng(mLat, mLong));
		    googleMap.moveCamera(seletedLocation);
	        
	        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            googleMap.animateCamera(zoom);
            googleMap.setOnMapClickListener(this);
            
            Button submitBtn = (Button)findViewById(R.id.map_submit);
            submitBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(myPoint == null){
						Toast.makeText(context, "아직 위치를 설정하지 않으셨습니다", Toast.LENGTH_SHORT);
					}else{
						double lat = myPoint.latitude;
						double lng = myPoint.longitude;
						Intent intent = new Intent();
						intent.putExtra("latitude", lat);
						intent.putExtra("longitude", lng);
						setResult(RESULT_OK,intent);
						finish();
					}
				}
			});
	    }
	}

	@Override
	public void onMapClick(LatLng point) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(point);
		
		googleMap.clear();
		Toast.makeText(this.getBaseContext(), point.toString(), 100).show();
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
		googleMap.addMarker(markerOptions);
		myPoint = point;
		
	}
	
	

}
