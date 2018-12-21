package com.example.lynar.boussole;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class Boussole extends AppCompatActivity implements SensorEventListener {

    private final String TAG = "Boussole";
    private ImageView boussole;
    private TextView lat;
    private TextView lon;
    private TextView A_x;
    private TextView A_y;
    private TextView A_z;
    private TextView Magnetique;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private SensorManager senSensorManager2;
    private Sensor senMagnetique;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boussole);
        Log.d(TAG, "OnCreate Function");
        lat = findViewById(R.id.textView12);
        lon = findViewById(R.id.textView14);
        A_x = findViewById(R.id.textView17);
        A_y = findViewById(R.id.textView15);
        A_z = findViewById(R.id.textView19);
        Magnetique = findViewById(R.id.textView21);

        ///   TP5 - Question 3
        /// utilisation du locationManager pour afficher la latitude et la longitude
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ///   TP5 - Question 4
        /// utilisation des SensorManager pour avoir acces à l'accelerometre et au capteur de champ magnetique
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senSensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senMagnetique = senSensorManager2.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /// TP5 - Question 5
        /// Register to Listener to know when modification  happen
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager2.registerListener(this, senMagnetique , SensorManager.SENSOR_DELAY_NORMAL);

        /// TP5 - Question 7
        /// Demander la permission si elle n'est pas accorder pour acceder au information de géolocalisation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        ///   TP5 - Question 3
        /// utilisation du locationListener pour détecter les modification de latitude et longitude
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d(TAG, "Latitude " + latitude + " Logitude " + longitude);
                lat.setText(String.valueOf(latitude));
                lon.setText(String.valueOf(longitude));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Log.d(TAG, "OnCreate end Function");

    }

    @Override
    public void onStart() {

        //Declaration of elements of the user interface
        super.onStart();

        /// TP5 - Question 2  Utilisation de la fonction setRotation
        Button buttonG = findViewById(R.id.button5);
        Button buttonD = findViewById(R.id.button6);
        boussole = findViewById(R.id.imageView2);
        Log.d(TAG, "OnStart Function");

        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException {
                boussole.setRotation(boussole.getRotation() - 30);
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException {
                boussole.setRotation(boussole.getRotation() + 30);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        senSensorManager.unregisterListener(this);
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager2.registerListener(this, senMagnetique, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        locationManager.removeUpdates(locationListener);
    }

    /**
     * Called when there is a new sensor event.  Note that "on changed"
     * is somewhat of a misnomer, as this will also be called if we have a
     * new reading from a sensor with the exact same sensor values (but a
     * newer timestamp).
     *
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     *
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

                A_x.setText("X:" + String.valueOf(x));
                A_y.setText("Y:" +String.valueOf(y));
                A_z.setText("Z:" +String.valueOf(z));
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];

            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));

            // set value on the screen
            Magnetique.setText("MAG:" + String.valueOf(magnitude));
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     *
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
