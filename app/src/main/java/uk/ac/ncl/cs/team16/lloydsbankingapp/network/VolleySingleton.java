package uk.ac.ncl.cs.team16.lloydsbankingapp.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import uk.ac.ncl.cs.team16.lloydsbankingapp.Models.LbgApplication;

/**
 * To enable the app to use only one request que.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;
    private RequestQueue requestQueue;

    private VolleySingleton(){
        requestQueue = Volley.newRequestQueue(LbgApplication.getAppContext());
    }

    public static VolleySingleton getInstance(){

        if(sInstance == null)
            sInstance = new VolleySingleton();

        return sInstance;

    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
