package com.example.emppunching43;

import static com.example.emppunching43.StoreWeeks.COL_CUSTOMER_NAME;
import static com.example.emppunching43.StoreWeeks.COL_DESCRIPTION;
import static com.example.emppunching43.StoreWeeks.COL_END_HOUR;
import static com.example.emppunching43.StoreWeeks.COL_PROJECT_ID;
import static com.example.emppunching43.StoreWeeks.COL_PROJECT_NAME;
import static com.example.emppunching43.StoreWeeks.COL_SHIFT;
import static com.example.emppunching43.StoreWeeks.COL_START_HOUR;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AddProjects extends AppCompatActivity {
    private LinearLayout cardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projects);
        Intent i = getIntent();
        cardContainer = findViewById(R.id.cardContainer);
        String dayOfWeek = i.getStringExtra("date");
        String date = "Date: "+dayOfWeek;
        TextView dateTxt = findViewById(R.id.dayOfWeek);
        dateTxt.setText(date);
        Button btn = (Button) findViewById(R.id.addBtn);

        StoreWeeks dbHelper = new StoreWeeks(this);
        Cursor cursor = dbHelper.getDataForDate(dayOfWeek);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String projectName = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROJECT_NAME));
                    int projectID = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROJECT_ID));
                    String customerName = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUSTOMER_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                    String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_START_HOUR));
                    String endTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_END_HOUR));
                    String shift = cursor.getString(cursor.getColumnIndexOrThrow(COL_SHIFT));
                    addNewCard(projectName,projectID, customerName, description, startTime, endTime, shift);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProjects.this, ProjectDetails.class);
                intent.putExtra("date", dayOfWeek);
                startActivity(intent);
            }
        });

    }
    private void addNewCard(String projectName,int projectID, String customerName, String description,String start_time ,String end_time, String shift) {
        String mainText = "Project Name: " + projectName + "\nProject Id: " + projectID + "\nCustomer: " + customerName + "\nDescription: " + description + "\n Start Time:" + start_time + "\n End Time:" + end_time + "\nShift:" + shift;
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 20);
        cardView.setLayoutParams(layoutParams);
        TextView projectInfoTextView = new TextView(this);
        projectInfoTextView.setText(mainText);
        projectInfoTextView.setTextSize(18);
        projectInfoTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardView.setRadius(20);
        projectInfoTextView.setLayoutParams(textViewParams);
        cardView.addView(projectInfoTextView);
        cardContainer.addView(cardView);
    }
}