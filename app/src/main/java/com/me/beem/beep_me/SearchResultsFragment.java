package com.me.beem.beep_me;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.beem.beep_me.Activities.MainActivity;
import com.me.beem.beep_me.Database.DatabaseInformation;
import com.me.beem.beep_me.Database.DatabaseRepository;
import com.me.beem.beep_me.Database.EntityInformation;
import com.me.beem.beep_me.Database.ViewModelSearch;
import com.me.beem.beep_me.RecyclerViews.RecyclerViewAdapterForSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {
    private TextView searchedName;
    private String stringSearchedName;

    private DatabaseInformation databaseInformation;
    private EntityInformation teachers;


    private ViewModelSearch viewModelSearch;
    private CoordinatorLayout coordinatorLayout;

    private List<EntityInformation> entityInformations = new ArrayList<>();

    public SearchResultsFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) Objects.requireNonNull(getActivity())).updateStatusBarColor("#ffffff");
        View v = inflater.inflate(R.layout.fragment_search_results, container, false);

        coordinatorLayout = v.findViewById(R.id.searchLinearLayout);

        searchedName = (TextView) v.findViewById(R.id.landingNameSearched);
        stringSearchedName = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            stringSearchedName = bundle.getString("search");
            searchedName.setText("\" " + stringSearchedName + " \"");
        }

//        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewSearch);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        RecyclerViewAdapterForSearch adapterForSearch = new RecyclerViewAdapterForSearch();
//        recyclerView.setAdapter(adapterForSearch);
//
//        viewModelSearch = ViewModelProviders.of(this).get(ViewModelSearch.class);
//        viewModelSearch.getSearchResults().observe(this, new Observer<List<EntityInformation>>() {
//            @Override
//            public void onChanged(@Nullable List<EntityInformation> entityInformations) {
//               adapterForSearch.setSearchedTeachers(entityInformations);
//            }
//        });


        return v;
    }

    private void searched(List<EntityInformation> entityInformations){
        this.entityInformations =  entityInformations;
    }

}
