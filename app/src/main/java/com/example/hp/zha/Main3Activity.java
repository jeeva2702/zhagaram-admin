package com.example.hp.zha;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hp.zha.YouTube.YoutubeMain;
import com.example.hp.zha.fragments.myact;
import com.example.hp.zha.fragments.photos;
import com.example.hp.zha.fragments.post;
import com.example.hp.zha.fragments.schedule;

public class Main3Activity extends AppCompatActivity {
    int index;
    public FragmentManager fragmentManager=getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        String st=getIntent().getExtras().getString("index").toString();
        index=Integer.parseInt(st);
        if(index==1)
        {
            //i Toast.makeText(this, "Photos", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction().replace(R.id.frame_container,new photos()).commit();

        }
        else if(index==0)
        {
            fragmentManager.beginTransaction().replace(R.id.frame_container,new post()).commit();
        }
        else if(index==3){
            fragmentManager.beginTransaction().replace(R.id.frame_container,new schedule()).commit();
        }
        else if(index==5){
            fragmentManager.beginTransaction().replace(R.id.frame_container,new myact()).commit();
        }
        else if (index==2)
        {
            startActivity(new Intent(getApplicationContext(), YoutubeMain.class));
        }
    }
}
