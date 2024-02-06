package com.example.emppunching43;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectDetails extends AppCompatActivity {
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        String url = "http://192.168.0.25:3000/projects";
        //String url = "http://129.213.42.17:8082/dsysdev/hr/timesheet/getproject?party_id=5928&dates=17-Dec-2023&useremail=admin@doyenche.com";
        ApiCall apiCall = new ApiCall(getApplicationContext(), url);
        apiCall.getProjects(new ApiCall.VolleyCallback() {
            @Override
            public void onSuccess(HashMap<String,String> itemList) {
                List<String> projects = new ArrayList<>(itemList.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProjectDetails.this, R.layout.drop_down_items, projects);
                AutoCompleteTextView autoCompleteTextView = findViewById(R.id.spinner1);
                autoCompleteTextView.setAdapter(adapter);

                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String currText = autoCompleteTextView.getText().toString();
                        AutoCompleteTextView project_id = findViewById(R.id.spinner2);
                        String currId = itemList.get(currText);
                        String urlCustomer = "http://192.168.0.25:3000/customer";
                       // String urlCustomer = "http://129.213.42.17:8082/dsysdev/hr/timesheet/Get_customer?pro_id=" + currId;
                        ApiCall apiCall = new ApiCall(getApplicationContext(), urlCustomer);
                        apiCall.getCustomer(new ApiCall.VolleyCallbackCustomer() {
                            public void onSuccess(String customerName) {
                                project_id.setText(customerName);
                            }

                            @Override
                            public void onFailure(String err) {
                                Toast.makeText(ProjectDetails.this,"Error Fetching Customer Name",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ProjectDetails.this,"Error Fetching Project Details",Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd = (Button) findViewById(R.id.AddBtn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                AutoCompleteTextView projectInputTxt = findViewById(R.id.spinner1);
                AutoCompleteTextView customerInputTxt = findViewById(R.id.spinner2);
                AutoCompleteTextView descInputTxt = findViewById(R.id.spinner3);

                resultIntent.putExtra("project",projectInputTxt.getText().toString() );
                resultIntent.putExtra("customer", customerInputTxt.getText().toString());
                resultIntent.putExtra("desc", descInputTxt.getText().toString());

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}