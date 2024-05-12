package com.example.emppunching43;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainPage extends AppCompatActivity {
    String empName, empId;
    private boolean isOnline = false;
    private TextView onlineStatusText;
    private View onlineStatusBlink;
    Handler mHandler = new Handler();
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        String party_id = "5928";
        Intent i1 = getIntent();
        empId = i1.getStringExtra("emp_id");
        empName = i1.getStringExtra("emp_name");
        onlineStatusText = findViewById(R.id.statusTextView);
        onlineStatusBlink = findViewById(R.id.indicatorView);
        autoCompleteTextView = findViewById(R.id.spinner);
        String url = "http://129.213.42.17:8082/dsysdev/hr/timesheet/timesheet?party_id=5928&company_id=1&location_id=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<String> itemList = new ArrayList<>();
                        JSONArray itemsArray = response.getJSONArray("items");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            String itemText = itemsArray.getJSONObject(i).getString("d");
                            itemList.add(itemText);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainPage.this, R.layout.drop_down_items, itemList);
                        autoCompleteTextView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainPage.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(MainPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        Button employeeDetail = findViewById(R.id.shEmpDtl);
        employeeDetail.setOnClickListener(view -> {
            Intent intent = new Intent(MainPage.this, EmployeeDetails.class);
            intent.putExtra("emp_name", empName);
            startActivity(intent);
        });
        Button submitBtn = findViewById(R.id.submitWeekDate);
        submitBtn.setOnClickListener(view -> {
            String currText = autoCompleteTextView.getText().toString();
            if(isValidInputDate(currText)){
                if(isSunday(currText)){
                    Intent intent = new Intent(MainPage.this, ListDates.class);
                    intent.putExtra("emp_id", empId);
                    intent.putExtra("emp_name", empName);
                    intent.putExtra("dateOfWeek", currText);
                    Toast.makeText(MainPage.this, currText, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainPage.this,"This is date is not Sunday!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(MainPage.this,"Not a Valid Date!", Toast.LENGTH_SHORT).show();
            }

        });
        checkOnlineStatus();
    }

    private void checkOnlineStatus() {
        isOnline = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isOnline = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                updateUI();
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(runnable, 2000);
    }

    private void updateUI() {
        int greenColor = ContextCompat.getColor(this, android.R.color.holo_green_dark);
        int redColor = ContextCompat.getColor(this, android.R.color.holo_red_dark);

        if (isOnline) {
            onlineStatusText.setText(R.string.online);
            onlineStatusText.setTextColor(greenColor);
            blinkIndicator(true);
        } else {
            onlineStatusText.setText(R.string.offline);
            onlineStatusText.setTextColor(redColor);
            blinkIndicator(false);
        }
    }

    private void blinkIndicator(boolean isOnline) {
        if (isOnline) {
            onlineStatusBlink.setBackground(ContextCompat.getDrawable(this, R.drawable.green_blink));
        } else {
            onlineStatusBlink.setBackground(ContextCompat.getDrawable(this, R.drawable.red_blink));
        }
    }
    private boolean isValidInputDate(String inputStr){
        Pattern pattern = Pattern.compile("^(0?[0-9]|[12][0-9]|3[0-1])-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-(\\d{4})$");
        return pattern.matcher(inputStr).find();
    }
    private boolean isSunday(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return (dayOfWeek == Calendar.SUNDAY);
        } catch (ParseException e) {
            return false;
        }
    }
}
