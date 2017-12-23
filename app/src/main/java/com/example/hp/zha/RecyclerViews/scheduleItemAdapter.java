package com.example.hp.zha.RecyclerViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.zha.Adapters.scheduleAdapter;
import com.example.hp.zha.R;

import java.util.ArrayList;

/**
 * Created by HP on 11/18/2017.
 */

public class scheduleItemAdapter extends RecyclerView.Adapter<scheduleItemAdapter.ViewHolder>{
    int listItemLayout;
    ArrayList<scheduleAdapter> list;
    public scheduleItemAdapter(int layout,ArrayList<scheduleAdapter> list1){
        listItemLayout=layout;
        list=list1;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        final ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {   TextView t=holder.t1;
        TextView d=holder.t2;
        d.setText(list.get(position).getDescrp());
        t.setText(list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {   TextView t1,t2;
        public ViewHolder(View itemView) {
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.textView9);
            t2=(TextView)itemView.findViewById(R.id.textView13);


        }

        @Override
        public void onClick(View view) {

        }
    }
}
