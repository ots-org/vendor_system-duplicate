/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ortusolis.rotarytarana.location;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.LoginResponse;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Class to process location results.
 */
public class LocationResultHelper {

    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";

    final private static String PRIMARY_CHANNEL = "default";


    private Context mContext;
    private Location mLocations;
    private NotificationManager mNotificationManager;
    SharedPreferences sharedPreferences;

    LocationResultHelper(Context context, Location locations) {
        mContext = context;
        mLocations = locations;
        sharedPreferences = mContext.getSharedPreferences("water_management", 0);
//        NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL,
//                context.getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT); // app is crashing at this point for some mobile phone
//        channel.setLightColor(Color.GREEN);
//        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        getNotificationManager().createNotificationChannel(channel);
    }

    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */
    private String getLocationResultTitle() {
        String numLocationsReported = mContext.getResources().getQuantityString(
                R.plurals.num_locations_reported, 1, 1);
        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(new Date());
    }

    private String getLocationResultText() {
        /*if (mLocations.isEmpty()) {
            return mContext.getString(R.string.unknown_location);
        }*/
        StringBuilder sb = new StringBuilder();
        //for (Location location : mLocations) {
            sb.append("(");
            sb.append(mLocations.getLatitude());
            sb.append(", ");
            sb.append(mLocations.getLongitude());
            sb.append(")");
            sb.append("\n");
        //}
        sendRequest(String.valueOf(mLocations.getLatitude()),String.valueOf(mLocations.getLongitude()));
        return sb.toString();
    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * Displays a notification with the location results.
     */
    /*
    void showNotification() {
        Intent notificationIntent = new Intent(mContext, MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(mContext,
                PRIMARY_CHANNEL)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        getNotificationManager().notify(0, notificationBuilder.build());
    }
*/
    void sendRequest(String latS, String lonS){
        if (!TextUtils.isEmpty(sharedPreferences.getString("userid", "")) && !TextUtils.isEmpty(latS) && !TextUtils.isEmpty(lonS)) {

            WebserviceController wss = new WebserviceController(mContext);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject =new JSONObject();
            try {
                jsonObject.put("latitude", latS);
                jsonObject.put("longitude", lonS);
                jsonObject.put("userId", sharedPreferences.getString("userid", ""));
                jsonObject.put("latLongId", "null");

                requestObject.put("requestData", jsonObject);
                sharedPreferences.edit().putString("latitude",latS).commit();
                sharedPreferences.edit().putString("longitude",lonS).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("from service request", requestObject.toString());

            wss.postLoginVolley(Constants.updateEmpLatLong, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("from service response", response);

                        LoginResponse responseData = new Gson().fromJson(response, LoginResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {


                        } else {
//                            Toast.makeText(mContext,"Hello Javatpoint1",Toast.LENGTH_SHORT).show();
//                            Toast.makeText(mContext, (TextUtils.isEmpty(responseData.getResponseDescription())) ? "Location Failed" : responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    try {
//                        Toast.makeText(mContext, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
