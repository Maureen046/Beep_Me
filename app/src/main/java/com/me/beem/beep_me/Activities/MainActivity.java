package com.me.beem.beep_me.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.me.beem.beep_me.MyPagerAdapter;
import com.me.beem.beep_me.R;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    String[] userIDs;
    String[] lastNames;

    String token;

    FirebaseAuth auth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.mainViewPager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        initStringArrays();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        if (mUser != null) {
            FirebaseMessaging.getInstance().subscribeToTopic("user_" + mUser.getUid().toString());
        }


    }


    public void updateStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (mPager.getCurrentItem() == 0) {
            if (count == 0) {
                super.onBackPressed();
                updateStatusBarColor("#FCE67C");
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
            } else {
                // Otherwise, select the previous step.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                // getFragmentManager().popBackStack();
            }
        }
    }

    private void initStringArrays(){
       userIDs = new String[]{
               "6rkxkCPG13WCeZraCcg6SEnQ3Sg1",
               "WWZu7mbGdONcLN2LFKC2zaiAuX53",
               "0Rk16YKONNb6TrKLwMWhvp56HMl1",
               "kppaOBFMRsZs6S5O4nKYksDyhnG2",
               "O21YNHCjy9QZTSwQ9k6vxPNCWR13",
               "VnJYUAtO45Oc74CRwocMXVpztNg2",
               "nOSRBiw0vMN8CPucEDFZuTE73PD3",
               "BNjRJ56suJNZzMU1ZFC6GHkbqsx2",
               "MqQHdLi7QmXUdIG9wmdTNcdsqRv1",
               "ixyBntwzSceKKw21HyuKHoq068p2",
               "Ee5iL7XXTlfPmwME3XpvYrXgrI62",
               "glTz63IrVsO2UZ6BS63j8RgQXgs1",
               "jccxtjVArwMqXqD2JNI2Cj0etLm1",
               "XIwGr4DEnNOFpJjzPMMTqPwNTuK2",
               "VZQIHiYA0WZ6eEysQh8sjpSss4B3",
               "kNYooe68dmSjBxYr2rQC80zjgag2",
               "5XDgxpxEwcXhOYc1MABrIROjZ6p1",
               "3QenydwlgIWrAbdHxZDbPK5Sbu42"
       };

        lastNames = new String[]{
                "Abraham",
                "Asia",
                "Barundia",
                "Belarmino",
                "Bete",
                "Bitancor",
                "Calinagan",
                "Elag",
                "Esguerra",
                "Evangelista",
                "Geluz",
                "Logo",
                "Marcelo",
                "Mayuga",
                "Mercado",
                "Narsolis",
                "Ortega",
                "Puyod"
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUser != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + mUser.getUid().toString());
        }

    }
}


