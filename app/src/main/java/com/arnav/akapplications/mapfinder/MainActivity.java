package com.arnav.akapplications.mapfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.PlaceDetectionClient;
//import com.google.android.gms.location.places.PlaceLikelihood;
//import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
//import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;
import com.google.android.libraries.places.api.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.arnav.akapplications.mapfinder.AppConfig.GEOMETRY;
import static com.arnav.akapplications.mapfinder.AppConfig.GOOGLE_BROWSER_API_KEY;
import static com.arnav.akapplications.mapfinder.AppConfig.ICON;
import static com.arnav.akapplications.mapfinder.AppConfig.LATITUDE;
import static com.arnav.akapplications.mapfinder.AppConfig.LOCATION;
import static com.arnav.akapplications.mapfinder.AppConfig.LONGITUDE;
import static com.arnav.akapplications.mapfinder.AppConfig.NAME;
import static com.arnav.akapplications.mapfinder.AppConfig.OK;
import static com.arnav.akapplications.mapfinder.AppConfig.PLACE_ID;
import static com.arnav.akapplications.mapfinder.AppConfig.PROXIMITY_RADIUS;
import static com.arnav.akapplications.mapfinder.AppConfig.REFERENCE;
import static com.arnav.akapplications.mapfinder.AppConfig.STATUS;
import static com.arnav.akapplications.mapfinder.AppConfig.SUPERMARKET_ID;
import static com.arnav.akapplications.mapfinder.AppConfig.VICINITY;
import static com.arnav.akapplications.mapfinder.AppConfig.ZERO_RESULTS;
import static com.arnav.akapplications.mapfinder.AppConfig.art_gallery;
import static com.arnav.akapplications.mapfinder.AppConfig.atm;
import static com.arnav.akapplications.mapfinder.AppConfig.bakery;
import static com.arnav.akapplications.mapfinder.AppConfig.bank;
import static com.arnav.akapplications.mapfinder.AppConfig.bar;
import static com.arnav.akapplications.mapfinder.AppConfig.book_store;
import static com.arnav.akapplications.mapfinder.AppConfig.gas_station;
import static com.arnav.akapplications.mapfinder.AppConfig.grocery_or_supermarket;
import static com.arnav.akapplications.mapfinder.AppConfig.gym;
import static com.arnav.akapplications.mapfinder.AppConfig.hospital;
import static com.arnav.akapplications.mapfinder.AppConfig.pharmacy;
import static com.arnav.akapplications.mapfinder.AppConfig.police;
import static com.arnav.akapplications.mapfinder.AppConfig.restaurant;
import static com.arnav.akapplications.mapfinder.AppConfig.shopping_mall;
import static com.arnav.akapplications.mapfinder.AppConfig.transit_station;
import static com.arnav.akapplications.mapfinder.AppConfig.university;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    public static final String GOOGLE_ACCOUNT = "google_account";
    private static final String TAG = MainActivity.class.getSimpleName();
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    TextView profileName,profileEmailID;
    ImageView profilePhoto;

    GoogleSignInAccount googleSignInAccount;
    GoogleSignInClient googleSignInClient;

