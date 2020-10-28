package com.ortusolis.rotarytarana.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.Utility.AvenuesParams;
//import com.ortusolis.rotarytarana.Utility.Constants;
import com.ortusolis.rotarytarana.Utility.LoadingDialog;
import com.ortusolis.rotarytarana.Utility.RSAUtility;
import com.ortusolis.rotarytarana.Utility.ServiceUtility;
import com.ortusolis.rotarytarana.Utility.VenderConstants;


public class WebViewActivity  extends AppCompatActivity {

    Intent mainIntent;

    String encVal;

    String vResponse;





    @Override

    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setContentView(R.layout.activity_webview);

        mainIntent = getIntent();



//get rsa key method

        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));

    }







    private class RenderView extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            // Showing progress dialog

            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");



        }



        @Override

        protected Void doInBackground(Void... arg0) {

            if (!ServiceUtility.chkNull(vResponse).equals("")

                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {

                StringBuffer vEncVal = new StringBuffer("");

                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));

                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));

                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency

            }



            return null;

        }



        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            // Dismiss the progress dialog

            LoadingDialog.cancelLoading();



            @SuppressWarnings("unused")

            class MyJavaScriptInterface {

                @JavascriptInterface

                public void processHTML(String html) {

                    // process the html source code to get final status of transaction

                    String status = null;

                    if (html.indexOf("Failure") != -1) {

                        status = "Transaction Declined!";

                    } else if (html.indexOf("Success") != -1) {

                        status = "Transaction Successful!";

                    } else if (html.indexOf("Aborted") != -1) {

                        status = "Transaction Cancelled!";

                    } else {

                        status = "Status Not Known!";

                    }

                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);

                    intent.putExtra("transStatus", status);

                    startActivity(intent);

                }

            }



            final WebView webview = (WebView) findViewById(R.id.webview);

            webview.getSettings().setJavaScriptEnabled(true);

            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

            webview.setWebViewClient(new WebViewClient() {



                @SuppressWarnings("deprecation")

                @Override

                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    return false;

                }



                @TargetApi(Build.VERSION_CODES.N)

                @Override

                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    view.loadUrl(request.getUrl().toString());

                    return true;

                }



                @Override

                public void onPageFinished(WebView view, String url) {

                    super.onPageFinished(webview, url);

                    LoadingDialog.cancelLoading();

                    if (url.indexOf("/ccavResponseHandler.php") != -1) {

                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                    }

                }



                @Override

                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    super.onPageStarted(view, url, favicon);

                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

                }

            });





            try {

                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");

                webview.postUrl(VenderConstants.TRANS_URL, postData.getBytes());

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            }



        }

    }



    public void get_RSA_key(final String ac, final String od) {

        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");



        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL),

                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();

                        LoadingDialog.cancelLoading();



                        if (response != null && !response.equals("")) {

                            vResponse = response;     ///save retrived rsa key

                            if (vResponse.contains("!ERROR!")) {

                                show_alert(vResponse);

                            } else {

                                new RenderView().execute();   // Calling async task to get display content

                            }





                        }

                        else

                        {

                            show_alert("No response");

                        }

                    }

                },

                new Response.ErrorListener() {

                    @Override

                    public void onErrorResponse(VolleyError error) {

                        LoadingDialog.cancelLoading();

                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                    }

                }) {

            @Override

            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put(AvenuesParams.ACCESS_CODE, ac);

                params.put(AvenuesParams.ORDER_ID, od);

                return params;

            }



        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(

                30000,

                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }



    public void show_alert(String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(

                WebViewActivity.this).create();



        alertDialog.setTitle("Error!!!");

        if (msg.contains("\n"))

            msg = msg.replaceAll("\\\n", "");



        alertDialog.setMessage(msg);







        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {



            @Override

            public void onClick(DialogInterface dialog, int which) {

                finish();

            }

        });





        alertDialog.show();

    }

}
