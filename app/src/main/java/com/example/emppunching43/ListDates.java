package com.example.emppunching43;


import static com.example.emppunching43.StoreWeeks.COL_CUSTOMER_NAME;
import static com.example.emppunching43.StoreWeeks.COL_DESCRIPTION;
import static com.example.emppunching43.StoreWeeks.COL_END_HOUR;
import static com.example.emppunching43.StoreWeeks.COL_PROJECT_ID;
import static com.example.emppunching43.StoreWeeks.COL_PROJECT_NAME;
import static com.example.emppunching43.StoreWeeks.COL_SHIFT;
import static com.example.emppunching43.StoreWeeks.COL_START_HOUR;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.example.emppunching43.databinding.ActivityListDatesBinding;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListDates extends AppCompatActivity {

    ActivityListDatesBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    List<List<String>> allWeekProjectDetails = new ArrayList<>();
    List<String> all_dates = new ArrayList<>();
    ListData listData;
    private static final String API_URL = "http://129.213.42.17:8082/dsysdev/hr/HR/FINAL_SUBMIT_ENTRY";
    String approveName,approveId,empID,empName,sundayDate,submitStatus;
    boolean isApproved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        binding = ActivityListDatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Intent i1 = getIntent();
        empID = i1.getStringExtra("emp_id");
        empName = i1.getStringExtra("emp_name");
        String[] dayList = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"};
        sundayDate = intent.getStringExtra("dateOfWeek");
        String url = "http://192.168.0.25:3000/date/"+sundayDate;
        ApiCallGetDates getDates = new ApiCallGetDates(getApplicationContext(),url);
        getDates.getDates(new ApiCallGetDates.VolleyCallback() {
            @Override
            public void onSuccess(List<String> dates) {
                for (int i = 0; i < dayList.length; i++){
                    listData = new ListData(dayList[i], dates.get(i));
                    all_dates.add(dates.get(i));
                    dataArrayList.add(listData);
                }
                listAdapter = new ListAdapter(ListDates.this, dataArrayList);
                binding.listview.setAdapter(listAdapter);
                binding.listview.setClickable(true);

                binding.listview.setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent12 = new Intent(ListDates.this,AddProjects.class);
                    ListData clickedItem = dataArrayList.get(i);
                    String clickedDate = clickedItem.date;
                    Toast.makeText(ListDates.this, clickedDate, Toast.LENGTH_SHORT).show();
                    intent12.putExtra("date",clickedDate);
                    startActivity(intent12);
                });
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ListDates.this,"Error fetching dates",Toast.LENGTH_SHORT).show();
            }
        });
        Button postBtn = (Button) findViewById(R.id.postBt);
        Toast.makeText(ListDates.this,empID,Toast.LENGTH_SHORT).show();
        postBtn.setOnClickListener(view -> {
            StoreWeeks dbHelper = new StoreWeeks(ListDates.this);
            if(dbHelper.areAllDatesPresent(all_dates)){
                Intent intent1 = new Intent(ListDates.this, Approve.class);
                intent1.putExtra("empName",empName);
                approve.launch(intent1);
            }else {
                Toast.makeText(ListDates.this,"Not Done all week's entry",Toast.LENGTH_SHORT).show();
            }

        });

    }
    String sunHrs,monHrs,tueHrs,wedHrs,thurHrs,friHrs,satHrs;
    String sunMints,monMints,tueMints, wedMints, thurMints, friMints,satMints;
    // TODO: Final Posting of Data To Doyensys Server
    ActivityResultLauncher<Intent> approve = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    StoreWeeks dbHelper = new StoreWeeks(ListDates.this);
                    approveName = data.getStringExtra("apprName");
                    approveId = data.getStringExtra("apprID");
                    isApproved = data.getBooleanExtra("approved",false);
                    Toast.makeText(ListDates.this,"Post Successful!!!",Toast.LENGTH_SHORT).show();
                    for(String day:all_dates){
                        List<String> dayData = dbHelper.getDataForDateList(day);
                        allWeekProjectDetails.add(dayData);
                    }
                    sunHrs = "0";
                    sunMints = "0";
                    List<String> innerListMon = allWeekProjectDetails.get(0);
                    Duration mondDuration = calculateTimeDifference(innerListMon.get(4),innerListMon.get(5));
                    monHrs = String.valueOf(mondDuration.toHours());
                    monMints = String.valueOf(mondDuration.toMinutes() % 60);

                    List<String> innerListTue = allWeekProjectDetails.get(1);
                    Duration tueDuration = calculateTimeDifference(innerListTue.get(4),innerListTue.get(5));
                    tueHrs = String.valueOf(tueDuration.toHours());
                    tueMints = String.valueOf(tueDuration.toMinutes() % 60);

                    List<String> innerListWed = allWeekProjectDetails.get(2);
                    Duration wedDuration = calculateTimeDifference(innerListWed.get(4),innerListWed.get(5));
                    wedHrs = String.valueOf(wedDuration.toHours());
                    wedMints = String.valueOf(wedDuration.toMinutes() % 60);

                    List<String> innerListThur = allWeekProjectDetails.get(3);
                    Duration thurDuration = calculateTimeDifference(innerListThur.get(4),innerListThur.get(5));
                    thurHrs = String.valueOf(thurDuration.toHours());
                    thurMints = String.valueOf(thurDuration.toMinutes() % 60);

                    List<String> innerListFri = allWeekProjectDetails.get(4);
                    Duration friDuration = calculateTimeDifference(innerListFri.get(4),innerListFri.get(5) );
                    friHrs = String.valueOf(friDuration.toHours());
                    friMints = String.valueOf(friDuration.toMinutes() % 60);

                    List<String> innerListSat = allWeekProjectDetails.get(5);
                    Duration satDuration = calculateTimeDifference(innerListSat.get(4),innerListSat.get(5) );
                    satHrs = String.valueOf(satDuration.toHours());
                    satMints = String.valueOf(satDuration.toMinutes() % 60);
                    submitStatus = isApproved ? "Y" : "N";
                    String[] arrOfStr = sundayDate.split(" ");
                    String weekStartDate = arrOfStr[1] + " " + arrOfStr[0]+ " " + arrOfStr[2];
                    try {
                        String res = submitData(empID,empName,innerListMon.get(1),innerListMon.get(0),innerListMon.get(3),weekStartDate,innerListMon.get(6),sunHrs,monHrs,tueHrs,wedHrs,thurHrs,friHrs,satHrs,sunMints,monMints,tueMints,wedMints,thurMints,friMints,satMints,submitStatus,approveName,approveId);
                        Toast.makeText(this,res,Toast.LENGTH_SHORT).show();
                        Log.d("ajay",res);
                    } catch (IOException e) {
                        Log.d("yoyo",e.toString());
                        Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private static String submitData(String eId,String empName,String projectId,String projectName,String desc,String weekStart,String Shift,String sunHrs,String monHrs,String tueHrs,String wedHrs,String thurHrs,String friHrs,String satHrs,String sunMints,String monMints,String tueMints,String wedMints,String thurMints,String friMints,String satMints,String sbmtSta,String apprName,String apprEId ) throws IOException {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("TIMEHSEET_ID", "123")
                .add("EMPLOYEE_ID", eId)
                .add("EMPLOYEE_NAME", empName)
                .add("PROJECT_ID", projectId)
                .add("PROJECT_NAME", projectName)
                .add("DESCRIPTION", desc)
                .add("WEEK_START_DATE", weekStart)
                .add("SHIFT_CODE", Shift)
                .add("SUNDAY_HOURS", sunHrs)
                .add("MONDAY_HOURS", monHrs)
                .add("TUESDAY_HOURS", tueHrs)
                .add("WEDNESDAY_HOURS", wedHrs)
                .add("THURSDAY_HOURS", thurHrs)
                .add("FRIDAY_HOURS", friHrs)
                .add("SATURDAY_HOURS", satHrs)
                .add("SUNDAY_MINTS", sunMints)
                .add("MONDAY_MINTS", monMints)
                .add("TUESDAY_MINTS", tueMints)
                .add("WEDNESDAY_MINTS", wedMints)
                .add("THURSDAY_MINTS", thurMints)
                .add("FRIDAY_MINTS", friMints)
                .add("SATURDAY_MINTS", satMints)
                .add("SUBMIT_STATUS", sbmtSta)
                .add("APPROVER_NAME",apprName)
                .add("APPROVER_EMPLOYEE_ID", apprEId);

        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected response: " + response);
        }
    }
    public static Duration calculateTimeDifference(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[H][HH][:m][MM]");
        LocalTime start;
        LocalTime end;

        try {
            start = LocalTime.parse(startTime, formatter);
            end = LocalTime.parse(endTime, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format");
        }

        if (end.isBefore(start)) {
            end = end.plusHours(24);
        }

        return Duration.between(start, end);
    }
}