//    private GeoDataClient geoDataClient;
//    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private Location lastKnownLocation;
    private CameraPosition cameraPosition;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    public double latitude,longitude;
    PlacesClient placesClient;
    String name,email,userID;

    private boolean LocationPermissionGranted;
    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 12;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int M_MAX_ENTRIES = 5;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private String[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLng;
    String pointOfInterest = "point_of_interest";

    public static final String PROFILE_USER_ID = "USER_ID";
    public static final String PROFILE_NAME = "PROFILE_NAME";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";

    SupportMapFragment supportMapFragment;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
        {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_main);

//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .build();
//        googleApiClient.connect();

        Places.initialize(getApplicationContext(), "api_key");
        placesClient = Places.createClient(this);

//        googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


//        geoDataClient = Places.getGeoDataClient(this);
//        placeDetectionClient = Places.getPlaceDetectionClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        NavigationView navigationView=findViewById(R.id.navigation_view);

//        profileName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
//        profileEmailID = navigationView.getHeaderView(0).findViewById(R.id.profileEmailID);
//        profilePhoto = navigationView.getHeaderView(0).findViewById(R.id.profilePhoto);
//
//
//        Uri photoUrl = googleSignInAccount.getPhotoUrl();
//
//        profileName.setText(googleSignInAccount.getDisplayName());
//        profileEmailID.setText(googleSignInAccount.getEmail());
//        Picasso.with(getApplicationContext())
//                .load(photoUrl.toString())
//                .placeholder(android.R.drawable.sym_def_app_icon)
//                .resize(100, 100)
//                .transform(new CircleTransform())
//                .centerCrop()
//                .into(profilePhoto);

        navigationView.setNavigationItemSelectedListener(   new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);
                switch(item.getItemId())
                {
                    case R.id.search_place:  //do something
                        Intent intent = new Intent(MainActivity.this,SearchPlaceActivity.class);
                        startActivity(intent);
                        break;

//                    case R.id.logout:
//                        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                            }
//                        });
//                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        getDeviceLocation(pointOfInterest);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mMap != null)
        {
            outState.putParcelable(KEY_CAMERA_POSITION,mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION,lastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.option_get_place:
                openPlacesDialog();
                break;

        }
        return true;
    }

    private void updateLocationUI()
    {
        if(mMap == null)
            return;

        try
        {
            if(LocationPermissionGranted)
            {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            else
            {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        }
        catch (SecurityException e)
        {
            Log.e("Exception: %s",e.getMessage() );
        }
    }

    private void getLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationPermissionGranted = true;
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_ACCESS_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationPermissionGranted = false;
        switch(requestCode)
        {
            case PERMISSION_REQUEST_ACCESS_LOCATION:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void getDeviceLocation(String type)
    {
        try
        {
            if(LocationPermissionGranted)
            {
                locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

                Location location = locationManager.getLastKnownLocation(bestProvider);
                if(location != null)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),DEFAULT_ZOOM));
                    loadNearByPlaces(location.getLatitude(), location.getLongitude(),type);
                }
                else
                {
                    locationManager.requestLocationUpdates(bestProvider,1000,0,this);

                }

            }
        }
        catch(SecurityException e)
        {
            Log.e("Exception : %s" , e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,null);

                TextView title = ((TextView) infoWindow.findViewById(R.id.placeName));
//                TextView snippet = ((TextView)infoWindow.findViewById(R.id.snippet));

                title.setText(marker.getTitle());
//                LatLng latLng = marker.getPosition();

//                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        getLocationPermission();

        updateLocationUI();

        getDeviceLocation(pointOfInterest);

    }

    private void showCurrentPlace()
    {
        if(mMap == null) {
            Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();
        }

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();


       if(LocationPermissionGranted) {
           try {

               placesClient.findCurrentPlace(request).addOnSuccessListener(new OnSuccessListener<FindCurrentPlaceResponse>() {
                   @Override
                   public void onSuccess(FindCurrentPlaceResponse findCurrentPlaceResponse) {
                       for (PlaceLikelihood placeLikelihood : findCurrentPlaceResponse.getPlaceLikelihoods()) {
                           Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                   placeLikelihood.getPlace().getName(),
                                   placeLikelihood.getLikelihood()));
//                   textView.append(String.format("Place '%s' has likelihood: %f\n",
//                           placeLikelihood.getPlace().getName(),
//                           placeLikelihood.getLikelihood()));
                           int count, i = 0;
                           count = M_MAX_ENTRIES;

                           likelyPlaceNames = new String[count];
                           likelyPlaceAddresses = new String[count];
                           likelyPlaceAttributions = new String[count];
                           likelyPlaceLatLng = new LatLng[count];

                           for (i = 0; i <count; i++) {
                               likelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                               likelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
                               likelyPlaceLatLng[i] = placeLikelihood.getPlace().getLatLng();
                               if (i > (count - 1)) {
                                   break;
                               }
                           }
                       }

                       openPlacesDialog();

                   }
               });
           }
           catch(Exception e) {
           Log.e(TAG,"Exception :%s" + e.getMessage());
            }

//           @SuppressLint("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.getCurrentPlace(null);
//
//           placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//               @Override
//               public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                   if(task.isSuccessful() && task.getResult()!= null) {
//                       PlaceLikelihoodBufferResponse placeLikelihoodBufferResponse = task.getResult();
//
//                       int count,i=0;
//                       count = M_MAX_ENTRIES;
//
////                       if(placeLikelihoodBufferResponse.getCount() < M_MAX_ENTRIES) {
////                           count = placeLikelihoodBufferResponse.getCount();
////                       }
////                       else {
////
////                       }
//
//                       likelyPlaceNames = new String[count];
//                       likelyPlaceAddresses = new String[count];
//                       likelyPlaceAttributions = new String[count];
//                       likelyPlaceLatLng = new LatLng[count];
//
//                       for(i=0;i<count;i++)     {
//                           likelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                           likelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
//                           likelyPlaceAttributions[i] = (String) placeLikelihood.getPlace().getAttributions();
//                           likelyPlaceLatLng[i] = placeLikelihood.getPlace().getLatLng();
//
//                           i++;
//                           if (i > (count - 1)) {
//                               break;
//                           }
//                       }
//                       placeLikelihoodBufferResponse.release();
//
//                       openPlacesDialog();
//                   }
//                   else
//                   {
//                       Log.e(TAG,"Exception :%s" + task.getException());
//                   }
//               }
//           });
       }
       else
       {
           Log.i(TAG,"the user did not grant location permission");

           mMap.addMarker(new MarkerOptions().title(getString(R.string.default_info_title)).position(mDefaultLocation).snippet(getString(R.string.default_info_snippet)));

           getLocationPermission();
       }
    }

    private void openPlacesDialog()
    {
        listView = new ListView(this);


        String[] placeTypes = {"ATM","Art Gallery","Bakery","Book Store","Bank","Bar","Gas Station","Hospital","Gym","Pharmacy","Police Station","Restaurant","Shopping","Supermarket","Transit Station","University"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.custom_info_contents,R.id.placeName,placeTypes);

        listView.setAdapter(adapter);

        final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.pick_place).setView(listView).show();
        dialog.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ViewGroup viewGroup = (ViewGroup) view;

