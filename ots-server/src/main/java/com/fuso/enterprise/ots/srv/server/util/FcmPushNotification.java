package com.fuso.enterprise.ots.srv.server.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class FcmPushNotification {
    public final static String AUTH_KEY_FCM = "AAAASd-8ZH0:APA91bGUCyZOtTeV7tC8gFl6U_2j0AgKqvWVJdS7nz8HSmSjrC9QuDhmJbNVsXoPuUO1M18bp6CgCE7Oq3QADbEfPINIIAV0INvcmc_nG0l7w63RHlEcenaQgm8_pNm9QTah0t-LAwEG";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static String sendPushNotification(String deviceToken,String title,String message)
            throws IOException, JSONException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("to", deviceToken.trim());
        JSONObject info = new JSONObject();
        info.put("title", title); // Notification title
        info.put("body",message); // Notification
                                                                // body
        json.put("notification", info);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            result = "Failed";
        }
        System.out.println("GCM Notification is sent successfully");

        return result;
    }
}
