package com.me.beem.beep_me.TeachersListBrowse;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.me.beem.beep_me.Activities.MainActivity;
import com.me.beem.beep_me.Activities.ProfileActivity;
import com.me.beem.beep_me.Database.EntityInformation;
import com.me.beem.beep_me.Database.ViewModelFemaleTeachers;
import com.me.beem.beep_me.Database.ViewModelMaleTeachers;
import com.me.beem.beep_me.R;
import com.me.beem.beep_me.RecyclerViews.RecyclerViewAdapterForFemaleBrowse;
import com.me.beem.beep_me.RecyclerViews.RecyclerViewAdapterForMaleBrowse;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MaleTeachersBrowseFragment extends Fragment {
    private ViewModelMaleTeachers viewModelMaleTeachers;


    public MaleTeachersBrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) Objects.requireNonNull(getActivity())).updateStatusBarColor("#385666");
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.BrowseMaleTeachers);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View v = localInflater.inflate(R.layout.fragment_male_teachers_browse, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewBrowseMale);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

        RecyclerViewAdapterForMaleBrowse adapterForMaleBrowse = new RecyclerViewAdapterForMaleBrowse();
        recyclerView.setAdapter(adapterForMaleBrowse);
        adapterForMaleBrowse.setOnTeacherClickListener(new RecyclerViewAdapterForMaleBrowse.OnTeacherClickListener() {
            @Override
            public void onTeacherClick(EntityInformation maleTeacher) {
                MaleTeachersBrowseFragment.open(getActivity(),
                        maleTeacher.getLastName(),
                        maleTeacher.getFirstName(),
                        maleTeacher.getMiddleName(),
                        maleTeacher.getSubjects(),
                        maleTeacher.getAdvisory(),
                        maleTeacher.getConsultationHours());
            }
        });

        viewModelMaleTeachers = ViewModelProviders.of(this).get(ViewModelMaleTeachers.class);
        viewModelMaleTeachers.getMaleTeachers().observe(this, new Observer<List<EntityInformation>>() {
            @Override
            public void onChanged(@Nullable List<EntityInformation> entityInformations) {
                adapterForMaleBrowse.setMaleBrowse(entityInformations);
            }
        });



        return v;

    }
    private static void open(Activity activity, String lastName, String firstName, String middleName, String subjects, String advisory, String consultationHours) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        String[] info = {lastName, firstName, middleName, subjects, advisory, consultationHours};
        intent.putExtra("info", info);
        activity.startActivity(intent);
    }

}
