package com.example.emppunching43;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


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
import java.util.List;

public class MainPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        String party_id = "5928";
        //String url = "http://129.213.42.17:8082/dsysdev/hr/timesheet/timesheet?party_id=" + party_id;
       String url = "http://192.168.0.25:3000";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<String> itemList = new ArrayList<>();
                            JSONArray itemsArray = response.getJSONArray("items");
                            for (int i = 0; i < itemsArray.length(); i++) {
                                String itemText = itemsArray.getJSONObject(i).getString("d");
                                itemList.add(itemText);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainPage.this,R.layout.drop_down_items,itemList);
                            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.spinner);
                            autoCompleteTextView.setAdapter(adapter);

                            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(MainPage.this,ListDates.class);
                                    String currText = autoCompleteTextView.getText().toString();
                                    intent.putExtra("dateOfWeek",currText);
                                    Toast.makeText(MainPage.this,currText,Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainPage.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}