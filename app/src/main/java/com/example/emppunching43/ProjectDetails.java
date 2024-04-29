package com.example.emppunching43;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectDetails extends AppCompatActivity {
    private AutoCompleteTextView auto_com_shifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        auto_com_shifts = findViewById(R.id.spinner6);
        //String shiftsUrl = "http://129.213.42.17:8082/dsysdev/hr/timesheet/Get_shift?party_id=5928";
        String shiftsUrl = "http://192.168.0.25:3000/shifts";
        //String url = "http://129.213.42.17:8082/dsysdev/hr/timesheet/getproject?party_id=5928&dates=01-APR-2024&useremail=admin@doyenche.com&location_id=1&company_id=1";
        String url = "http://192.168.0.25:3000/projects";
        ApiCall apiCall = new ApiCall(getApplicationContext(), url);
        apiCall.getProjects(new ApiCall.VolleyCallback() {
            @Override
            public void onSuccess(HashMap<String,String> itemList) {
                List<String> projects = new ArrayList<>(itemList.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProjectDetails.this, R.layout.drop_down_items, projects);
                AutoCompleteTextView autoCompleteTextView = findViewById(R.id.spinner1);
                autoCompleteTextView.setAdapter(adapter);

                autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
                    String currText = autoCompleteTextView.getText().toString();
                    AutoCompleteTextView project_id = findViewById(R.id.spinner2);
                    AutoCompleteTextView projectInputIdTxt = findViewById(R.id.spinner_id);
                    String currId = itemList.get(currText);
                    String urlCustomer = "http://192.168.0.25:3000/customer";
                    //String urlCustomer = "http://129.213.42.17:8082/dsysdev/hr/timesheet/Get_customer?pro_id=" + currId;
                    ApiCall apiCall1 = new ApiCall(getApplicationContext(), urlCustomer);
                    apiCall1.getCustomer(new ApiCall.VolleyCallbackCustomer() {
                        public void onSuccess(String customerName) {
                            project_id.setText(customerName);
                            projectInputIdTxt.setText(currId);
                        }
                        @Override
                        public void onFailure(String err) {
                            Toast.makeText(ProjectDetails.this,"Error Fetching Customer Name",Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ProjectDetails.this,"Error Fetching Project Details",Toast.LENGTH_SHORT).show();
            }
        });
        Button btnAdd = findViewById(R.id.AddBtn);
        Button dateBtn = findViewById(R.id.DateBtn);
        Button dateBtnEnd = findViewById(R.id.DateBtnEndDate);

        btnAdd.setOnClickListener(view -> {
            int projectId;
            Intent resultIntent = getIntent();
            String day = resultIntent.getStringExtra("date");
            AutoCompleteTextView projectInputTxt = findViewById(R.id.spinner1);
            AutoCompleteTextView projectInputIdTxt = findViewById(R.id.spinner_id);
            AutoCompleteTextView customerInputTxt = findViewById(R.id.spinner2);
            AutoCompleteTextView descInputTxt = findViewById(R.id.spinner3);
            AutoCompleteTextView startTime = findViewById(R.id.spinner4);
            AutoCompleteTextView endTime = findViewById(R.id.spinner5);
            String projectName = projectInputTxt.getText().toString();
            try{
                projectId = Integer.parseInt(projectInputIdTxt.getText().toString());
            }catch (Exception e){
                projectId = 0;
            }

            String customerName = customerInputTxt.getText().toString();
            String desc = descInputTxt.getText().toString();
            String startTim = startTime.getText().toString();
            String endTim = endTime.getText().toString();
            String shift = auto_com_shifts.getText().toString();
            if(day != null && !projectName.isEmpty() && !projectInputIdTxt.getText().toString().isEmpty() &&  !customerName.isEmpty() && !desc.isEmpty() && !startTim.isEmpty() && ! endTim.isEmpty() && !shift.isEmpty()){
                StoreWeeks dbHelper = new StoreWeeks(ProjectDetails.this);
                boolean inserted = dbHelper.insertData(day, projectName,projectId, customerName, desc, startTim, endTim, shift);
                if (inserted) {
                    Toast.makeText(ProjectDetails.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProjectDetails.this, "Failed to Insert Data", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(ProjectDetails.this,"Fill all details",Toast.LENGTH_SHORT).show();
            }
        });


        dateBtn.setOnClickListener(view -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select Start Time")
                    .build();

            picker.addOnPositiveButtonClickListener(v -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();
                String selectedTime = hour + ":" + minute;
                AutoCompleteTextView project_id = findViewById(R.id.spinner4);
                project_id.setText(selectedTime);
            });
            FragmentManager fragmentManager = getSupportFragmentManager();
            picker.show(fragmentManager, "timePicker");
        });

        dateBtnEnd.setOnClickListener(view -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select End Time")
                    .build();

            picker.addOnPositiveButtonClickListener(v -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();
                String selectedTime = hour + ":" + minute;
                AutoCompleteTextView project_id = findViewById(R.id.spinner5);
                project_id.setText(selectedTime);
            });
            FragmentManager fragmentManager = getSupportFragmentManager();
            picker.show(fragmentManager, "timePicker1");
        });

        // This is for shifts :)
        ApiCall apiCallSifts = new ApiCall(getApplicationContext(), shiftsUrl);
        apiCallSifts.getShifts(new ApiCall.VolleyCallbackShifts() {
            @Override
            public void onSuccess(List<String> shifts) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProjectDetails.this, R.layout.drop_down_items, shifts);
                auto_com_shifts.setAdapter(adapter);
                auto_com_shifts.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(ProjectDetails.this, auto_com_shifts.getText().toString(),Toast.LENGTH_SHORT).show());
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ProjectDetails.this,"Error Fetching Shifts",Toast.LENGTH_SHORT).show();
            }
        });

    }
}