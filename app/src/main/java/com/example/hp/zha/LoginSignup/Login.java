package com.example.hp.zha.LoginSignup;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.zha.Adapters.UserCreds;
import com.example.hp.zha.MainActivity;
import com.example.hp.zha.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;
import test.jinesh.easypermissionslib.EasyPermission;

public class Login extends AppCompatActivity {

    Firebase fb_db;
    String Base_url = "https://zha-admin.firebaseio.com/Admin/AdminCreds/";
    EasyPermission easyPermission;
    EditText input_mail, input_password;
    Button button_login;
    TextView no_account;
    String mail, password;
    Calligrapher calligrapher;
    UserCreds userCreds;
    int i;
    ArrayList<String> Creds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(getApplicationContext());
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        calligrapher = new Calligrapher(this);
        calligrapher.setFont(Login.this, "Ubuntu_R.ttf", true);
        no_account = (TextView) findViewById(R.id.textView3);
        input_mail = (EditText) findViewById(R.id.input_mail);
        input_password = (EditText) findViewById(R.id.input_password);
        button_login = (Button) findViewById(R.id.button_login);
         SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
         SharedPreferences.Editor edit=sharedPreferences.edit();
        Boolean islog=false;
        islog=sharedPreferences.getBoolean("login",false);

        if(islog){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            this.finish();
        }
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = input_mail.getText().toString();
                password = input_password.getText().toString();
                new MyTask().execute();


            }
        });

        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(final String... strings) {

            Firebase fb_db = new Firebase(Base_url);
            fb_db.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        i=0;
                        Creds = new ArrayList<String>();
                        System.out.println("LOL 1"+postSnapshot.getKey());
                        for (com.firebase.client.DataSnapshot child : postSnapshot.getChildren())
                        {
                            System.out.println("#### "+i+" "+child.getValue());
                            i++;
                            Creds.add(child.getValue().toString());
                        }
                        if ((mail.equals(Creds.get(3)))&&(password.equals(Creds.get(4))))
                        {
                            Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor edit=sharedPreferences.edit();
                            edit.putBoolean("login",true);
                            edit.putString("username",Creds.get(3));
                            edit.putString("password",Creds.get(4));
                            edit.commit();



                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }




            });
            return null;
        }
    }
}




