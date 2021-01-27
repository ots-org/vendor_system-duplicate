package com.ortusolis.rotarytaranaadmin.NetworkUtility;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ortusolis.rotarytaranaadmin.Utility.CommonFunctions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WebserviceController {

	Context context;
	ProgressDialog pDialog;
	WebControllerInterface myInterface;
	int mStatusCode = 0;
	private static final int MY_SOCKET_TIMEOUT_MS = 600000;

	public WebserviceController(Context context) {
		this.context = context;
	}

	public WebserviceController(Context context, Object obj) {
		this.context = context;
		this.myInterface = (WebControllerInterface) obj;
	}

	public void sendRequest(String url) {
		if (isNetworkAvailable()) {
			pDialog = new ProgressDialog(context);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(60000);

			Log.d("url",url);

			client.get(url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
					try {
						String response = new String(responseBody, "UTF-8");
						System.out.println(response);

						try {
							JSONObject jo = new JSONObject(response);
							if (response.contains("error")) {
								boolean errorInLogin = jo.getBoolean("error");
								if (!errorInLogin) {
									myInterface.getResponse(response);
								} else {
									myInterface.getResponse(response);
									String message = jo.getString("message");
									Toast.makeText(context, TextUtils.isEmpty(message)?"Server responded with an error":message,
											Toast.LENGTH_LONG).show();
								}
							} else {
								myInterface.getResponse(response);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						}

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					pDialog.dismiss();
				}

				@Override
				public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
					pDialog.dismiss();

					String response = null;
					try {
						response = new String(responseBody, "UTF-8");
						System.out.println(response);
						try {
							JSONObject jsonObject = new JSONObject(response);


							Toast.makeText(context, jsonObject.has("message")? jsonObject.getString("message"):"Error", Toast.LENGTH_LONG)
									.show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}


				}
			});
		} else {
			Toast.makeText(context, "Internet not connected", Toast.LENGTH_LONG)
					.show();
		}

	}



	public void sendPostRequest(String url, RequestParams params) {
		if (isNetworkAvailable()) {
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please Wait ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(300000);

			client.addHeader("Content-Type","application/json");

			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
					try {
						String response = new String(responseBody, "UTF-8");
						System.out.println(response);

						try {
							JSONObject jo = new JSONObject(response);
							if (jo.has("error")) {
								boolean errorInLogin = jo.getBoolean("error");
								if (!errorInLogin) {
									myInterface.getResponse(response);
								} else {
									String message = jo.getString("message");
									Toast.makeText(context, message,
											Toast.LENGTH_LONG).show();
								}
							} else {
								myInterface.getResponse(response);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						}

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					pDialog.dismiss();
				}

				@Override
				public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
					pDialog.dismiss();
					String response = null;
					if (responseBody != null) {
						try {
							response = new String(responseBody, "UTF-8");
							System.out.println(response);
							try {
								JSONObject jsonObject = new JSONObject(response);


								Toast.makeText(context, jsonObject.has("message") ? jsonObject.getString("message") : "Error", Toast.LENGTH_LONG)
										.show();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} else {
			Toast.makeText(context, "Internet not connected", Toast.LENGTH_LONG)
					.show();
		}

	}

	public void postLoginVolley(final String url, final String sendObj, final IResult callback) {
		try {
			//RequestQueue queue = Volley.newRequestQueue(mContext);
			Log.d("post String", "postLoginVolley " + sendObj);

			CommonFunctions.appendLog("\n\r"+url +"\n Request : "+sendObj+"\n");

			StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					CommonFunctions.appendLog("\n\r"+url +"\n Response : "+response+"\n");
					callback.notifySuccess(response, mStatusCode);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					callback.notifyError(error);
				}
			}) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String, String> headers = new HashMap<>();
					headers.put("Content-Type", "application/json");

					return headers;
				}

				@Override
				public byte[] getBody() throws AuthFailureError {
					try {
						return sendObj == null ? null :
								sendObj.getBytes(getParamsEncoding());
					} catch (UnsupportedEncodingException uee) {
						return null;
					}
				}

				@Override
				protected Response<String> parseNetworkResponse(NetworkResponse response) {
					mStatusCode = response.statusCode;
					return super.parseNetworkResponse(response);
				}
			};
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(
					MY_SOCKET_TIMEOUT_MS,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			VolleyQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
		} catch (Exception e) {
			Log.d("Exception", "Exception:" + e);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String returnErrorMessage(VolleyError error){
		NetworkResponse response = error.networkResponse;
		String json = null;

		if(response != null && response.data != null) {
			json = new String(response.data);
			Log.e("Jsonnnn",json);

			json = trimMessage(json, "responseDescription");
		}
		return json;
	}

	public static String returnErrorJson(VolleyError error){
		NetworkResponse response = error.networkResponse;
		String json = null;

		if(response != null && response.data != null) {
			json = new String(response.data);
			Log.e("Jsonnnn",json);

		}
		return json;
	}

	public static String trimMessage(String json, String key){
		String trimmedString = null;

		try{
			JSONObject obj = new JSONObject(json);
			if (obj.has(key))
				trimmedString = obj.getString(key);
			else {
				trimmedString = "";
			}
		} catch(JSONException e) {
			e.printStackTrace();
			return "";
		}

		return trimmedString;
	}

}
