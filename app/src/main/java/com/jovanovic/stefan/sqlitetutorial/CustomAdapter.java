package com.jovanovic.stefan.sqlitetutorial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList list_id, list_title, list_type, list_color;
    CustomAdapter(Activity activity, Context context, ArrayList list_id, ArrayList list_title, ArrayList list_type,
                 ArrayList list_color){
        this.activity = activity;
        this.context = context;
        this.list_id = list_id;
        this.list_title = list_title;
        this.list_type = list_type;
        this.list_color = list_color;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.title_txt.setText(String.valueOf(list_title.get(position)));
        holder.type_txt.setText(String.valueOf(list_type.get(position)));
        try {
            holder.colorId.setBackgroundColor(Color.parseColor((String.valueOf(list_color.get(position)))));
        } catch (Exception e) {
            holder.colorId.setText(String.valueOf(list_color.get(position)));
        }

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intentI = new Intent(context, ItemList.class);
                intentI.putExtra("id",String.valueOf(list_id.get(position)));
                intentI.putExtra("title", String.valueOf(list_title.get(position)));
                intentI.putExtra("color", String.valueOf(list_color.get(position)));
                activity.startActivityForResult(intentI,1);
                return true;
            }
        });

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentI = new Intent(context, NameList.class);
                intentI.putExtra("id",String.valueOf(list_id.get(position)));
                intentI.putExtra("title", String.valueOf(list_title.get(position)));
                intentI.putExtra("color", String.valueOf(list_color.get(position)));
                activity.startActivityForResult(intentI,1);
            }
        });

        holder.mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("id", String.valueOf(list_id.get(position)));
                    intent.putExtra("title", String.valueOf(list_title.get(position)));
                    intent.putExtra("type", String.valueOf(list_type.get(position)));
                    intent.putExtra("color", String.valueOf(list_color.get(position)));
                    activity.startActivityForResult(intent, 1);
                }
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return list_id.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title_txt, type_txt,colorId;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_txt = itemView.findViewById(R.id.title_txt);
            type_txt = itemView.findViewById(R.id.type_txt);
            colorId = itemView.findViewById(R.id.colorId);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }

    }

}
