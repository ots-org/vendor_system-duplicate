package com.ortusolis.rotarytaranaadmin.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.VolleyError
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.ortusolis.rotarytaranaadmin.Activity.MainActivity
import com.ortusolis.rotarytaranaadmin.Activity.WaterApp.CHANNEL_ID
import com.ortusolis.rotarytaranaadmin.NetworkUtility.Constants
import com.ortusolis.rotarytaranaadmin.NetworkUtility.IResult
import com.ortusolis.rotarytaranaadmin.NetworkUtility.WebserviceController
import com.ortusolis.rotarytaranaadmin.R
import com.ortusolis.rotarytaranaadmin.pojo.LoginResponse
import com.google.gson.Gson

import org.json.JSONObject

import java.util.Timer
import java.util.TimerTask

import locationprovider.davidserrano.com.LocationProvider

class UserService : Service() , GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener {

    override fun onConnected(p0: Bundle?) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        startLocationUpdates();
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(location : Location?) {
        mLocation = location
        latS =  location?.latitude.toString()
        lonS = location?.longitude.toString()

    }

    internal lateinit var con: Context
    internal var dateStr = ""
    internal var timeStr = ""
    var TAG = "UserService"
    internal var latS = "0"
    internal var lonS = "0"
    internal var mLocation : Location? = null
    private lateinit var lProvider: LocationProvider
    lateinit var sharedPreferences : SharedPreferences
    lateinit var gson : Gson
    private var isGPS = false

    var mGoogleApiClient : GoogleApiClient? = null;
    var mLocationRequest : LocationRequest? = null;
    var UPDATE_INTERVAL : Long = 15000;  /* 15 secs */
    var FASTEST_INTERVAL : Long = 5000; /* 5 secs */

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    private val bothLocationsRequestCode = 132

    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()

        sharedPreferences = getSharedPreferences("water_management", 0)
        gson = Gson()
        con = this@UserService

        //startLocationUpdates()

        val handler = Handler()
        val timer = Timer()

        mGoogleApiClient = GoogleApiClient.Builder(this@UserService)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }

        val doAsynchronousTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {

                        SendUserLoginTime()

                    } catch (e: Exception) {
                        // TODO Auto-generated catch block
                    }
                }
            }
        }
        timer.schedule(doAsynchronousTask, 0, 30000)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this@UserService, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this@UserService, 0, notificationIntent, 0)

        var text :String = if( latS.isEmpty()){ "Searching For Location"} else{  "Location set by GPS"}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

         var builder : Notification.Builder = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setAutoCancel(true);

         var notification:Notification = builder?.build();
        startForeground(1, notification);

    } else {

        var builder:NotificationCompat.Builder  = NotificationCompat.Builder(this@UserService)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        var notification : Notification  = builder.build();

        startForeground(1, notification);
    }
    return START_NOT_STICKY;
    }

    private fun SendUserLoginTime() {
        // TODO Auto-generated method stub

        SendRequest()
    }

    private fun SendRequest() {
        if (!TextUtils.isEmpty(sharedPreferences.getString("userid", "")) && !TextUtils.isEmpty(latS) && !TextUtils.isEmpty(lonS)) {
            val wss = WebserviceController(this@UserService)

            val requestObject = JSONObject()

            val jsonObject = JSONObject()
            try {
                jsonObject.put("latitude", latS)
                jsonObject.put("longitude", lonS)
                jsonObject.put("userId", sharedPreferences.getString("userid", ""))
                jsonObject.put("latLongId", "null")

                requestObject.put("requestData", jsonObject)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.e("from service request", requestObject.toString());

            wss.postLoginVolley(Constants.updateEmpLatLong, requestObject.toString(), object : IResult {
                override fun notifySuccess(response: String, statusCode: Int) {
                    try {
                        Log.e("from service response", response)

                        val responseData = gson.fromJson(response, LoginResponse::class.java)

                        if (responseData.getResponseCode().equals("200", ignoreCase = true)) {


                        } else {
//                            Toast.makeText(this@UserService, if (TextUtils.isEmpty(responseData.getResponseDescription())) "Location Failed" else responseData.getResponseDescription(), Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun notifyError(error: VolleyError) {
                    try {
//                        Toast.makeText(this@UserService, WebserviceController.returnErrorMessage(error)!! + "", Toast.LENGTH_LONG).show()
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }

                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Destroyed")
        sendBroadCast()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        sendBroadCast()

        super.onTaskRemoved(rootIntent)
    }

    internal fun sendBroadCast() {
        Log.e("Send BroadCast", ">>>>>>>>>>")
        val broadcastIntent = Intent("com.ionsales.RestartIntent")
        sendBroadcast(broadcastIntent)
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this@UserService, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@UserService, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {

        } else {
            //if (isContinue) {
            mFusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
            /*} else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this@MainActivity) { location ->
                    if (location != null) {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                        txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude))
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                    }
                }
            }*/
        }
    }

    internal fun permissionGranted() {

        //handler.postDelayed(runnable,30000);
        /*}else if (handler!=null){
            startStop.setText("Start");
            handler.removeCallbacks(runnable);*/
    }

    private fun startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this@UserService,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            permissionGranted()
        }

        Log.i("startLocationUpdates",">>>>>>>>>>>>>>>>>")
        mLocationRequest = LocationRequest();
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest?.setInterval(UPDATE_INTERVAL);
        mLocationRequest?.setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this@UserService);


    }

    private fun printOutput(s: String) {
        Log.d("Update",s);
    }
}
