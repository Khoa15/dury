package com.example.dury.Service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiService {
    protected static String hostAPI = "http://localhost:8080/api/v1";
    private static ApiService instance;
    private static RequestQueue requestQueue = getRequestQueue();
    private static Context ctx;

    ApiService(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    public static String getHostAPI() {
        return hostAPI;
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static void getJsonObject(String url, final ApiResponseCallback<JSONObject> callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );
        getRequestQueue().add(jsonObjectRequest);
    }

    public interface ApiResponseCallback<T> {
        void onSuccess(T result);
        void onError(VolleyError error);
    }
}
