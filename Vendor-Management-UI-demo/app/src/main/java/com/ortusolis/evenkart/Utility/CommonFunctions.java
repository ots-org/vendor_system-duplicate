package com.ortusolis.evenkart.Utility;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonFunctions {

    public static void appendLog(String text)
    {
        File logFile = new File(Environment.getExternalStorageDirectory()+"/networklog.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String converDateStr(String dateStr){

        /*if (dateStr.trim().length() < 10 ) return dateStr.trim();
        else return dateStr.trim().substring(0, 10);*/

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                date = format.parse(dateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }

        return dateFormat.format(date);
    }

    public static String fmt(float d)
    {
        double s = Math.ceil(d*100)/100;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(s);
    }

    public static boolean isKeyBoardOpen(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            Log.d("Keyboard", "Software Keyboard was shown");
            return true;
        } else {
            Log.d("Keyboard", "Software Keyboard was not shown");
            return false;
        }
    }

}
