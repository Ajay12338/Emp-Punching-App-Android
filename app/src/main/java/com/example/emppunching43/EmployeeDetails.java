package com.example.emppunching43;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EmployeeDetails extends AppCompatActivity {
    String totalEmpDetail;
    private LinearLayout cardContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details2);
        Intent intent = getIntent();
        cardContainer = findViewById(R.id.cardContainer1);
        String employeeName = intent.getStringExtra("emp_name");
        String url = "http://129.213.42.17:8082/dsysdev/hr/HR/Timehseet_entry?&employee_name="+employeeName;
        ApiCall apiCall = new ApiCall(getApplicationContext(), url);
        apiCall.getEmpDetail(new ApiCall.VolleyCallbackGetEmpDetail() {
            @Override
            public void onSuccess(LinkedHashMap<String, String> empDetails) {
                for (String key : empDetails.keySet()){
                    String value = empDetails.get(key);
                    addNewCard(key,value);
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EmployeeDetails.this,errorMessage,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void addNewCard(String key,String value) {
        String mainText = key + ":" +  value;
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 20);
        layoutParams.height = 150;
        cardView.setLayoutParams(layoutParams);
        TextView projectInfoTextView = new TextView(this);
        projectInfoTextView.setText(mainText);
        projectInfoTextView.setTextSize(18);
        projectInfoTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        projectInfoTextView.setLayoutParams(textViewParams);
        cardView.addView(projectInfoTextView);
        cardContainer.addView(cardView);
    }
}