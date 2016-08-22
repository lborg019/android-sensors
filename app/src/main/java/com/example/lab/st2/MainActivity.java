package com.example.lab.st2;


import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private boolean linFlag = false;
    private boolean accelFlag = false;
    private boolean gyroFlag = false;
    private TextView xTextL, yTextL, zTextL, xTextA, yTextA, zTextA, xTextG, yTextG, zTextG;
    //private Sensor linearAccelSensor, accelSensor; //we move these to local scope (optimized).
    //private SensorManager SM;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //Context:
    /*private static Context myContext;
    public static Context getAppContext(){
        return MainActivity.myContext;
    }*/

    public String timeStamp()
    {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }

    public void write(String data, int mode, boolean flag){
        String file_name = "";
        String heading="Time,X,Y,Z\n";

        //mode flag selects correct file to write to
        if(mode==1)//Linear
        {
            file_name = "linear-log.txt";
            if(!flag)//first time writing, we write headings:
            {
                //write to selected file
                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_APPEND);
                    fileOutputStream.write(heading.getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            linFlag=true;
        }
        if(mode==2)//acceleration
        {
            file_name = "accelerometer-log.txt";//raw
            if (!flag)//first time writing, we write headings:
            {
                //write to selected file
                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_APPEND);
                    fileOutputStream.write(heading.getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            accelFlag=true;
        }
        if(mode==3)//gyroscope
        {
            file_name = "gyroscope-log.txt";
            if(!flag)//first time writing, we write headings:
            {
                //write to selected file
                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_APPEND);
                    fileOutputStream.write(heading.getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            gyroFlag=true;
        }

        //write to selected file
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_APPEND);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sensor linearAccelSensor, accelSensor, gyroSensor;
        SensorManager SM;

        //create sensor manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        //accelerometer sensor
        linearAccelSensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accelSensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //Register sensor Listener
        SM.registerListener(this, linearAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Assign TextView
        //Linear Acceleration
        xTextL = (TextView) findViewById(R.id.xTextL);
        yTextL = (TextView) findViewById(R.id.yTextL);
        zTextL = (TextView) findViewById(R.id.zTextL);

        //Acceleration
        xTextA = (TextView) findViewById(R.id.xTextA);
        yTextA = (TextView) findViewById(R.id.yTextA);
        zTextA = (TextView) findViewById(R.id.zTextA);

        //Gyroscope
        xTextG = (TextView) findViewById(R.id.xTextG);
        yTextG = (TextView) findViewById(R.id.yTextG);
        zTextG = (TextView) findViewById(R.id.zTextG);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        //LINEAR_ACCELERATION
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            String tag = "linear acceleration";

            //================= LOW PASS FILTER =================//
            //filter x value
            if (event.values[0] > -0.15 && event.values[0] < 0.15)
                event.values[0] = 0;
            //filter Y value
            if (event.values[1] > -0.10 && event.values[1] < 0.20)
                event.values[1] = 0;
            //filter Z value
            if (event.values[2] > 0.1 && event.values[2] < 0.4)
                event.values[2] = 0;

            String _value0 = "X: " + event.values[0];
            String _value1 = "Y: " + event.values[1];
            String _value2 = "X: " + event.values[2];

            String timeStamp = timeStamp();//get UNIX Epoch time
            String log = timeStamp+","+event.values[0]+","+event.values[1]+","+event.values[2]+"\n";//prepare text

            write(log,1,linFlag);//write to file

            xTextL.setText(_value0); //write to UI
            yTextL.setText(_value1);
            zTextL.setText(_value2);
        }
        //ACCELEROMETER

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            String tag = "accelerometer";
            String _value0 = "X: " + event.values[0];
            String _value1 = "Y: " + event.values[1];
            String _value2 = "Z: " + event.values[2];

            String timeStamp = timeStamp();//get UNIX Epoch time
            String log = timeStamp+","+event.values[0]+","+event.values[1]+","+event.values[2]+"\n";//prepare text
            write(log,2,accelFlag);

            xTextA.setText(_value0);//write to UI
            yTextA.setText(_value1);
            zTextA.setText(_value2);
        }
        //GYROSCOPE
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){

            String tag = "gyroscope";
            String _value0 = "X: " + event.values[0];
            String _value1 = "Y: " + event.values[1];
            String _value2 = "Z: " + event.values[2];

            String timeStamp = timeStamp();//get UNIX Epoch time
            String log = timeStamp+","+event.values[0]+","+event.values[1]+","+event.values[2]+"\n";//prepare text
            write(log,3,gyroFlag);//write to file

            xTextG.setText(_value0);//write to UI
            yTextG.setText(_value1);
            zTextG.setText(_value2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lab.st2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lab.st2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

