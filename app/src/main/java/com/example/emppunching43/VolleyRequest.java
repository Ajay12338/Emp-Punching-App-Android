package com.example.emppunching43;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class VolleyRequest {
    private static final String BASE_URL = "http://132.145.145.118:8082/apex/pms_live/Timesheet_entry/Timesheet_entry?party_id=5928"; // Combined base URL and endpoint

    public static void getApiResponse(Context context, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                successListener,
                errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
