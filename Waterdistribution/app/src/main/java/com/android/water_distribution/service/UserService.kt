package com.android.water_distribution.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.android.volley.VolleyError
import com.android.water_distribution.Activity.MainActivity
import com.android.water_distribution.Activity.WaterApp.CHANNEL_ID
import com.android.water_distribution.NetworkUtility.Constants
import com.android.water_distribution.NetworkUtility.IResult
import com.android.water_distribution.NetworkUtility.WebserviceController
import com.android.water_distribution.R
import com.android.water_distribution.pojo.LoginResponse
import com.google.gson.Gson

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

import locationprovider.davidserrano.com.LocationProvider

class UserService : Service() {

    internal lateinit var con: Context
    internal var dateStr = ""
    internal var timeStr = ""
    var TAG = "UserService"
    internal var latS = "0"
    internal var lonS = "0"
    private lateinit var lProvider: LocationProvider
    lateinit var sharedPreferences : SharedPreferences
    lateinit var gson : Gson

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

        startLocationUpdates()

        val handler = Handler()
        val timer = Timer()
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

        var text :String = if( latS.isEmpty()){ "Searcing For Location"} else{  "Location set by GPS"}
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)

        return START_NOT_STICKY
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


            wss.postLoginVolley(Constants.updateEmpLatLong, requestObject.toString(), object : IResult {
                override fun notifySuccess(response: String, statusCode: Int) {
                    try {
                        Log.e("login response", response)

                        val responseData = gson.fromJson(response, LoginResponse::class.java)

                        if (responseData.getResponseCode().equals("200", ignoreCase = true)) {


                        } else {
                            Toast.makeText(this@UserService, if (TextUtils.isEmpty(responseData.getResponseDescription())) "Location Failed" else responseData.getResponseDescription(), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun notifyError(error: VolleyError) {
                    try {
                        Toast.makeText(this@UserService, WebserviceController.returnErrorMessage(error)!! + "", Toast.LENGTH_SHORT).show()
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

    private fun startLocationUpdates() {
        val callback = object : LocationProvider.LocationCallback {
            override fun locationRequestStopped() {
                printOutput("Stopped requesting location")
                //enableLocationButton()
            }

            override fun onNewLocationAvailable(latitude: Float, longitude: Float) {
                printOutput("New location available - Lat: $latitude / Lon: $longitude")
                latS = latitude.toString()
                lonS = longitude.toString()
                Log.d("loc", "lat:$latS/lon:$lonS")
            }

            override fun locationServicesNotEnabled() {
                printOutput("Location services are not enabled - please turn them on and try again")
                //enableLocationButton()
            }

            override fun updateLocationInBackground(latitude: Float, longitude: Float) {
                printOutput("Location updated in background - Lat: $latitude / Lon: $longitude")
                latS = latitude.toString()
                lonS = longitude.toString()
                Log.d("loc", "lat:$latS/lon:$lonS")
            }

            override fun networkListenerInitialised() {
                printOutput("Network listener started")
            }
        }

        lProvider = LocationProvider.Builder()
                .setContext(this)
                .setListener(callback)
                .create()

        //disableLocationButton()

        printOutput("\nStarting location updates...")
        lProvider.requestLocation()
    }

    private fun printOutput(s: String) {
        Log.d("Update",s);
    }
}
