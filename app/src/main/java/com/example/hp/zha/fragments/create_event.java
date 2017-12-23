package com.example.hp.zha.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hp.zha.Adapters.Adapter;
import com.example.hp.zha.Adapters.DT;
import com.example.hp.zha.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HP on 7/20/2017.
 */

public class create_event extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    int i,c=0;
    //public ArrayList<Adapter> adapters=new ArrayList<Adapter>();
    //public itemAdapter itemArrayAdapter = new itemAdapter(R.layout.row,adapters);
    TextView textview, textview2, textView, textview4, en,des;
    EditText loca;
    public static EditText loc;
    Spinner spinner;
    ImageView ig1,ig2,imageView;
    RecyclerView recyclerView1;
    StorageReference storageReference;
    CharSequence[] items = {"Take Photo", "Choose from library", "Cancel"};
    public static final int PICK_IMAGE = 1;
    public static final int RESULT_LOAD_IMAGE = 2;
    Uri imageData;
    public Bitmap bitmap;
    public String  picturePath;
    Uri yourUri;
    FloatingActionButton fab1, fab2;
    String sdate, edate;
    Button setevent;
    public Adapter ad;
    public ArrayList<Adapter> adap2=new ArrayList<Adapter>();
    public ArrayList<String> sr=new ArrayList<String>();
    NotificationManager Nm;
    public PendingIntent pi;
    DateFormat df;
    String date;
    ProgressDialog progressDialog;
    public int a=0;
    Uri selectedImageUri,imageUri;
    //Context CurrentObj=getActivity();
    Firebase fb;
    public Context context1;
    public Iterator<String> iterator=sr.iterator();
    public FragmentManager fragmentManager=getFragmentManager();
    View view;
  myact et=new myact();

    public create_event()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view2;
        view=inflater.inflate(R.layout.event1,container,false);
        context1=view.getContext();
        Firebase.setAndroidContext(getActivity());
        fb = new Firebase("https://zha-admin.firebaseio.com/");
        imageView = (ImageView)view. findViewById(R.id.profile_image);
        ig1=(ImageView)view.findViewById(R.id.imageButton);
        ig2=(ImageView)view.findViewById(R.id.imageView2);
        ig1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().add(R.id.frame_container,new myact()).commit();
            }
        });

        //view2=inflater.inflate(R.layout.fragment,container,false);
        //recyclerView1=(RecyclerView)view2.findViewById(R.id.recyclerView);
        //recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //final GPSTracker gpsTracker = new GPSTracker(view.getContext());
        loc=(EditText)view.findViewById(R.id.textView7);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        textview  = (TextView)view. findViewById(R.id.textView);
        textview2  = (TextView) view.findViewById(R.id.textView2);
        textView  = (TextView)view. findViewById(R.id.enddate);
        textview4  = (TextView)view. findViewById(R.id.textView4);
        loca=(EditText)view.findViewById(R.id.textView7);
        setevent=(Button)view.findViewById(R.id.setEvent);
        en=(TextView)view.findViewById(R.id.event);
        des=(TextView)view.findViewById(R.id.description);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Choose image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (items[i].equals("Take Photo")) {
                            String fileName = "new-photo-name.jpg";
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, fileName);
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                            //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE);
                            Toast.makeText(view.getContext(),"Say Cheese",Toast.LENGTH_LONG).show();
                        }
                        if (items[i].equals("Choose from library")) {
                            bitmap=null;
                            imageData=null;
                            Intent it = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(it, RESULT_LOAD_IMAGE);
                            Toast.makeText(view.getContext(),"Select One Picture",Toast.LENGTH_LONG).show();


                        }
                        if (items[i].equals("Cancel")) {
                            Toast.makeText(view.getContext(),"Go Back To Redo Action",Toast.LENGTH_LONG).show();

                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        Nm=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(view.getContext(), Main3Activity.class);
//        pi = PendingIntent.getActivity(view.getContext(), 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = 0;
                Calendar now = Calendar.getInstance();
                DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                        create_event.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                datepickerdialog.show(getFragmentManager(), "DatePickerdialog"); //show dialog
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(create_event.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(), "DatePickerdailog");
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 1;
                //context1.startActivity(new Intent(getActivity(),MapsActivity.class));

            }
        });

        setevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DT.setEname(en.getText().toString());
                DT.setDes(des.getText().toString());
                DT.setLocation(loca.getText().toString());
                System.out.println("location"+loca.getText().toString());
                new MyTask().execute();


