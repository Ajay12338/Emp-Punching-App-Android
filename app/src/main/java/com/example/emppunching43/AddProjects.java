package com.example.emppunching43;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddProjects extends AppCompatActivity {
    private LinearLayout cardContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projects);
        Intent i = getIntent();
        cardContainer = findViewById(R.id.cardContainer);
        String dayOfWeek = i.getStringExtra("day");
        TextView dateTxt = findViewById(R.id.dayOfWeek);
        dateTxt.setText("Day: "+dayOfWeek);
        Button btn = (Button) findViewById(R.id.addBtn);

        ActivityResultLauncher<Intent> cardData = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String projectName = data.getStringExtra("project");
                                String customerName = data.getStringExtra("customer");
                                String description = data.getStringExtra("desc");
                                addNewCard(projectName, customerName, description);
                            }
                        }
                    }
                });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProjects.this, ProjectDetails.class);
                cardData.launch(intent);
            }
        });

    }
    private void addNewCard(String projectName, String customerName, String description) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 20);
        cardView.setLayoutParams(layoutParams);
        TextView projectInfoTextView = new TextView(this);
        projectInfoTextView.setText("Project Name: " + projectName + "\nCustomer: " + customerName + "\nDescription: " + description);
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