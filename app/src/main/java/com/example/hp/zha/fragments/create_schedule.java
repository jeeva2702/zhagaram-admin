package com.example.hp.zha.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.zha.Adapters.dummy;
import com.example.hp.zha.Adapters.schAdap;
import com.example.hp.zha.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by HP on 11/18/2017.
 */

public class create_schedule extends Fragment implements TimePickerDialog.OnTimeSetListener{

    FloatingActionButton time,add;
    ImageView ig1;
    TextInputEditText ed;
    TextView ti;
    Firebase firebase;
    public create_schedule() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view2;
        view2=inflater.inflate(R.layout.schedulesetter,container,false);
        Firebase.setAndroidContext(getActivity());

        time=(FloatingActionButton)view2.findViewById(R.id.time);
        ti=(TextView)view2.findViewById(R.id.timeText) ;
        ed=(TextInputEditText) view2.findViewById(R.id.desc);
        ig1=(ImageView)view2.findViewById(R.id.imageButton);
        add=(FloatingActionButton)view2.findViewById(R.id.add);
        Firebase.setAndroidContext(view2.getContext());
        ig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container,new schedule_view()).commit();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(
                        create_schedule.this,
                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);

                timepickerdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(getActivity(), "Cancel choosing time", Toast.LENGTH_SHORT).show();
                    }
                });
                timepickerdialog.show(getFragmentManager(), "Timepickerdialog"); //show time picker dialog

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new MyTask().execute();

            }
        });
        return view2;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+ hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String stime = hourString + "h " + minuteString + "m " + secondString + "s";
        Toast.makeText(getActivity(), ""+stime, Toast.LENGTH_SHORT).show();
        ti.setText(stime);
    }
    public class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            firebase = new Firebase("https://zha-admin.firebaseio.com/");
            String descr=ed.getText().toString();
            String time=ti.getText().toString();
            schAdap schadap=new schAdap();
            schadap.setDesc(descr);
            firebase.child("Admin").child("Schedule").child(dummy.date_picked).child(time).setValue(schadap);
            getFragmentManager().beginTransaction().replace(R.id.frame_container,new schedule_view()).commit();
            return null;
        }
    }

}
