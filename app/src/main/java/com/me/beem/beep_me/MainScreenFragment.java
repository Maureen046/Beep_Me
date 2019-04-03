package com.me.beem.beep_me;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.me.beem.beep_me.Activities.LoginActivity;
import com.me.beem.beep_me.TeachersListBrowse.FemaleTeachersBrowseFragment;
import com.me.beem.beep_me.TeachersListBrowse.MaleTeachersBrowseFragment;
import com.me.beem.beep_me.Activities.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainScreenFragment extends Fragment {
    private BottomSheetBehavior mBottomSheetBehaviour;
    private CardView maleTeachers;
    private CardView femaleTeachers;

    private EditText searchBar;
    String searchValue;

    private ImageButton goToLogin;
    private ImageButton goToSearch;

    private Boolean isEmpty;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;


    public MainScreenFragment() {
        // Required empty public constructor
    }

    public static android.app.Fragment newInstance() {
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);


        goToLogin = (ImageButton) view.findViewById(R.id.profileLogin);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        View nestedScrollView = view.findViewById(R.id.drawer);
        mBottomSheetBehaviour = BottomSheetBehavior.from(nestedScrollView);

        maleTeachers = (CardView) view.findViewById(R.id.maleTeachersCardview);
        femaleTeachers = (CardView) view.findViewById(R.id.femaleTeachersCardview);

        cardViewClick();
        return view;
    }

    private void login() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void cardViewClick() {
        maleTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment maleBrowse = new MaleTeachersBrowseFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.mainLanding, maleBrowse)
                        .addToBackStack(null);
                transaction.commit();
            }
        });

        femaleTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment femaleBrowse = new FemaleTeachersBrowseFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.mainLanding, femaleBrowse)
                        .addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void goToSearchButton() {
        searchValue = searchBar.getText().toString();
        if (TextUtils.isEmpty(searchValue)) {
            return;
        }

        Fragment searchResults = new SearchResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search", searchValue);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        searchResults.setArguments(bundle);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(R.id.mainLanding, searchResults);
        transaction.addToBackStack(null);
        transaction.commit();
        searchBar.setText("");
    }


}