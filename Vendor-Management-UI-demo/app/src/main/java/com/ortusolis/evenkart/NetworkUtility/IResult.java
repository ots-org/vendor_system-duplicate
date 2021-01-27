package com.ortusolis.evenkart.NetworkUtility;

import com.android.volley.VolleyError;

public interface IResult {
    public void notifySuccess(String response, int statusCode);
    public void notifyError(VolleyError error);
}