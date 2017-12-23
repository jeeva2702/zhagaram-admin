package com.example.hp.zha.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.zha.Adapters.dummy;
import com.example.hp.zha.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by HP on 11/18/2017.
 */

public class schedule extends Fragment {
    CalendarView calendarView;
    ImageView ig1;
    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd:MM:yyyy");
    public schedule() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.schedule,container,false);
        calendarView=(CalendarView)view.findViewById(R.id.calendarView);
        String date =simpleDateFormat.format(calendar.getTime());
        ig1=(ImageView)view.findViewById(R.id.imageButton);
        long timemilis=calendar.getTimeInMillis();
//        calendarView.setDate(timemilis);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {
                String picke_date=""+day+":"+month+":"+year;
                Toast.makeText(getActivity(), ""+picke_date, Toast.LENGTH_SHORT).show();
                dummy.date_picked=picke_date;
                getFragmentManager().beginTransaction().replace(R.id.frame_container,new schedule_view()).commit();
            }
        });
        ig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }
}