//                TextView title = viewGroup.findViewById(R.id.placeName);

//                Toast.makeText(MainActivity.this,title.getText(),Toast.LENGTH_LONG).show();
                switch(position)
                {
                    case 0:
                        getDeviceLocation(atm);
                        dialog.dismiss();
                        break;
                    case 1:
                        getDeviceLocation(art_gallery);
                        dialog.dismiss();
                        break;
                    case 2:
                        getDeviceLocation(bakery);
                        dialog.dismiss();
                        break;
                    case 3:
                        getDeviceLocation(book_store);
                        dialog.dismiss();
                        break;
                    case 4:
                        getDeviceLocation(bank);
                        dialog.dismiss();
                        break;
                    case 5:
                        getDeviceLocation(bar);
                        dialog.dismiss();
                        break;
                    case 6:
                        getDeviceLocation(gas_station);
                        dialog.dismiss();
                        break;
                    case 7:
                        getDeviceLocation(hospital);
                        dialog.dismiss();
                        break;
                    case 8:
                        getDeviceLocation(gym);
                        dialog.dismiss();
                        break;
                    case 9:
                        getDeviceLocation(pharmacy);
                        dialog.dismiss();
                        break;
                    case 10:
                        getDeviceLocation(police);
                        dialog.dismiss();
                        break;
                    case 11:
                        getDeviceLocation(restaurant);
                        dialog.dismiss();
                        break;
                    case 12:
                        getDeviceLocation(shopping_mall);
                        dialog.dismiss();
                        break;
                    case 13:
                        getDeviceLocation(grocery_or_supermarket);
                        dialog.dismiss();
                        break;
                    case 14:
                        getDeviceLocation(transit_station);
                        dialog.dismiss();
                        break;
                    case 15:
                        getDeviceLocation(university);
                        dialog.dismiss();
                        break;
                }
            }
        });

//        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                LatLng markerLatLng = likelyPlaceLatLng[which];
////                String markerSnippet = likelyPlaceAddresses[which];
////                if(likelyPlaceAttributions[which] != null)
////                {
////                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
////                }
////
////                mMap.addMarker(new MarkerOptions().title(likelyPlaceNames[which]).position(markerLatLng).snippet(markerSnippet));
////
////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,DEFAULT_ZOOM));
//
//
//
//            }
//        };

    }


    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


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

    private void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                mMap.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    id = place.getString(SUPERMARKET_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);

                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(latitude, longitude);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);

                    mMap.addMarker(markerOptions);
                }

                Toast.makeText(getBaseContext(), jsonArray.length() + " " + "locations found",
                        Toast.LENGTH_LONG).show();
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Toast.makeText(getBaseContext(), "No location found in your vicinity!!!",
                        Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }

    private void loadNearByPlaces(double latitude, double longitude,String type) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works
//        String type = "grocery_or_supermarket";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);

        JSONObject object = null;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,googlePlacesUrl.toString(),object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {

                        Log.i(TAG, "onResponse: Result= " + result.toString());
                        parseLocationResult(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Error= " + error);
                        Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });


        AppController.getInstance().addToRequestQueue(request);
    }




}
