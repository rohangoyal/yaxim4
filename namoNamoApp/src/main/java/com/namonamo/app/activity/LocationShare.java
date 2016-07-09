package com.namonamo.app.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import com.namonamo.app.common.Log;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.namonamo.app.R;
import com.namonamo.app.content.PlaceJSONParser;

public class LocationShare extends FragmentActivity implements
		LocationListener {

	SharedPreferences sharedPreferences;
	int locationCount = 0;
	private GoogleMap googleMap;
	private ListView list_places;
	private OnClickListener fullScreenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			View layout_list = findViewById(R.id.layout_list);
			if (layout_list.getVisibility() == View.GONE) {
				layout_list.setVisibility(View.VISIBLE);
				img_fullscreen
						.setImageResource(R.drawable.full_screen_selector);
			} else {
				img_fullscreen
						.setImageResource(R.drawable.small_screen_selector);
				layout_list.setVisibility(View.GONE);
			}
		}
	};
	private Location currentLocation;
	private OnClickListener sendLocation = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendLocation(currentLocation.getLatitude(),
					currentLocation.getLongitude(), "");
		}
	};

	void sendLocation(double latitude, double longitude, String place_name) {
		JSONObject json = new JSONObject();
		try {
			json.put("lat", latitude);
			json.put("long", longitude);
			json.put(
					"place_map",
					"https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=100x100&maptype=roadmap&markers=color:blue|"
							+ latitude + "," + longitude);
			json.put("place_name", place_name);
		} catch (Exception x) {
		}
		Bundle bnd = new Bundle();
		bnd.putString("DATA", json.toString());
		Intent data = new Intent();
		data.putExtras(bnd);
		setResult(RESULT_OK, data);
		finish();
	}

	private OnItemClickListener placeClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, String> hmPlace = LocationShare.this.placeList
					.get(arg2);
			double lat = Double.parseDouble(hmPlace.get("lat"));
			// Getting longitude of the place
			double lng = Double.parseDouble(hmPlace.get("lng"));
			String name = hmPlace.get("place_name");

			sendLocation(lat, lng, name);

		}
	};

	public List<HashMap<String, String>> placeList;
	private ImageView img_fullscreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_share_layout);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);

		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		list_places = (ListView) findViewById(R.id.list_places);
		img_fullscreen = (ImageView) findViewById(R.id.img_fullscreen);
		img_fullscreen.setOnClickListener(fullScreenClick);
		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}
			locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	@Override
	public void onLocationChanged(Location location) {
		this.currentLocation = location;
		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		findViewById(R.id.btn_send_curr_location).setEnabled(true);
		findViewById(R.id.btn_send_curr_location).setOnClickListener(
				sendLocation);

		startNearPlaceTask(location);
	}

	private void startNearPlaceTask(Location location) {
		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		StringBuilder sb = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		sb.append("location=" + location.getLatitude() + ","
				+ location.getLongitude());
		sb.append("&radius=50000");
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyCrKi8sDON_tSXfESImsU2CKnTCrZ8w03I");

		// Creating a new non-ui thread task to download json data
		PlacesTask placesTask = new PlacesTask();

		// Invokes the "doInBackground()" method of the class PlaceTask
		placesTask.execute(sb.toString());
	}

	/** A class, to download Google Places */
	private class PlacesTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {
			ParserTask parserTask = new ParserTask();

			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}

	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {
		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {
			List<HashMap<String, String>> places = null;
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();
			try {
				jObject = new JSONObject(jsonData[0]);
				places = placeJsonParser.parse(jObject);
			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {
			findViewById(R.id.progressBar).setVisibility(View.GONE);

			// Clears all the existing markers
			googleMap.clear();
			if (list == null)
				return;
			for (int i = 0; i < list.size(); i++) {
				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();
				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);
				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat"));
				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng"));
				// Getting name
				String name = hmPlace.get("place_name");
				// Getting vicinity
				String vicinity = hmPlace.get("vicinity");
				LatLng latLng = new LatLng(lat, lng);
				// Setting the position for the marker
				markerOptions.position(latLng);
				// Setting the title for the marker.
				// This will be displayed on taping the marker
				markerOptions.title(name + " : " + vicinity);

				// Placing a marker on the touched position
				googleMap.addMarker(markerOptions);
			}
			LocationShare.this.placeList = list;
			PlaceAdapter adapter = new PlaceAdapter(list);
			list_places.setAdapter(adapter);
			list_places.setOnItemClickListener(placeClick);
		}
	}

	class PlaceAdapter extends BaseAdapter {

		private List<HashMap<String, String>> list;

		public PlaceAdapter(List<HashMap<String, String>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						android.R.layout.simple_list_item_2, null);
			}
			HashMap<String, String> hmPlace = list.get(index);
			String name = hmPlace.get("place_name");
			String vicinity = hmPlace.get("vicinity");
			TextView text1 = (TextView) convertView
					.findViewById(android.R.id.text1);
			TextView text2 = (TextView) convertView
					.findViewById(android.R.id.text2);
			text1.setText(name);
			text2.setText(vicinity);
			return convertView;
		}

	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}
