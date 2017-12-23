package com.example.hp.zha.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.zha.Adapters.Permision;
import com.example.hp.zha.Adapters.photoAdap;
import com.example.hp.zha.Adapters.postAdap;
import com.example.hp.zha.Filepath;
import com.example.hp.zha.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
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

/**
 * Created by HP on 11/17/2017.
 */

public class create_post extends Fragment
{   public TextInputEditText td;
    ImageView ig1,ig2,imageView;
    FloatingActionButton photoUpload,send;
    public static final int PICK_IMAGE = 1;
    public static final int RESULT_LOAD_IMAGE = 2;
    Uri imageData;
    public Bitmap bitmap;
    public String  picturePath;
    Uri yourUri,imageUri    ;
    public ProgressDialog progressDialog;
    public Firebase fb;
    String cur;
    public String url="https://zha-admin.firebaseio.com/";
    public StorageReference fb_stg;
    public FirebaseStorage storage;
    String userChoosenTask;
    Uri SelectedUri;
    FloatingActionButton uploadVideo;
    int REQUEST_CAMERA = 99,SELECT_FILE = 100,VIDEO_CAPTURE=101,REQUEST_PICK_VIDEO=102;
    String path;
    Uri VideoUri;
    public create_post()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.post_create,container,false);
        ig1=(ImageView)view.findViewById(R.id.imageButton);
        ig2=(ImageView)view.findViewById(R.id.imageView2);
        uploadVideo = (FloatingActionButton)view.findViewById(R.id.uploadVideo);
        photoUpload=(FloatingActionButton)view.findViewById(R.id.uploadPhoto);
        send=(FloatingActionButton)view.findViewById(R.id.sendpost);
        imageView=(ImageView)view.findViewById(R.id.post_image);
        td=(TextInputEditText)view.findViewById(R.id.posttitle);
        Firebase.setAndroidContext(getActivity());
        fb = new Firebase(url);
        ig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container,new post()).commit();
            }
        });
        photoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                cur = "PHOTO";
                selectImage();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur.equals("VID"))
                {
                    new MyTaskVid().execute();
                }
                if(cur.equals("PHOTO"))
                {
                    new MyTask().execute();
                }

            }
        });
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur = "VID";
                selectVideo();
            }
        });

        return view;
    }
    private void selectVideo() {
        final CharSequence[] items = { "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose video..");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Video")) {
                    File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                    int cnt=f.listFiles().length;
                    System.out.println("FIle cnt is "+cnt+"and they are"+f.listFiles());
                    File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/myvideo"+Integer.toString(cnt+1)+".mp4");
                    path=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/myvideo"+Integer.toString(cnt+1)+".mp4";
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    VideoUri = Uri.fromFile(mediaFile);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, VideoUri);
                    startActivityForResult(intent, VIDEO_CAPTURE);


                } else if (items[item].equals("Choose from Library")) {
                    Intent gintent = new Intent();
                    gintent.setType("video/*");
                    gintent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(
                            Intent.createChooser(gintent, "Select Picture"),
                            REQUEST_PICK_VIDEO);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


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
            if (requestCode == SELECT_FILE) {
                SelectedUri = data.getData();
                System.out.println("URI FILE " + SelectedUri);
                imageView.setImageURI(SelectedUri);
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {

                imageView.setImageURI(SelectedUri);
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_PICK_VIDEO) {
                if (data.equals(null)) {
                    Toast.makeText(getActivity(), "Video was not selected", Toast.LENGTH_SHORT).show();
                } else {
                    VideoUri = data.getData();
                    Toast.makeText(getActivity(), VideoUri.toString(), Toast.LENGTH_SHORT).show();
                    Filepath obj = new Filepath();
                    path = obj.getPaths(getActivity(), VideoUri);
                    System.out.println("VIDEOO" + VideoUri + " d " + VideoUri.getPath());

                }
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







        public class MyTask extends AsyncTask<String, Integer, String>{

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Creating Post...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            fb_stg=FirebaseStorage.getInstance().getReference();
            Calendar c =Calendar.getInstance();
            SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat ssf=new SimpleDateFormat("HH:mm:ss");
            final String date=sdf.format(c.getTime());
            final String time=ssf.format(c.getTime());
            System.out.println("ammo"+time+"****"+date);
            final String posttitle=td.getText().toString();

            StorageReference stg=fb_stg.child("Admin").child("Post").child(time+"@"+date);
            stg.putFile(SelectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    String downloadurl=downloaduri.toString();
                    postAdap postadap=new postAdap();
                    postadap.setImgurl(downloadurl);
                    postadap.setTitle(posttitle);
                    fb.child("Admin").child("Post").child(time+"@"+date+"@IMG").setValue(postadap);
//                    progressDialog.dismiss();
//                    Toast.makeText(getActivity(),"Post Created",Toast.LENGTH_SHORT).show();
                }
            });


            return null;
        }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Post Created",Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.frame_container,new post()).commit();

            }
        }
    public class MyTaskVid extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Creating Post...");
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
            final String posttitle=td.getText().toString();


            StorageReference stg=fb_stg.child("Admin").child("Post").child(time+"@"+date+"@"+posttitle);
            stg.putFile(VideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    String downloadurl=downloaduri.toString();
                    postAdap postadap=new postAdap();
                    postadap.setImgurl(downloadurl);
                    postadap.setTitle(posttitle);
                    fb.child("Admin").child("Post").child(date+"@"+time+"@VID").setValue(postadap);

                    getFragmentManager().beginTransaction().replace(R.id.frame_container,new post()).commit();
                }
            });

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getActivity(),"Post Created",Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().replace(R.id.frame_container,new post()).commit();

        }

    }
}

