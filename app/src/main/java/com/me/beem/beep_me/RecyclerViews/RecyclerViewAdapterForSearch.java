package com.me.beem.beep_me.RecyclerViews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.beem.beep_me.Database.EntityInformation;
import com.me.beem.beep_me.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterForSearch extends RecyclerView.Adapter<RecyclerViewAdapterForSearch.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterForSearch";

    private List<EntityInformation> entityInformations = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_recycler_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        EntityInformation teacher = entityInformations.get(position);
        viewHolder.teacherLastName.setText(teacher.getLastName());
        viewHolder.teacherFirstName.setText(teacher.getFirstName());

    }

    @Override
    public int getItemCount() {
        return entityInformations.size();
    }

    public void setSearchedTeachers(List<EntityInformation> entityInformations){
        this.entityInformations =  entityInformations;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView teacherLastName;
        private TextView teacherFirstName;
        private RelativeLayout listHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.listViewImage);
            teacherLastName = itemView.findViewById(R.id.listViewLastName);
            teacherFirstName = itemView.findViewById(R.id.listViewFirstName);
            listHolder = itemView.findViewById(R.id.listIemView);
        }
    }
}
