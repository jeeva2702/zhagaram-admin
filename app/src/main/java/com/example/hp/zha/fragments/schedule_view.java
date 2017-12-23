package com.example.hp.zha.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.zha.Adapters.dummy;
import com.example.hp.zha.Adapters.schAdap;
import com.example.hp.zha.Adapters.scheduleAdapter;
import com.example.hp.zha.R;
import com.example.hp.zha.RecyclerViews.scheduleItemAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by HP on 11/18/2017.
 */

public class schedule_view extends Fragment
{
    TextView t1,t2;
    ImageView ig1;
    RecyclerView recyclerView;
    ArrayList<scheduleAdapter> sad=new ArrayList<>();
    scheduleItemAdapter sID;
    Firebase fb;
    public schedule_view() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view1;
        view1 = inflater.inflate(R.layout.schedulerecycler, container, false);
        t1=(TextView)view1.findViewById(R.id.add);
        t2=(TextView)view1.findViewById(R.id.date);
        ig1=(ImageView)view1.findViewById(R.id.imageButton);
        t2.setText(dummy.date_picked);
        Firebase.setAndroidContext(getActivity());
        fb= new Firebase("https://zha-admin.firebaseio.com/");
        new MyTask().execute();
        recyclerView=(RecyclerView)view1.findViewById(R.id.recycler_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(view1.getContext()));
//        sad.add(new scheduleAdapter());
//        sad.add(new scheduleAdapter());
//        sad.add(new scheduleAdapter());
//        sad.add(new scheduleAdapter());
//        sad.add(new scheduleAdapter());
        ig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container,new schedule()).commit();
            }
        });

        sID=new scheduleItemAdapter(R.layout.schedulecard,sad);
        recyclerView.setAdapter(sID);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().replace(R.id.frame_container,new create_schedule()).commit();
            }
        });

        return view1;
    }
    public class MyTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            fb.child("Admin").child("Schedule").child(dummy.date_picked).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        schAdap schadap=child.getValue(schAdap.class);
                        sad.add(new scheduleAdapter(child.getKey(),schadap.getDesc()));
                    }
                    sID=new scheduleItemAdapter(R.layout.schedulecard,sad);
                    recyclerView.setAdapter(sID);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }


}
