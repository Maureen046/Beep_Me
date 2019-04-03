package com.me.beem.beep_me.RecyclerViews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.beem.beep_me.Activities.MainActivity;
import com.me.beem.beep_me.Activities.ProfileActivity;
import com.me.beem.beep_me.Database.EntityInformation;
import com.me.beem.beep_me.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterForFemaleBrowse extends RecyclerView.Adapter<RecyclerViewAdapterForFemaleBrowse.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterForFemaleBrowse";
    private List<EntityInformation> entityInformations = new ArrayList<>();
    private OnTeacherClickListener onTeacherClickListener;

    public interface OnTeacherClickListener {
        void onTeacherClick(EntityInformation femaleTeacher);
    }

    public void setOnTeacherClickListener(OnTeacherClickListener listener) {
        onTeacherClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterForFemaleBrowse.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_recycler_browse_teachers, viewGroup, false);
        return new RecyclerViewAdapterForFemaleBrowse.ViewHolder(itemView, onTeacherClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        EntityInformation teacherBrowse = entityInformations.get(i);
        viewHolder.teacherLastNameBrowse.setText(teacherBrowse.getLastName());
        viewHolder.teacherFirstNameBrowse.setText(teacherBrowse.getFirstName());
        // viewHolder.profileImageBrowse.setImageResource(teacherBrowse.getPicture());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTeacherClickListener != null && viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onTeacherClickListener.onTeacherClick(entityInformations.get(viewHolder.getAdapterPosition()));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return entityInformations.size();
    }

    public void setFemaleBrowse(List<EntityInformation> entityInformations) {
        this.entityInformations = entityInformations;
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImageBrowse;
        private TextView teacherLastNameBrowse;
        private TextView teacherFirstNameBrowse;
        private RelativeLayout listHolderBrowse;


        public ViewHolder(@NonNull View itemView, OnTeacherClickListener onTeacherClickListener) {
            super(itemView);
            teacherLastNameBrowse = itemView.findViewById(R.id.listViewLastNameBrowse);
            teacherFirstNameBrowse = itemView.findViewById(R.id.listViewFirstNameBrowse);
            listHolderBrowse = itemView.findViewById(R.id.listIemViewBrowseTeachers);

        }
    }
}

