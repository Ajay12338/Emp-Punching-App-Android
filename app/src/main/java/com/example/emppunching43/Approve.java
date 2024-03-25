package com.example.emppunching43;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Approve extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approver);
        Intent inti = getIntent();
        EditText approveNameTxt = (EditText) findViewById(R.id.approverName);
        EditText approveIdTxt = (EditText) findViewById(R.id.appId);
        Button approvedBtn = (Button) findViewById(R.id.approveBtn);
        Button rejectedBtn = (Button) findViewById(R.id.rejectBtn);
        TextView empNameText = (TextView) findViewById(R.id.emplName);
        empNameText.setText("Employee Name:" + inti.getStringExtra("empName"));


        if(!approveNameTxt.toString().isEmpty() && !approveIdTxt.toString().isEmpty()){
            approvedBtn.setOnClickListener(view -> {
                String approveName = approveNameTxt.getText().toString();
                String approveId = approveIdTxt.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("apprName",approveName);
                intent.putExtra("apprID",approveId);
                intent.putExtra("approved",true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            });
            rejectedBtn.setOnClickListener(view -> {
                String approveName = approveNameTxt.getText().toString();
                String approveId = approveIdTxt.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("apprName",approveName);
                intent.putExtra("apprID",approveId);
                intent.putExtra("approved",false);
                setResult(Activity.RESULT_OK, intent);
                finish();
            });

        }else {
            Toast.makeText(Approve.this,"Fill all entries!!!",Toast.LENGTH_SHORT).show();
        }


    }
}