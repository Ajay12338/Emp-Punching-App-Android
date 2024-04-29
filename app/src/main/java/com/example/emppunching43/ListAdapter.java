package com.example.emppunching43;


import static com.example.emppunching43.ListDates.calculateTimeDifference;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.time.Duration;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ListData> {
    private final Context mContext;
    private final StoreWeeks mDatabaseHelper;
    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList,StoreWeeks databaseHelper) {
        super(context, R.layout.list_item, dataArrayList);
        mContext = context;
        mDatabaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView listName = view.findViewById(R.id.day);
        TextView listTime = view.findViewById(R.id.date);



        boolean isDataPresent = mDatabaseHelper.isDayDataPresent(listData.date);

        if (isDataPresent) {
            List<String> dateData = mDatabaseHelper.getDataForDateList(listData.date);
            Duration timeDiff = calculateTimeDifference(dateData.get(4),dateData.get(5));
            String hrs = String.valueOf(timeDiff.toHours());
            String mints = String.valueOf(timeDiff.toMinutes() % 60);
            listName.setText(listData.day);
            listTime.setText("HH:"+hrs+" MM:"+mints);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_green));
        }
        else{
            listName.setText(listData.day);
            listTime.setText(listData.date);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }

        return view;
    }
}
