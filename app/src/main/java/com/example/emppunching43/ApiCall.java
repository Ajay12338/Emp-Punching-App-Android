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

import java.util.HashMap;

public class ApiCall {
    private String url;
    private Context context;
    public ApiCall(Context context, String url) {
        this.context = context;
        this.url = url;
    }
    public void getProjects(final VolleyCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            HashMap<String, String> map = new HashMap<>();
                            JSONArray itemsArray = response.getJSONArray("items");
                            for (int i = 0; i < itemsArray.length(); i++) {
                                String projectName = itemsArray.getJSONObject(i).getString("project_short_name");
                                String projectId = itemsArray.getJSONObject(i).getString("project_id");
                                map.put(projectName,projectId);
                            }
                            callback.onSuccess(map);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error parsing JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onFailure("Error parsing JSON");
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
    }
    public void getCustomer(final VolleyCallbackCustomer callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String customerName = response.getString("Customer_name");
                            callback.onSuccess(customerName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error parsing JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onFailure("Error parsing JSON");
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
    }
    public interface  VolleyCallbackCustomer{
        void onSuccess(String customerName);
        void onFailure(String err);
    }
    public interface VolleyCallback {
        void onSuccess(HashMap<String,String> itemList);
        void onFailure(String errorMessage);
    }
}