package com.stingtools.smarttaxi;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.android.volley.VolleyLog.d;

public class MainActivity extends AppCompatActivity {
    Button mAddSensor;
    EditText mSensorName;
    String query;
    EditText mLongitude;
    EditText mLatitude;
    Button mAddData;
    TimePicker mTimePicker;


    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddSensor = (Button) findViewById(R.id.button_add_sensor);
        mSensorName = (EditText) findViewById(R.id.edittext_sensor_name);
        mLatitude = (EditText) findViewById(R.id.editText_latitude);
        mLongitude = (EditText) findViewById(R.id.editText_longitude);
        mAddData = (Button) findViewById(R.id.button_add_data);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);





        mAddSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = mSensorName.getText().toString();
                callWebServiceAddSensor(query);

                Toast.makeText(getApplicationContext(),"Sensor Added", Toast.LENGTH_SHORT).show();

            }
        });

        mAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hour = mTimePicker.getHour();
                minute = mTimePicker.getMinute();

                Log.d("Time: ", hour + " " + minute);
                callWebServiceAddData(mLongitude.getText().toString(),mLatitude.getText().toString(),
                        String.valueOf(hour),String.valueOf(minute));


               //addFakeData();
                Toast.makeText(getApplicationContext(),"Data Added", Toast.LENGTH_SHORT).show();
            }
        });




    }



    private void callWebServiceAddSensor(final String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        //"IDENTITY_KEY: <YOUR_KEY>"
        // http://<your_api_server.com>/catalog/<provider_id>
        // http://catalog.thingtia.cloud/catalog/taxi
        //http://api.thingtia.cloud
        String url = "http://api.thingtia.cloud/catalog/taxi";

        final JSONObject bodyExt = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        final JSONObject bodyInt = new JSONObject();
        jsonArray.put(bodyInt);

        try {
            bodyInt.put("sensor",query);
            bodyInt.put("component","mobil");
            bodyInt.put("dataType","text");
            bodyInt.put("type","otros");
            bodyInt.put("publicAccess","true");


            bodyExt.put("sensors",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("JSON",bodyExt.toString());
                return bodyExt.toString().getBytes();
            }
           @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("IDENTITY_KEY","4d8fdfd38476f44924d7738b1db76e1926d1c83b0986ac07aa3c2f8c973151f7");
                params.put("Content-Type","application/json");

                return params;
            }


        };

        try {
            Log.d("Headers", "" + sr.getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        queue.add(sr);
    }

    private void callWebServiceAddData(final String longitude, final String latitude, final String hour, final String minute) {
        RequestQueue queue = Volley.newRequestQueue(this);

        //"IDENTITY_KEY: <YOUR_KEY>"
        // http://<your_api_server.com>/catalog/<provider_id>
        // http://catalog.thingtia.cloud/catalog/taxi
        //http://api.thingtia.cloud
        String url = "http://api.thingtia.cloud/data/taxi/" + mSensorName.getText();

        final JSONObject bodyExt = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        final JSONObject bodyInt = new JSONObject();
        jsonArray.put(bodyInt);

        try {

            bodyInt.put("value",longitude + " " + latitude + " " +hour+":"+minute);

            bodyExt.put("observations",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("JSON",bodyExt.toString());
                return bodyExt.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("IDENTITY_KEY","4d8fdfd38476f44924d7738b1db76e1926d1c83b0986ac07aa3c2f8c973151f7");
                params.put("Content-Type","application/json");

                return params;
            }

        };

        try {
            Log.d("Headers", "" + sr.getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        queue.add(sr);
    }


    private void callWebServiceAddDataFake(final String sensorName, final String longitude, final String latitude, final String hour, final String minute) {
        RequestQueue queue = Volley.newRequestQueue(this);

        //"IDENTITY_KEY: <YOUR_KEY>"
        // http://<your_api_server.com>/catalog/<provider_id>
        // http://catalog.thingtia.cloud/catalog/taxi
        //http://api.thingtia.cloud
        String url = "http://api.thingtia.cloud/data/taxi/" + sensorName;

        final JSONObject bodyExt = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        final JSONObject bodyInt = new JSONObject();
        jsonArray.put(bodyInt);

        try {

            bodyInt.put("value",longitude + " " + latitude + " " +hour+":"+minute);

            bodyExt.put("observations",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                //Log.d("JSON",sensorName+" "+bodyExt.toString());
                return bodyExt.toString().getBytes();
            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("IDENTITY_KEY","4d8fdfd38476f44924d7738b1db76e1926d1c83b0986ac07aa3c2f8c973151f7");
                params.put("Content-Type","application/json");

                return params;
            }

        };

        try {
            Log.d("Headers", "" + sr.getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        queue.add(sr);
    }

    private void addFakeData() {

        Log.d("Random: ", String.valueOf(myRandom(41387107,41398268)));

        //Add fake sensors

        for (int i = 0; i < 100; i++){
            callWebServiceAddSensor("sensor"+i);
        }



        //Add fake data

        for (int i = 0; i < 100; i++){
            Log.d("TEST: ","sensor"+i);
                    // 1) 00-12
            //callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2125075,2128120)),String.valueOf(myRandom(41387107,41398268)),String.valueOf(myRandomTime(00,12)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2178685,2191128)),String.valueOf(myRandom(41397038,41406073)),String.valueOf(myRandomTime(00,12)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2107404,2113625)),String.valueOf(myRandom(41390633,41396492)),String.valueOf(myRandomTime(00,12)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2060802,2076527)),String.valueOf(myRandom(41303212,41305884)),String.valueOf(myRandomTime(00,12)),String.valueOf(myRandomTime(0,59)));
            // 2) 12-19
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2147206,2151464)),String.valueOf(myRandom(41371899,41376180)),String.valueOf(myRandomTime(12,19)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2178010,2182768)),String.valueOf(myRandom(41390098,41391455)),String.valueOf(myRandomTime(12,19)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2170828,2179037)),String.valueOf(myRandom(41393963,41398493)),String.valueOf(myRandomTime(12,19)),String.valueOf(myRandomTime(0,59)));
            // 3) 19-00
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2192628,2196161)),String.valueOf(myRandom(41386119,41388857)),String.valueOf(myRandomTime(19,23)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2060802,2076527)),String.valueOf(myRandom(41303212,41305884)),String.valueOf(myRandomTime(19,23)),String.valueOf(myRandomTime(0,59)));

            //Log.d("TEST: ","sensor"+i +" " +String.valueOf(myRandom(2125075,2128120))+" " +String.valueOf(myRandom(41387107,41398268))+" " +String.valueOf(myRandomTime(00,12))+" " +String.valueOf(myRandomTime(0,59)));

        }

        for (int i = 0; i < 100; i++){
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2110660,2159636)),String.valueOf(myRandom(41352401,41404843)),String.valueOf(myRandomTime(00,12)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2110660,2159636)),String.valueOf(myRandom(41352401,41404843)),String.valueOf(myRandomTime(12,19)),String.valueOf(myRandomTime(0,59)));
            callWebServiceAddDataFake("sensor"+i,String.valueOf(myRandom(2110660,2159636)),String.valueOf(myRandom(41352401,41404843)),String.valueOf(myRandomTime(19,23)),String.valueOf(myRandomTime(0,59)));
            Log.d("TEST: ","sensor"+i);

        }


    }

    double myRandom(double min, double max) {
        Random r = new Random();
        return (r.nextInt((int)((max-min)*10+1))+min*10) / 10000000.0;
    }
    int myRandomTime(double min, double max) {
        Random r = new Random();
        return (int)(r.nextInt((int)((max-min)*10+1))+min*10)/10;
    }



    @Override
    public View onCreateView(String name, final Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }


}
