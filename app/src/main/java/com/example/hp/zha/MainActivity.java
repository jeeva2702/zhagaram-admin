package com.example.hp.zha;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hp.zha.Adapters.dummy;
import com.example.hp.zha.Adapters.postAdap;
import com.example.hp.zha.Adapters.postAdapter;
import com.example.hp.zha.Adapters.wallAdap;
import com.example.hp.zha.Adapters.wallAdapter;
import com.example.hp.zha.RecyclerViews.postItemAdapter;
import com.example.hp.zha.RecyclerViews.wallItemAdapter;
import com.example.hp.zha.fragments.post;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    Firebase fb;
    private wallItemAdapter wId;
    private ArrayList<wallAdapter> wallList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CircleMenuView menu = (CircleMenuView) findViewById(R.id.circle_menu);
        Firebase.setAndroidContext(this);
        fb = new Firebase("https://zha-admin.firebaseio.com/");
        new MyTask().execute();
//        for(int i=0;i<=20;i++)
//        {
//            wallList.add(new wallAdapter());
//        }
        recyclerView=(RecyclerView)findViewById(R.id.recycler_wall);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wId=new wallItemAdapter(R.layout.wall_card,wallList);
        recyclerView.setAdapter(wId);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view)
            {
                Log.d("D", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view)
            {
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view)
            {
                Log.d("D", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view)
            {
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index)
            {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);

            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index)
            {
                Log.d("D", "onButtonClickAnimationEnd| index: " + index);
                Intent i=new Intent(MainActivity.this,Main3Activity.class);
                i.putExtra("index",""+index);
                startActivity(i);
            }
        });
    }
    public class MyTask extends AsyncTask<String , Integer, String > {

        @Override
        protected String doInBackground(String... strings) {
            fb.child("Admin").child("Post").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        System.out.println("bow"+child.getKey());
                        wallAdap walladap=child.getValue(wallAdap.class);
                        System.out.println("bow"+walladap.getTitle());
                        String s=child.getKey();

                        String[] ss=s.split("@");
                        System.out.println("bow"+ss[0]);


                        wallList.add(new wallAdapter(walladap.getTitle(),walladap.getImgurl(),ss[1],ss[0],ss[2]));

                    }
                    Collections.reverse(wallList);
                    wallItemAdapter ptD=new wallItemAdapter(R.layout.wall_card,wallList);
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
