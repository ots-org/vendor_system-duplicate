package com.android.water_distribution.Utility;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.android.water_distribution.service.UserService;

public class RestarterBroadcastReceiver extends WakefulBroadcastReceiver {

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
            e.printStackTrace();
        }
    }
}