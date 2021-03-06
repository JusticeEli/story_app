package com.justice.storyapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class StoriesAdapter extends FirestoreRecyclerAdapter<StoryData, StoriesAdapter.ViewHolder> {
    private Context context;

    public StoriesAdapter(Context context, @NonNull FirestoreRecyclerOptions<StoryData> options) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StoryData model) {
        String date=new SimpleDateFormat("   HH:mm \n dd-MM-yyyy").format(model.getDate());

        holder.titleTxtView.setText(model.getTitle());
        holder.dateTxtView.setText(date);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stories, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTxtView;
        private TextView dateTxtView;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleTxtView = itemView.findViewById(R.id.titleTxtView);
            dateTxtView = itemView.findViewById(R.id.dateTxtView);
            setOnClickListeners();

        }

        private void setOnClickListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationClass.documentSnapshot=getSnapshots().getSnapshot(getAdapterPosition());
                    context.startActivity(new Intent(context,AddActivity.class));
                    }
            });

        }

    }








}
