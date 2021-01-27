package com.ortusolis.rotarytaranaadmin.NetworkUtility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * Singleton class to have a common Volley queue
 */
public class VolleyQueueSingleton {
    private static VolleyQueueSingleton mInstance;
    private RequestQueue mRequestQueue = null;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleyQueueSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyQueueSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();

            final ClientConnectionManager mClientConnectionManager = mDefaultHttpClient.getConnectionManager();
            final HttpParams mHttpParams = mDefaultHttpClient.getParams();
            final ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager( mHttpParams, mClientConnectionManager.getSchemeRegistry() );

            mDefaultHttpClient = new DefaultHttpClient( mThreadSafeClientConnManager, mHttpParams );

            final HttpStack httpStack = new HttpClientStack( mDefaultHttpClient );

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), httpStack);
        }
        return mRequestQueue;
    }

    public RequestQueue getRequestQueueDel() {

            String userAgent = "volley/0";
            try {
                String packageName = mCtx.getPackageName();
                PackageInfo info = mCtx.getPackageManager().getPackageInfo(packageName, 0);
                userAgent = packageName + "/" + info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {}
            HttpStack httpStackDel = new OwnHttpClientStack(AndroidHttpClient.newInstance(userAgent));

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), httpStackDel);

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueueDel(Request<T> req) {
        getRequestQueueDel().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}

