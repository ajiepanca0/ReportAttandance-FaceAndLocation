package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;


public class LocationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;

    TextView tvlocation,tvnip;
    Button btnUlangi, btnSave;
    ProgressDialog progressDialog;

    SharedPreferences check_absensi;
    SharedPreferences.Editor editor;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        // Check if the location permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed with location retrieval
            getLocation();
        } else {
            // Request the location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        check_absensi = getSharedPreferences("absensi",MODE_PRIVATE);
        editor = check_absensi.edit();

        if (check_absensi.contains("presensi")) {
            Intent intent= new Intent(LocationActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Sudah Melakukan Absensi", Toast.LENGTH_SHORT).show();
        }



        tvlocation = findViewById(R.id.tv_lokasi);
        btnSave = findViewById(R.id.btn_save);
        btnUlangi = findViewById(R.id.btn_ulangi);
        tvnip = findViewById(R.id.tv_nip);


        Bundle extras = getIntent().getExtras();
        String nip = extras.getString("nip");
        tvnip.setText(nip);

        btnUlangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvlocation == null){
                    Toast.makeText(LocationActivity.this, "Lokasi Kosong", Toast.LENGTH_SHORT).show();
                } else if (tvnip == null) {
                    Toast.makeText(LocationActivity.this, "Nip Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    String s_nip = tvnip.getText().toString();
                    String s_location = tvlocation.getText().toString();
                    actionSave(s_nip,s_location);
                }
            }
        });

    }

    private void actionSave(String tvnip, String tvlocation ) {

        String url = "http://192.168.43.161/report/absensi.php";
        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("response");
                            if (status.equals("success")) {
                                editor.putString("presensi","presensi");
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Berhasil Melakukan Absensi", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (status.equals("failed")) {
                                Toast.makeText(getApplicationContext(), "Gagal Melakukan Absensi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {

            Date currentTime = Calendar.getInstance().getTime();

            protected Map<String, String> getParams() {
                Map<String, String> kirim_isi = new HashMap<String, String>();
                kirim_isi.put("nip", tvnip);
                kirim_isi.put("lokasi", tvlocation);
                kirim_isi.put("waktu", currentTime.toString());

                return kirim_isi;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(respon);

    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void getLocation() {
        showLoading();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String locationName = getLocationName(latitude, longitude);
                hideLoading();

                // Use the location name as needed
                tvlocation.setText(locationName);
                Toast.makeText(LocationActivity.this, locationName, Toast.LENGTH_SHORT).show();
                locationManager.removeUpdates(locationListener);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }



    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String locationName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                locationName = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationName;
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with location retrieval
                getLocation();
            } else {
                // Location permission denied, handle accordingly
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop location updates when the activity is destroyed
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}