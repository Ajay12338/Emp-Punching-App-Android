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
                int noOfWeeks = empDetails.size() / 13;
                for(int i=0;i<noOfWeeks;i++){
                    String projectName = empDetails.get("Project Name " + i);
                    String description = empDetails.get("Description " + i);
                    String weekStartDate = empDetails.get("Week Start Date " + i);
                    String shiftCode = empDetails.get("Shift Code " + i);
                    String sunday = empDetails.get("Sunday " + i);
                    String monday = empDetails.get("Monday " + i);
                    String tuesday = empDetails.get("Tuesday " + i);
                    String wednesday = empDetails.get("Wednesday " + i);
                    String thursday = empDetails.get("Thursday " + i);
                    String friday = empDetails.get("Friday " + i);
                    String saturday = empDetails.get("Saturday " + i);
                    String submitDate = empDetails.get("Submit Date " + i);
                    String approverName = empDetails.get("Approver Name " + i);

                    String contentBuilder = "*** Week Start Date: " + weekStartDate + "***" + "\n" +
                            "Project Name: " + projectName + "\n" +
                            "Description: " + description + "\n" +
                            "Shift Code: " + shiftCode + "\n" +
                            "Sunday: " + sunday + "\n" +
                            "Monday: " + monday + "\n" +
                            "Tuesday: " + tuesday + "\n" +
                            "Wednesday: " + wednesday + "\n" +
                            "Thursday: " + thursday + "\n" +
                            "Friday: " + friday + "\n" +
                            "Saturday: " + saturday + "\n" +
                            "Submit Date: " + submitDate + "\n" +
                            "Approver Name: " + approverName + "\n";
                    addNewCard(contentBuilder);
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EmployeeDetails.this,errorMessage,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void addNewCard(String value) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 20);
        cardView.setLayoutParams(layoutParams);
        TextView projectInfoTextView = new TextView(this);
        projectInfoTextView.setText(value);
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