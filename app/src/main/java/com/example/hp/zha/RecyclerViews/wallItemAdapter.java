package com.example.hp.zha.RecyclerViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.hp.zha.Adapters.wallAdapter;
import com.example.hp.zha.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by HP on 11/15/2017.
 */

public class wallItemAdapter  extends RecyclerView.Adapter<wallItemAdapter.ViewHolder>{
    private int listItemLayout;
    private ArrayList<wallAdapter> itemList;
    static MediaController mediacontroller;
   static String TimeLineUrl = "https://firebasestorage.googleapis.com/v0/b/zha-admin.appspot.com/o/Admin%2FPost%2F08%3A58%4027-11-2017%40shincan?alt=media&token=7ef9407a-bfe8-4e08-9a53-e185739570cc";
    static Context context;
    //static String TimeLineUrl="";
    public wallItemAdapter(int layout,ArrayList<wallAdapter> list){
        this.listItemLayout=layout;
        this.itemList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        final ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)

    {
        ProgressDialog pDialog1;
        TextView pt=holder.t1;
         TextView dt=holder.t2;
         TextView tt=holder.t3;
          VideoView videoview1=holder.videoview;
         ImageView image=holder.imageView;
         pt.setText(itemList.get(position).getTitl());
         dt.setText(itemList.get(position).getDate());
         tt.setText(itemList.get(position).getTime());
        if(itemList.get(position).getType().equals("IMG")){
            Glide.with(context).load(itemList.get(position).getUrl()).asBitmap().into(image);
            videoview1.setVisibility(View.GONE);
        }
        else if(itemList.get(position).getType().equals("VID")){
            try
            {

                // Get the URL from String VideoURL
                Uri video = Uri.parse(itemList.get(position).getUrl());
                videoview1.setMediaController(mediacontroller);
                videoview1.setVideoURI(video);

            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            image.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount()
    {
        return itemList==null?0:itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {   public TextView t1,t2,t3;
        public ImageView imageView;
        //ProgressDialog pDialog;

        VideoView videoview;

        public ViewHolder(View itemView) {
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.postdes);
            t2=(TextView)itemView.findViewById(R.id.walldate);
            t3=(TextView)itemView.findViewById(R.id.walltime);
            imageView=(ImageView)itemView.findViewById(R.id.wallimage);
            context=itemView.getContext();
           videoview = (VideoView) itemView.findViewById(R.id.exerciseVideo);
            // Start the MediaController
            mediacontroller = new MediaController(
                    itemView.getContext());
            mediacontroller.setAnchorView(videoview);



            // Execute StreamVideo AsyncTask

            // Create a progressbar
//            pDialog = new ProgressDialog(itemView.getContext());
//            // Set progressbar title
//            pDialog.setTitle(R.string.app_name);
//            // Set progressbar message
//            pDialog.setMessage("Buffering...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            // Show progressbar
//            pDialog.show();
//            Log.e("Error", TimeLineUrl+29);




        }

        @Override
        public void onClick(View view) {

        }
    }

}
