package com.example.hp.zha.YouTube;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.hp.zha.Adapters.Adapter;
import com.example.hp.zha.Adapters.YTVideos;
import com.example.hp.zha.R;
import com.example.hp.zha.RecyclerViews.itemAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

public class YoutubeMain extends AppCompatActivity {

    Firebase fb;
    String Base_url = "https://zha-admin.firebaseio.com/";
    //RECYCLER VIEW FIELD
    String Link;
    RecyclerView recyclerView;
    EditText YTlink;
    FloatingActionButton YTbut;
    YTVideos ytVideos;
    //VECTOR FOR VIDEO URLS
    Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();
    ArrayList<String> Links = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_main);

        Firebase.setAndroidContext(getApplicationContext());
        fb = new Firebase(Base_url);
//        YTlink = (EditText)findViewById(R.id.YTlink);
//        YTbut = (FloatingActionButton)findViewById(R.id.YTbut);

//                        new MyTask().execute();

//        YTbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar c =Calendar.getInstance();
//                SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
//                SimpleDateFormat ssf=new SimpleDateFormat("HH:mm:ss");
//                final String date=sdf.format(c.getTime());
//                final String time=ssf.format(c.getTime());
//                Link = YTlink.getText().toString();
//                YTVideos.YTLink=Link;
//                fb.child("Admin").child("YTVideos").child(time+"@"+date).setValue(YTVideos.YTLink);
//            }
//        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        //Load video List//

        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/OFK3zXbJ_BA?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/aBcsBcO7Lj8?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/di_HTg5mWpA&t=24s?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/bcLPLch5azo?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/nYG0JUJeC-g&t=4s?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/T24HgsurpII?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/BeOC6sf1F8c?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xj0yc5evIqE?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/gSn1nX-tTT0?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/JCr0Zio_ERY&t=264s?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );

        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);


    }
//    public class MyTask extends AsyncTask<String, Integer, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            fb.child("Admin").child("YTVideos").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot)
//                {
//                    Links = new ArrayList<>();
//                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//
//
//
//
////                        System.out.println("child: " + dataSnapshot.getKey());
////                        YTVideos ytVideos = child.getValue(YTVideos.class);
////                        System.out.println("$$$$$$"+YTVideos.YTLink);
////                        String[] S = YTVideos.YTLink.split("v=");
////                        System.out.print("ADDINGGG "+S[1]);
////
////                        Links.add(S[1]);
//                    }
////                    Collections.reverse(Links);
////                    System.out.print("KEYSSSS "+Links);
////                    for (int i=0;i<Links.size();i++)
////                    {
////                        System.out.println("@@@@@@@@"+Links.get(i));
////                        youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+Links.get(i)+"?ecver=1\" frameborder=\"0\" allowfullscreen></iframe>") );
////
////                    }
////                    VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
////                    recyclerView.setAdapter(null);
////                    recyclerView.setAdapter(videoAdapter);
////                    Collections.reverse(adapters);
////                    itemAdapter itemArrayAdapter= new itemAdapter(R.layout.row2, adapters);
////                    recyclerView1.setAdapter(itemArrayAdapter);
//
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//    }


}
