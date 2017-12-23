package com.example.hp.zha.RecyclerViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.zha.Adapters.dummy;
import com.example.hp.zha.Adapters.photoAdapter;
import com.example.hp.zha.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HP on 11/16/2017.
 */

public class photoItemAdapter  extends RecyclerView.Adapter<photoItemAdapter.ViewHolder>
{
    private int listItemLayout;
    private static ArrayList<photoAdapter> list1;
    static Context context1;
    public static Bitmap bitmap;
    public static int pos;
    public FirebaseStorage firebaseStorage;
    public photoItemAdapter(int layout,ArrayList<photoAdapter> list)
    {
        this.list1=list;
        this.listItemLayout=layout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        final ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {  TextView t1=holder.tv;
       ImageView t2=holder.imageView;
       Button b1=holder.save;
       pos=position;
       t1.setText(list1.get(position).getTitle());
        Glide.with(context1).load(list1.get(position).getSurl()).asBitmap().into(t2);


    }

    @Override
    public int getItemCount() {
        return list1==null?0:list1.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {   public TextView tv;
        public ImageView imageView;
        public Button save;
        public ViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.textView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            save=(Button)itemView.findViewById(R.id.savebutton);
            save.setOnClickListener(this);
            context1=itemView.getContext();

        }

        @Override
        public void onClick(View view) {
             if(view.getId()==R.id.savebutton){
                  File folder=new File(Environment.getExternalStorageDirectory()+"/Zhagaram/Photos");
                  boolean success=true;
                  if(!folder.exists()){
                      success=folder.mkdirs();
                  }
                  if(success){
                      String key= dummy.photokeyvalues.get(getLayoutPosition());

                      System.out.println("key" + key);
                      String name=tv.getText().toString();

                      StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("Admin").child("Photos").child(key);

                      String file=Environment.getExternalStorageDirectory()+"/Zhagaram/Photos/"+name+".jpg";
                      File files=new File(file);

                      storageReference.getFile(files).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                              System.out.println("Storage Successfull");

                          }
                      });
                  }

             }
        }
    }
}
