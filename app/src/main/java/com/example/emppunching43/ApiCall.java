package com.example.emppunching43;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class ApiCall {
    private final String url;
    private final Context context;
    public ApiCall(Context context, String url) {
        this.context = context;
        this.url = url;
    }
    public void getProjects(final VolleyCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                response -> {
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
                },
                error -> {
                    error.printStackTrace();
                    callback.onFailure("Error parsing JSON");
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
    }
    public void getCustomer(final VolleyCallbackCustomer callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                response -> {
                    try {
                        String customerName = response.getString("Customer_name");
                        callback.onSuccess(customerName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure("Error parsing JSON");
                    }
                },
                error -> {
                    error.printStackTrace();
                    callback.onFailure("Error parsing JSON");
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
    }
    public void getShifts(final VolleyCallbackShifts callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                response -> {
                    try {
                        List<String> shifts = new ArrayList<>();
                        JSONArray itemsArray = response.getJSONArray("items");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            String shiftName = itemsArray.getJSONObject(i).getString("d");
                            shifts.add(shiftName);
                        }
                        callback.onSuccess(shifts);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure("Error parsing JSON");
                    }
                },
                error -> {
                    error.printStackTrace();
                    callback.onFailure("Error parsing JSON");
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
    }
    public void getEmpDetail(final VolleyCallbackGetEmpDetail callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                response -> {
                    try {
                        LinkedHashMap<String,String> empDetails = new LinkedHashMap<>();
                        JSONArray itemsArray = response.getJSONArray("items");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject item = itemsArray.getJSONObject(i);
                            String projectName = item.getString("project_name");
                            String description = item.getString("description");
                            String weekStartDate = item.getString("week_start_date");
                            String shiftCode = item.getString("shift_code");
                            int sundayHours = item.getInt("sunday_hours");
                            int mondayHours = item.getInt("monday_hours");
                            int tuesdayHours = item.getInt("tuesday_hours");
                            int wednesdayHours = item.getInt("wednesday_hours");
                            int thursdayHours = item.getInt("thursday_hours");
                            int fridayHours = item.getInt("friday_hours");
                            int saturdayHours = item.getInt("saturday_hours");
                            int sundayMints = item.getInt("sunday_mints");
                            int mondayMints = item.getInt("monday_mints");
                            int tuesdayMints = item.getInt("tuesday_mints");
                            int wednesdayMints = item.getInt("wednesday_mints");
                            int thursdayMints = item.getInt("thursday_mints");
                            int fridayMints = item.getInt("friday_mints");
                            int saturdayMints = item.getInt("saturday_mints");
                            String submitDate = item.getString("submit_date");
                            String approverName = item.getString("approver_name");

                            empDetails.put("Project Name " + i, projectName);
                            empDetails.put("Description " + i, description);
                            empDetails.put("Week Start Date " + i, weekStartDate);
                            empDetails.put("Shift Code " + i, shiftCode);
                            empDetails.put("Sunday " + i, sundayHours +" Hours, " + sundayMints +" Minutes");
                            empDetails.put("Monday " + i, mondayHours +" Hours, " + mondayMints +" Minutes");
                            empDetails.put("Tuesday "+ i, tuesdayHours +" Hours, " + tuesdayMints +" Minutes");
                            empDetails.put("Wednesday "+ i, wednesdayHours +" Hours, " + wednesdayMints +" Minutes");
                            empDetails.put("Thursday "+ i, thursdayHours +" Hours, " + thursdayMints +" Minutes");
                            empDetails.put("Friday "+ i, fridayHours +" Hours, " + fridayMints +" Minutes");
                            empDetails.put("Saturday "+ i, saturdayHours +" Hours, " + saturdayMints +" Minutes");
                            empDetails.put("Submit Date "+ i, submitDate);
                            empDetails.put("Approver Name "+ i, approverName);
                        }
                        callback.onSuccess(empDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure("Error fetching Employee Detail from API");
                    }
                },
                error -> {
                    error.printStackTrace();
                    callback.onFailure("Error fetching Employee Detail from API");
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
    public interface VolleyCallbackShifts {
        void onSuccess(List<String> shifts);
        void onFailure(String errorMessage);
    }
    public interface VolleyCallbackGetEmpDetail{
        void onSuccess(LinkedHashMap<String,String> empDetails);
        void onFailure(String errorMessage);
    }
}