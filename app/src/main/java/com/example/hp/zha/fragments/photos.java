package com.example.hp.zha.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hp.zha.Adapters.dummy;
import com.example.hp.zha.Adapters.photoAdap;
import com.example.hp.zha.Adapters.photoAdapter;
import com.example.hp.zha.Adapters.postAdap;
import com.example.hp.zha.Adapters.postAdapter;
import com.example.hp.zha.R;
import com.example.hp.zha.RecyclerViews.photoItemAdapter;
import com.example.hp.zha.RecyclerViews.postItemAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by HP on 11/16/2017.
 */

public class photos extends Fragment {
         RecyclerView recyclerView;
         ImageView ig1,ig2;
         Firebase fb;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
        ArrayList<photoAdapter> pa=new ArrayList<>();
        photoItemAdapter pTD;
    public photos()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.photos,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_photos);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        Firebase.setAndroidContext(getActivity());
        fb = new Firebase("https://zha-admin.firebaseio.com/");
        ig1=(ImageView)view.findViewById(R.id.imageButton);
        ig2=(ImageView)view.findViewById(R.id.imageView2);

        ig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        ig2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dummy.photokeyvalues.clear();
                getFragmentManager().beginTransaction().add(R.id.frame_container,new create_photo()).commit();
            }
        });
        new MyTask().execute();
//        int i=10;
//        while(i>=0) {
//            pa.add(new photoAdapter());
//            i--;
//        }
//        pTD=new photoItemAdapter(R.layout.photos_card,pa);
//        recyclerView.setAdapter(pTD);
        return view;
    }

    public class MyTask extends AsyncTask<String , Integer, String > {

        @Override
        protected String doInBackground(String... strings) {
            fb.child("Admin").child("Photos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        System.out.println("bow"+child.getKey());
                        photoAdap photoadap=child.getValue(photoAdap.class);
                        System.out.println("bow"+photoadap.getTitle());
                        dummy.photokeyvalues.add(child.getKey());


                        pa.add(new photoAdapter(photoadap.getTitle(),photoadap.getPurl()));

                    }
                    Collections.reverse(pa);
                    photoItemAdapter ptD=new photoItemAdapter(R.layout.photos_card,pa);
                    recyclerView.setAdapter(ptD);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }





}
