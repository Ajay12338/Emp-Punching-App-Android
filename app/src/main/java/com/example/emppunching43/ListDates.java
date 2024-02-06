package com.example.emppunching43;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.example.emppunching43.databinding.ActivityListDatesBinding;
import java.util.ArrayList;
import java.util.List;

public class ListDates extends AppCompatActivity {

    ActivityListDatesBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListDatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String[] dayList = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"};
        String sundayDate = intent.getStringExtra("dateOfWeek");
        String url = "http://192.168.0.25:3000/date/"+sundayDate;
        ApiCallGetDates getDates = new ApiCallGetDates(getApplicationContext(),url);
        getDates.getDates(new ApiCallGetDates.VolleyCallback() {
            @Override
            public void onSuccess(List<String> dates) {
                for (int i = 0; i < dayList.length; i++){
                    listData = new ListData(dayList[i], dates.get(i));
                    dataArrayList.add(listData);
                }
                listAdapter = new ListAdapter(ListDates.this, dataArrayList);
                binding.listview.setAdapter(listAdapter);
                binding.listview.setClickable(true);

                binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ListDates.this,AddProjects.class);
                        ListData clickedItem = dataArrayList.get(i);
                        String clickedDay = clickedItem.day;
                        Toast.makeText(ListDates.this, clickedDay, Toast.LENGTH_SHORT).show();
                        intent.putExtra("day",clickedDay);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ListDates.this,"Error fetching dates",Toast.LENGTH_SHORT).show();
            }
        });
    }
}