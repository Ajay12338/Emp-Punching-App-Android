package com.example.emppunching43;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApiCallGetDates {
    private String url;
    private Context context;
    public ApiCallGetDates(Context context, String url) {
        this.context = context;
        this.url = url;
    }
    public void getDates(final ApiCallGetDates.VolleyCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<String> dates = new ArrayList<>();
                            JSONArray itemsArray = response.getJSONArray("items");
                            JSONObject k = itemsArray.getJSONObject(0);
                            Iterator<String> iter = k.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                dates.add(k.getString(key));
                            }
                            callback.onSuccess(dates);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error parsing JSON-Dates");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onFailure("Error Fetching Dates");
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
    }
    public interface VolleyCallback {
        void onSuccess(List<String> dates);
        void onFailure(String errorMessage);
    }
}
