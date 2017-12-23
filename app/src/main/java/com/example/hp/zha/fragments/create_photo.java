package com.example.hp.zha.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.zha.Adapters.Permision;
import com.example.hp.zha.Adapters.photoAdap;
import com.example.hp.zha.Adapters.postAdap;
import com.example.hp.zha.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by HP on 11/17/2017.
 */

public class create_photo extends Fragment {
    ImageView ig1, ig2, imageView;
    public EditText des;
    FloatingActionButton send;
    public ProgressDialog progressDialog;
    public StorageReference fb_stg;
    public Firebase fb;
    double progress = 0.0;
    Notification notification;
    String userChoosenTask;
    int REQUEST_CAMERA = 100;
    int SELECT_FILE = 101;
    Uri SelectedUri=null;
    private String selectedImagePath;
    String mCurrentPhotoPath;
    Firebase Fb_db;
    String Base_Url="https://zha-admin.firebaseio.com/";
    StorageReference storageReference;
    public create_photo() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view1;
        view1 = inflater.inflate(R.layout.create_photos, container, false);
        ig1 = (ImageView) view1.findViewById(R.id.imageButton);
        imageView=(ImageView)view1.findViewById(R.id.select);
        des=(EditText)view1.findViewById(R.id.description);
        send=(FloatingActionButton)view1.findViewById(R.id.floatingActionButton3);
        Firebase.setAndroidContext(getActivity());
        fb = new Firebase(Base_Url);

        ig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new photos()).commit();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                UploadTask Up = storageReference.putFile(SelectedUri);
//                Up.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getActivity(),"Image Uploaded",Toast.LENGTH_SHORT).show();
//                    }
//                });
                new MyTask().execute();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectImage();
            }
        });
        return view1;
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Permision.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        SelectedUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        System.out.println("URI CAMMMMM"+SelectedUri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, SelectedUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Permision.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                SelectedUri = data.getData();
                System.out.println("URI FILE "+SelectedUri);
                imageView.setImageURI(SelectedUri);
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){

                imageView.setImageURI(SelectedUri);
                onCaptureImageResult(data);
            }
        }
    }
    private void onSelectFromGalleryResult(Intent data)
    {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),"new-photo-name.jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(thumbnail);
    }

    public class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Creating event...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            fb_stg= FirebaseStorage.getInstance().getReference();
            Calendar c =Calendar.getInstance();
            SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat ssf=new SimpleDateFormat("HH:mm");
            final String date=sdf.format(c.getTime());
            final String time=ssf.format(c.getTime());
            System.out.println("ammo"+time);
            final String posttitle=des.getText().toString();

            StorageReference stg=fb_stg.child("Admin").child("Photos").child(date+"@"+time+"@"+posttitle);
            stg.putFile(SelectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    String downloadurl=downloaduri.toString();
                    photoAdap photoadap=new photoAdap();
                    photoadap.setPurl(downloadurl);
                    photoadap.setTitle(posttitle);
                    fb.child("Admin").child("Photos").child(date+"@"+time+"@"+posttitle).setValue(photoadap);
                    progressDialog.dismiss();
                    getFragmentManager().beginTransaction().replace(R.id.frame_container,new photos()).commit();
                }
            });

            return null;
        }

    }



}

