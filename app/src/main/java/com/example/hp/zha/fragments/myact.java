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


import com.example.hp.zha.Adapters.Adapter;
import com.example.hp.zha.R;
import com.example.hp.zha.RecyclerViews.itemAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by HP on 7/13/2017.
 */

public class myact extends Fragment
{
    public ArrayList<Adapter> adapters=new ArrayList<Adapter>();
    //public itemAdapter itemArrayAdapter = new itemAdapter(R.layout.row,adapters);
    RecyclerView recyclerView1;
    ImageView ig1,ig2;
    Firebase fb = new Firebase("https://zha-admin.firebaseio.com/");
    public myact()
    {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view;
        Firebase.setAndroidContext(getActivity());
        view=inflater.inflate(R.layout.eventfragment,container,false);
        ig1=(ImageView)view.findViewById(R.id.imageButton);
        ig2=(ImageView)view.findViewById(R.id.imageView2);
        ig1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });
        ig2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().add(R.id.frame_container,new create_event()).commit();
            }
        });
        new MyTask().execute();
       recyclerView1=(RecyclerView)view.findViewById(R.id.recyclerView);
       recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        //recyclerView.addItemDecoration(itemDecoration);
////        recyclerView.setItemAnimator(new DefaultItemAnimator());   new
//        adapters.add(new Adapter("BirthDay","Am organizing a party ","2/7/2017","5/7/2017","Puducherry",""+2,""+0));

        itemAdapter itemArrayAdapter = new itemAdapter(R.layout.row2,adapters);
       recyclerView1.setAdapter(itemArrayAdapter);
        return view ;

    }
        public class MyTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {
                fb.child("Admin").child("Events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            System.out.println(child.getKey().toString()+"bow");
                            Adapter adapter = child.getValue(Adapter.class);
    //                    Adapter adapter = dataSnapshot.getValue(Adapter.class);
                            adapters.add(0, adapter);
                            System.out.println(adapters.get(0).getName() + "bow");
    //                        adapters.add(adapter);
                            System.out.println("child: " + dataSnapshot.getKey());
                        }
//                        Collections.reverse(adapters);
                        itemAdapter itemArrayAdapter= new itemAdapter(R.layout.row2, adapters);
                        recyclerView1.setAdapter(itemArrayAdapter);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }


}
