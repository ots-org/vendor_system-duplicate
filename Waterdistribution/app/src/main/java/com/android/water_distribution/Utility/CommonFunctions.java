package com.android.water_distribution.Utility;

import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
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

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                date = format.parse(dateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFormat.format(date);
    }

}