//                getFragmentManager().beginTransaction().add(R.id.frame_container,new myact()).commit();
            }
        });

        return view;
    }


    @Override
    public void onDateSet(DatePickerDialog view1, int year, int monthOfYear, int dayOfMonth) {
        if (i == 0) {
            sdate = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            DT.setD(dayOfMonth);
            DT.setM(++monthOfYear);
            DT.setY(year);
            DT.setStartD(sdate);
            Toast.makeText(view1.getActivity(), sdate, Toast.LENGTH_SHORT).show();
        } else {
            edate = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            DT.setEd(dayOfMonth);
            DT.setEm(++monthOfYear);
            DT.setEy(year);
            DT.setEndD(edate);
            //textView.setText(edate);
            Toast.makeText(view1.getActivity(), edate, Toast.LENGTH_SHORT).show();
        }


        Calendar now = Calendar.getInstance();
        TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(
                create_event.this,
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);

        timepickerdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getActivity(), "Cancel choosing time", Toast.LENGTH_SHORT).show();
            }
        });
        timepickerdialog.show(getFragmentManager(), "Timepickerdialog"); //show time picker dialog


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PICK_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {

                bitmap = (Bitmap) data.getExtras().get("data");
                imageData=imageUri;
                imageView.setImageBitmap(bitmap);
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK)
            {
                try {
                    imageData = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(imageData,filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);


                    yourUri = Uri.parse(picturePath);

//                    Toast.makeText(getActivity(),picturePath,Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "URi"+"     "+yourUri, Toast.LENGTH_LONG).show();



                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageData);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        // String time = hourString + "h" + minuteString + "m" + secondString + "s";
        if (i == 0) {
            String stime = hourString + "h" + minuteString + "m" + secondString + "s";
            DT.setH(hourOfDay);
            DT.setMin(minute);
            textview2.setText(sdate);
            textview.setText(stime);
            DT.setStartT(stime);
//            snackbar = Snackbar
//                    .make(c1, "Alarm set for " +stime+" on "+sdate, Snackbar.LENGTH_LONG)
//                    .setAction("Dismiss", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                        Snackbar snackbar1 = Snackbar.make(c1, "Message is restored!", Snackbar.LENGTH_SHORT);
////                        snackbar1.show();
//                            snackbar.dismiss();
//                        }
//                    });

//            snackbar.show();
        }
        else
        {
            String etime = hourString + "h" + minuteString + "m" + secondString + "s";
            DT.setEh(hourOfDay);
            DT.setEmin(minute);
            textView.setText(edate);
            textview4.setText(etime);
            DT.setEntT(etime);
//            snackbar = Snackbar
//                    .make(c1, "Alarm set for " +etime+" on "+edate, Snackbar.LENGTH_LONG)
//                    .setAction("Dismiss", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                        Snackbar snackbar1 = Snackbar.make(c1, "Message is restored!", Snackbar.LENGTH_SHORT);
////                        snackbar1.show();
//                            snackbar.dismiss();
//                        }
//                    });
//
//            snackbar.show();
        }

    }



    public class MyTask extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            fb=new Firebase("https://zha-admin.firebaseio.com/");
            storageReference= FirebaseStorage.getInstance().getReference();
            final Adapter event=new Adapter();
            event.setSd(DT.getStartD());
            event.setDesp(DT.getDes());
            event.setName(DT.getEname());


            event.setLocc(loca.getText().toString());
            System.out.println("locccc"+event.getLocc());
            event.setEd(DT.getEndD());

//            eventadapters event=new eventadapters();
//            event.setEname(DT.ename);
//            event.setDesc(DT.des);
//            event.setStartd(DT.startD);
//            event.setStartt(DT.startT);
//            event.setLocation(location);
//            event.setLat(DT.lat);
//            event.setLng(DT.lng);
//            event.setEndd(DT.endD);
//            event.setEndt(DT.endT);


            df=new SimpleDateFormat("d MMM yyyy,HH:mm:ss a");
            date=df.format(Calendar.getInstance().getTime());



            StorageReference sr=storageReference.child("Admin").child("Events").child(date);
            sr.putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    System.out.println("bowbow"+taskSnapshot.getStorage());
                    // Uri ndi=taskSnapshot.getUploadSessionUri();
                    // System.out.println("BOW"+ndi.toString());
                    String DownloadUri=downloaduri.toString();
                    event.setUrl(DownloadUri);
                    event.setCom("0");
                    event.setNotcom("0");
                    fb.child("Admin").child("Events").child(date).setValue(event);
                }
            });

            progressDialog.dismiss();
            a=0;
            getFragmentManager().beginTransaction().add(R.id.frame_container,new myact()).commit();

            return null;
        }

            @Override
            protected void onPreExecute() {
                progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setMessage("Creating event...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPreExecute();

            }
    }




}

