package com.liebald.popularmovies.utilities;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * based on https://developer.android.com/training/volley/requestqueue.html
 */
public class VolleyRequests {
    @SuppressLint("StaticFieldLeak")
    private static VolleyRequests mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private VolleyRequests(Context context) {
        mContext = context.getApplicationContext();
        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequests getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequests(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
