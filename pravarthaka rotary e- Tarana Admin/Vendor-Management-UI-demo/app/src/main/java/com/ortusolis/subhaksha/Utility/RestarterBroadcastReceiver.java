package com.ortusolis.subhaksha.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.ortusolis.subhaksha.service.UserService;

/**
 * Created by Shahbaz on 27-10-2016.
 */

public class RestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(RestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(new Intent(context, UserService.class));
            }
            else {
                context.startService(new Intent(context, UserService.class));
            }
        }
        catch (Exception e){
            Log.e("error msg",e.getMessage());
            e.printStackTrace();
        }
    }
}