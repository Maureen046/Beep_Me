package com.me.beem.beep_me.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.me.beem.beep_me.R;
import com.mostafaaryan.transitionalimageview.TransitionalImageView;
import com.mostafaaryan.transitionalimageview.model.TransitionalImage;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileFragment";
    private Button changeStatus;
    private FloatingActionButton beepButton;
    private Button logOutButton;

    private TransitionalImageView landingImage;
    private TextView landingLastName;
    private TextView landingFirstName;
    private TextView landingMiddleName;
    private TextView landingSubjects;
    private TextView landingAdvisory;
    private TextView landingConsultation;
    private CoordinatorLayout parentLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    String[] info = null;
    String userId;


    ImageView statusImage;
    String statusResult;
    String[] statusAttendance;
    String[] statusAvailability;

    private Boolean isFromLogIn = null;

    private int checkedAttendance;
    private int checkedStatus;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;


    String lastName, firstName, middleName, subject, advisory, consultation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();

                if (mUser != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic("user_" + mUser);
                    userId = mUser.getUid();
                    hideBeepButton();
                    showChangeStatusButton();
                    showLogOutButton();

                } else {
                    showBeepButton();
                    hideChangeStatusButton();
                    hideLogOutButton();
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        setContentView(R.layout.activity_profile);


        parentLayout = findViewById(R.id.profileActivityParentLayout);
        landingImage = findViewById(R.id.landingPicture);

        landingLastName = findViewById(R.id.landingLastName);
        landingFirstName = findViewById(R.id.landingFirstName);
        landingMiddleName = findViewById(R.id.landingMiddleName);
        landingSubjects = findViewById(R.id.landingSubjects);
        landingAdvisory = findViewById(R.id.landingAdvisory);
        landingConsultation = findViewById(R.id.landingConsultation);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("info")) {
            info = intent.getStringArrayExtra("info");
            landingLastName.setText(info[0]);
            landingFirstName.setText(info[1]);
            landingMiddleName.setText(info[2]);
            landingSubjects.setText(info[3]);
            landingAdvisory.setText(info[4]);
            landingConsultation.setText(info[5]);
        } else if (intent != null && intent.hasExtra("fromLogin")) {
            isFromLogIn = intent.getBooleanExtra("fromLogin", false);
        }


        beepButton = findViewById(R.id.buttonBeep);
        beepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastName = landingLastName.getText().toString();
                firstName = landingFirstName.getText().toString();
                String[] fullName = {lastName, firstName};
                Intent intent = new Intent(getApplicationContext(), BeepActivity.class);
                intent.putExtra("name", fullName);
                startActivity(intent);
            }
        });


        logOutButton = findViewById(R.id.buttonLogOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = mAuth.getCurrentUser();
                String userId = mUser.getUid();
                mReference.child("Users").child(userId).child("messagingToken").setValue("");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + mUser.getUid().toString());
                mAuth.signOut();
                finish();
            }
        });

        statusImage = findViewById(R.id.landingStatusImage);

        changeStatus = findViewById(R.id.buttonChangeStatus);
        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hasInternetConnection();
                getStatusFromDatabase();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        showLogOutButton();
        showChangeStatusButton();


        if (mUser != null) {
            if (isFromLogIn != null) {
                isFromLoginSetPicture();
                showInfoLoggedIn();
                showChangeStatusButton();
                showLogOutButton();
                hideBeepButton();
            } else {
                hideBeepButton();
                hideLogOutButton();
                hideChangeStatusButton();
            }
        } else {
            showBeepButton();
            hideChangeStatusButton();
            hideLogOutButton();
        }

        if (isFromLogIn == null) {
            setPicture();
        }

        getStatusFromDatabase();
    }


    private void showInfoLoggedIn() {

        userId = mUser.getUid();
        String[] userIDs = {
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

        String[] lastNames = {
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

        String[] firstNames = {
                "Karol Ann",
                "Kate Nicoleen",
                "Lorela",
                "Bea May",
                "John Astley",
                "Abraham",
                "Florissa",
                "Jesus Jr.",
                "John Paulus",
                "Marvin",
                "Katrina",
                "Maylene",
                "Ramona",
                "Lovely Joy",
                "Ma. Isabel",
                "Maricar",
                "Karl Angelo II",
                "Salve"
        };

        String[] middlenames = {
                "Diego",
                "Buenafe",
                "Alconaba",
                "Mas",
                "Ansay",
                "Aquino",
                "Padullo",
                "Capital",
                "Arambulo",
                "Capitan",
                "Abdul",
                "Ercia",
                "Fajardo",
                "Eguia",
                "Espinosa",
                "Layuso",
                "Malupig",
                "Galang",
        };

        String[] subjects = {
                "Earth and Life Science\n" +
                        "General Chemistry 2",
                "Physical Education 2\n" +
                        "Physical Education 4",
                "Oblications and Contracts\n" +
                        "Contemporary Arts\n" +
                        "Politics and Government\n" +
                        "Business Ethics\n" +
                        "Communication Engagement",
                "Mobile App Programming 1 (Android Studio)\n" +
                        "Computer Programming 3\n" +
                        "Computer Programming 6\n" +
                        "Computer Hardware Fundamentals\n" +
                        "Empowerment Technology",
                "Mobile App Programming 1 (Android Studio)\n" +
                        "Mobile App Programming 2 (Xamarin)\n" +
                        "Empowerment Technology\n" +
                        "Advanced Programming (Java Enterprise Edition)\n" +
                        "IT Special Project\n" +
                        "Thesis 2",
                "Entrepreneurship\n" +
                        "Applied Economics\n" +
                        "Fundamentals of ABM 1\n" +
                        "Work Immersion\n" +
                        "Inquiries, Investigations and Immersion",
                "Probability and Statistics (College and SHS)\n" +
                        "Basic Calculus\n" +
                        "Practical Research 1\n" +
                        "Inquiries, Investigations and Immersion",
                "Komunikasyon at Pananaliksik",
                "Reading and Writing\n" +
                        "Inquiries, Investigations and Immersion\n" +
                        "Contemporary Philippine Arts from the Region\n" +
                        "Komunikasyon at Pananaliksik",
                "Inquiries, Investigations and Immersion\n" +
                        "Reading and Writing\n" +
                        "Practical Research 1",
                "Principles of Marketing\n" +
                        "Entrepreneurship\n" +
                        "Applied Economics\n" +
                        "Work Immersion",
                "Understanding Culture, Society and Politics\n" +
                        "Contemporary Philippine Arts from the Region",
                "Reading and Writing\n" +
                        "Inquiries, Investigations and Immersion\n" +
                        "Contemporary Philippine Arts from the Region",
                "General Biology 2\n" +
                        "Earth Science\n" +
                        "Earth and Life Science\n" +
                        "General Chemistry 2\n" +
                        "General Physics 2\n" +
                        "Work Immersion",
                "Practical Research 1\n" +
                        "Inquiries, Investigations and Immersion\n" +
                        "Reading and Writing\n" +
                        "Komunikasyon at Pananaliksik\n" +
                        "Work Immersion\n" +
                        "Contemporary Philippine Arts from the Region",
                "Understanding Culture, Society and Politics\n" +
                        "Contemporary Philippine Arts from the Region\n" +
                        "Business Ethics\n" +
                        "Art Appreciation",
                "Basic Calculus\n" +
                        "Probability and Statistics\n" +
                        "General Chemistry 2\n" +
                        "General Physics 2\n" +
                        "Physics for Engineers",
                "Work Immersion\n" +
                        "Reading and Writing\n" +
                        "Inquiries, Investigations and Immersion\n" +
                        "Contemporary Philippine Arts from the Region\n" +
                        "Communication Arts\n" +
                        "Practical Research 1",
        };

        String[] advisories = {
                "STEM 13\n" + "CULARTS 11",
                "NONE",
                "NONE",
                "NONE",
                "ITMAW 11\n" + "ITMAW 12",
                "ABM 23",
                "STEM 21\n" + "CULARTS 21",
                "ABM 22\n" + "STEM 22",
                "HOP 11\n" + "ITMAW 13",
                "NONE",
                "STEM 11\n" + "GAS 11",
                "ITMAW 23\n" + "TOP 11",
                "HOP 21\n" + "ABM 21",
                "STEM 24\n" + "ITMAW 22",
                "TOP 21\n" + "STEM 23",
                "NONE",
                "ITMAW 21\n" + "GAS 21",
                "ABM 11\n" + "STEM 12"
        };

        String[] consultations = {
                "MONDAY\n" + "TUESDAY\n" + "THURSDAY\n" + "FRIDAY\n" + "\n" + "4:00 PM - 6:00 PM onwards",
                "MONDAY 1:00 - 2:30 PM, 3:30 - 8:00 PM",
                "TUESDAY 8:30 - 11:30 AM",
                "EVERY WEDNESDAY",
                "MONDAY 1:00 - 2:30 PM, 3:30 - 8:00 PM",
                "MONDAY AND THURSDAY 1:00 - 2:30 PM\n" + "TUESDAY AND FRIDAY 8:30 AM - 10:00 AM, 2:30 PM - 4:00 PM",
                "WEDNESDAY 1:00 - 4:30 PM",
                "WEDNESDAY 12:00 - 2:30\n" + "MONDAY AND THURSDAY 2:30 - 5:30\n" + "TUESDAY AND FRIDAY 2:30 - 5:00",
                "MONDAY AND THURSDAY 8:00 - 10:00 AM",
                "MONDAY AND THURSDAY 10:00-12:00, 2:30-4:00\n" + "WEDNESDAY 7:00-10:00, 2:00-4:00\n" + "TUESDAY AND FRIDAY 8:30-12:00",
                "FRIDAY 8:00 - 4:00 PM",
                "TUESDAY AND FRIDAY 3:00 - 5:00 PM",
                "MONDAY AND THURSDAY 10:00 - 11:30\n" +
                        "TUESDAY 11:30 - 1:00, 3:30 - 4:00 PM\n" +
                        "WEDNESDAY 11:00 - 2:30\n" +
                        "FRIDAY 11:30 - 1:00, 2:30 - 4:00",
                "MONDAY AND THURSDAY 1:00 - 2:30\n" +
                        "TUESDAY AND FRIDAY 4:00 - 5:00\n" +
                        "WEDNESDAY 3:00 - 5:00",
                "MONDAY AND THURSDAY 2:30 - 5:00 PM\n" +
                        "TUESDAY AND FRIDAY 10:00 - 1:00 PM\n" +
                        "WEDNESDAY 10:00 - 2:00 PM",
                "MONDAY AND THURSDAY 8:30 - 10:00 AM, 4:00 - 6:00 PM\n" +
                        "TUESDAY AND FRIDAY 8:30 - 11:30 , 4:00 - 6:00 PM",
                "MONDAY 11:00 - 11:30 , 1:00 - 4:00\n" +
                        "TUESDAY 12:30 - 1:00\n" +
                        "WEDNESDAY 2:00 - 2:30\n" +
                        "THURSDAY 10:00 - 10:30, 1:00 - 4:00\n" +
                        "FRIDAY 12:30 - 1:00\n" +
                        "SATURDAY 9:00 - 10:00, 2:00 - 2:30",
                "MONDAY AND THURSDAY 7:00 - 10:00\n" +
                        "MONDAY ONLY 2:30 - 4:00\n" +
                        "TUESDAY AND FRIDAY 1:00 - 2:30 ",
        };


        if (userId.equals(userIDs[0])) {
            lastName = lastNames[0];
            firstName = firstNames[0];
            middleName = middlenames[0];
            subject = subjects[0];
            advisory = advisories[0];
            consultation = consultations[0];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[1])) {
            lastName = lastNames[1];
            firstName = firstNames[1];
            middleName = middlenames[1];
            subject = subjects[1];
            advisory = advisories[1];
            consultation = consultations[1];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[2])) {
            lastName = lastNames[2];
            firstName = firstNames[2];
            middleName = middlenames[2];
            subject = subjects[2];
            advisory = advisories[2];
            consultation = consultations[2];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[3])) {
            lastName = lastNames[3];
            firstName = firstNames[3];
            middleName = middlenames[3];
            subject = subjects[3];
            advisory = advisories[3];
            consultation = consultations[3];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[4])) {
            lastName = lastNames[4];
            firstName = firstNames[4];
            middleName = middlenames[4];
            subject = subjects[4];
            advisory = advisories[4];
            consultation = consultations[4];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[5])) {
            lastName = lastNames[5];
            firstName = firstNames[5];
            middleName = middlenames[5];
            subject = subjects[5];
            advisory = advisories[5];
            consultation = consultations[5];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[6])) {
            lastName = lastNames[6];
            firstName = firstNames[6];
            middleName = middlenames[6];
            subject = subjects[6];
            advisory = advisories[6];
            consultation = consultations[6];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[7])) {
            lastName = lastNames[7];
            firstName = firstNames[7];
            middleName = middlenames[7];
            subject = subjects[7];
            advisory = advisories[7];
            consultation = consultations[7];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[8])) {
            lastName = lastNames[8];
            firstName = firstNames[8];
            middleName = middlenames[8];
            subject = subjects[8];
            advisory = advisories[8];
            consultation = consultations[8];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[9])) {
            lastName = lastNames[9];
            firstName = firstNames[9];
            middleName = middlenames[9];
            subject = subjects[9];
            advisory = advisories[9];
            consultation = consultations[9];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[10])) {
            lastName = lastNames[10];
            firstName = firstNames[10];
            middleName = middlenames[10];
            subject = subjects[10];
            advisory = advisories[10];
            consultation = consultations[10];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[11])) {
            lastName = lastNames[11];
            firstName = firstNames[11];
            middleName = middlenames[11];
            subject = subjects[11];
            advisory = advisories[11];
            consultation = consultations[11];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[12])) {
            lastName = lastNames[12];
            firstName = firstNames[12];
            middleName = middlenames[12];
            subject = subjects[12];
            advisory = advisories[12];
            consultation = consultations[12];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[13])) {
            lastName = lastNames[13];
            firstName = firstNames[13];
            middleName = middlenames[13];
            subject = subjects[13];
            advisory = advisories[13];
            consultation = consultations[13];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[14])) {
            lastName = lastNames[14];
            firstName = firstNames[14];
            middleName = middlenames[14];
            subject = subjects[14];
            advisory = advisories[14];
            consultation = consultations[14];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[15])) {
            lastName = lastNames[15];
            firstName = firstNames[15];
            middleName = middlenames[15];
            subject = subjects[15];
            advisory = advisories[15];
            consultation = consultations[15];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[16])) {
            lastName = lastNames[16];
            firstName = firstNames[16];
            middleName = middlenames[16];
            subject = subjects[16];
            advisory = advisories[16];
            consultation = consultations[16];
            setTextLoggedIn();
        } else if (userId.equals(userIDs[17])) {
            lastName = lastNames[17];
            firstName = firstNames[17];
            middleName = middlenames[17];
            subject = subjects[17];
            advisory = advisories[17];
            consultation = consultations[17];
            setTextLoggedIn();
        }
    }


    private void setTextLoggedIn() {
        landingLastName.setText(lastName);
        landingFirstName.setText(firstName);
        landingMiddleName.setText(middleName);
        landingSubjects.setText(subject);
        landingAdvisory.setText(advisory);
        landingConsultation.setText(consultation);
    }

    private void hideChangeStatusButton() {
        changeStatus.setVisibility(View.INVISIBLE);
    }

    private void showChangeStatusButton() {
        changeStatus.setVisibility(View.VISIBLE);
    }

    private void hideLogOutButton() {
        logOutButton.setVisibility(View.GONE);
    }

    private void showLogOutButton() {
        logOutButton.setVisibility(View.VISIBLE);
    }

    private void hideBeepButton() {
        beepButton.hide();
    }

    private void showBeepButton() {
        beepButton.show();
    }


    private void changeStatus() {
        statusAttendance = new String[]{"Present", "Absent"};
        statusAvailability = new String[]{"Busy", "Available"};


        AlertDialog.Builder attendanceBuilder = new AlertDialog.Builder(this);
        attendanceBuilder.setTitle("Today I am:");
        attendanceBuilder.setCancelable(true);
        attendanceBuilder.setSingleChoiceItems(statusAttendance, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    checkedAttendance = 1;
                } else {
                    checkedAttendance = 2;
                }
            }
        });
        attendanceBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        attendanceBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkedAttendance == 1) {
                    setStatusAvailability();
                }
                if (checkedAttendance == 2) {
                    if (!hasInternetConnection()) {
                        String RED = "#de5750";
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(parentLayout, "Network connection is required to display status", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.parseColor(RED));
                        snackbar.show();
                    } else {
                        statusResult = "absent";
                        sendStatusToDatabase();
                        dialog.dismiss();
                    }

                }
            }
        });
        attendanceBuilder.show();
    }

    private void setStatusAvailability() {
        AlertDialog.Builder availabilityBuilder = new AlertDialog.Builder(this);
        availabilityBuilder.setTitle("My status:");
        availabilityBuilder.setCancelable(false);
        availabilityBuilder.setSingleChoiceItems(statusAvailability, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogAvailability, int which) {
                if (which == 0) {
                    checkedStatus = 1;
                } else {
                    checkedStatus = 2;
                }
            }
        });
        availabilityBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        availabilityBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkedStatus == 1) {
                    if (!hasInternetConnection()) {
                        String RED = "#de5750";
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(parentLayout, "Network connection is required to display status", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.parseColor(RED));
                        snackbar.show();
                    } else {
                        statusResult = "busy";
                        sendStatusToDatabase();
                        dialog.dismiss();
                    }

                }
                if (checkedStatus == 2) {
                    if (!hasInternetConnection()) {
                        String RED = "#de5750";
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(parentLayout, "Network connection is required to display status", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.parseColor(RED));
                        snackbar.show();
                    } else {
                        statusResult = "available";
                        sendStatusToDatabase();
                        dialog.dismiss();
                    }
                }
            }
        });
        availabilityBuilder.show();
    }


    private void isFromLoginSetPicture() {
        userId = mUser.getUid();
        String[] userIDs = {
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

        if (userId.equals(userIDs[0])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a01_abraham_karol)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[1])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a02_asia_kate)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[2])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a03_barundia_lorela)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);

        } else if (userId.equals(userIDs[3])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a04_belarmino_bea_may)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[4])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a05_bete_john_astley)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[5])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a06_bitancor_abraham)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[6])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a07_calinagan_florissa)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[7])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a08_elag_jesus)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[8])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a09_esguerra_paulus)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[9])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a10_evangelista_marvin)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[10])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a11_geluz_katrina)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[11])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a12_logo_maylene)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[12])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a13_marcelo_ramona)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[13])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a14_mayuga_lovely)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[14])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a15_mercado_isabel)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);

        } else if (userId.equals(userIDs[15])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a16_narsolis_maricar)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[16])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a17_ortega_karl)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        } else if (userId.equals(userIDs[17])) {
            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                    .duration(150)
                    .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                    .image(R.drawable.a18_puyod_salve)
                    /* or */
                    .create();
            landingImage.setTransitionalImage(transitionalImage);
        }
    }

    private void setPicture() {
        if (info != null) {
            String lastNameUser = info[0];
            switch (lastNameUser) {
                case "Abraham": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a01_abraham_karol)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Asia": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a02_asia_kate)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Barundia": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a03_barundia_lorela)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);

                    break;
                }
                case "Belarmino": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a04_belarmino_bea_may)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Bete": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a05_bete_john_astley)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Bitancor": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a06_bitancor_abraham)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Calinagan": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a07_calinagan_florissa)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Elag": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a08_elag_jesus)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Esguerra": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a09_esguerra_paulus)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Evangelista": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a10_evangelista_marvin)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Geluz": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a11_geluz_katrina)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Logo": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a12_logo_maylene)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Marcelo": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a13_marcelo_ramona)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Mayuga": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a14_mayuga_lovely)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Mercado": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a15_mercado_isabel)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);

                    break;
                }
                case "Narsolis": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a16_narsolis_maricar)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Ortega": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a17_ortega_karl)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
                case "Puyod": {
                    TransitionalImage transitionalImage = new TransitionalImage.Builder()
                            .duration(150)
                            .backgroundColor(ContextCompat.getColor(this, R.color.popup_background))
                            .image(R.drawable.a18_puyod_salve)
                            /* or */
                            .create();
                    landingImage.setTransitionalImage(transitionalImage);
                    break;
                }
            }
        }
    }

    private void getStatusFromDatabase() {
        String lastNameDisplayed;
        String userDisplayedUID = null;
        String[] userIDs = {
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
        String[] lastNames = {
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
        lastNameDisplayed = landingLastName.getText().toString();

        if (lastNameDisplayed.equals(lastNames[0])) {
            userDisplayedUID = userIDs[0];
        } else if (lastNameDisplayed.equals(lastNames[1])) {
            userDisplayedUID = userIDs[1];
        } else if (lastNameDisplayed.equals(lastNames[2])) {
            userDisplayedUID = userIDs[2];
        } else if (lastNameDisplayed.equals(lastNames[3])) {
            userDisplayedUID = userIDs[3];
        } else if (lastNameDisplayed.equals(lastNames[4])) {
            userDisplayedUID = userIDs[4];
        } else if (lastNameDisplayed.equals(lastNames[5])) {
            userDisplayedUID = userIDs[5];
        } else if (lastNameDisplayed.equals(lastNames[6])) {
            userDisplayedUID = userIDs[6];
        } else if (lastNameDisplayed.equals(lastNames[7])) {
            userDisplayedUID = userIDs[7];
        } else if (lastNameDisplayed.equals(lastNames[8])) {
            userDisplayedUID = userIDs[8];
        } else if (lastNameDisplayed.equals(lastNames[9])) {
            userDisplayedUID = userIDs[9];
        } else if (lastNameDisplayed.equals(lastNames[10])) {
            userDisplayedUID = userIDs[10];
        } else if (lastNameDisplayed.equals(lastNames[11])) {
            userDisplayedUID = userIDs[11];
        } else if (lastNameDisplayed.equals(lastNames[12])) {
            userDisplayedUID = userIDs[12];
        } else if (lastNameDisplayed.equals(lastNames[13])) {
            userDisplayedUID = userIDs[13];
        } else if (lastNameDisplayed.equals(lastNames[14])) {
            userDisplayedUID = userIDs[14];
        } else if (lastNameDisplayed.equals(lastNames[15])) {
            userDisplayedUID = userIDs[15];
        } else if (lastNameDisplayed.equals(lastNames[16])) {
            userDisplayedUID = userIDs[16];
        } else if (lastNameDisplayed.equals(lastNames[17])) {
            userDisplayedUID = userIDs[17];
        }

        if (userDisplayedUID != null) {
            if (hasInternetConnection()) {
                statusImage.setVisibility(View.VISIBLE);
                mReference.child("Users").child(userDisplayedUID).child("status").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        statusResult = dataSnapshot.getValue(String.class);
                        if (statusResult != null && statusResult.equals("available")) {
                            statusImage.setImageResource(R.drawable.status_available);
                            swipeRefreshLayout.isRefreshing();
                        } else if (statusResult != null && statusResult.equals("busy")) {
                            statusImage.setImageResource(R.drawable.status_busy);
                            swipeRefreshLayout.isRefreshing();
                        } else if (statusResult != null && statusResult.equals("absent")) {
                            statusImage.setImageResource(R.drawable.status_absent);
                            swipeRefreshLayout.isRefreshing();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            } else {

                statusImage.setVisibility(View.INVISIBLE);

                String RED = "#de5750";
                Snackbar snackbar = Snackbar.make(parentLayout, "Network connection is required to display status", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.parseColor(RED));
                snackbar.show();

            }
        }
    }


    private void sendStatusToDatabase() {
        String lastNameDisplayed;
        String userDisplayedUID = null;
        String[] userIDs = {
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
        String[] lastNames = {
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
        lastNameDisplayed = landingLastName.getText().toString();

        if (lastNameDisplayed.equals(lastNames[0])) {
            userDisplayedUID = userIDs[0];
        } else if (lastNameDisplayed.equals(lastNames[1])) {
            userDisplayedUID = userIDs[1];
        } else if (lastNameDisplayed.equals(lastNames[2])) {
            userDisplayedUID = userIDs[2];
        } else if (lastNameDisplayed.equals(lastNames[3])) {
            userDisplayedUID = userIDs[3];
        } else if (lastNameDisplayed.equals(lastNames[4])) {
            userDisplayedUID = userIDs[4];
        } else if (lastNameDisplayed.equals(lastNames[5])) {
            userDisplayedUID = userIDs[5];
        } else if (lastNameDisplayed.equals(lastNames[6])) {
            userDisplayedUID = userIDs[6];
        } else if (lastNameDisplayed.equals(lastNames[7])) {
            userDisplayedUID = userIDs[7];
        } else if (lastNameDisplayed.equals(lastNames[8])) {
            userDisplayedUID = userIDs[8];
        } else if (lastNameDisplayed.equals(lastNames[9])) {
            userDisplayedUID = userIDs[9];
        } else if (lastNameDisplayed.equals(lastNames[10])) {
            userDisplayedUID = userIDs[10];
        } else if (lastNameDisplayed.equals(lastNames[11])) {
            userDisplayedUID = userIDs[11];
        } else if (lastNameDisplayed.equals(lastNames[12])) {
            userDisplayedUID = userIDs[12];
        } else if (lastNameDisplayed.equals(lastNames[13])) {
            userDisplayedUID = userIDs[13];
        } else if (lastNameDisplayed.equals(lastNames[14])) {
            userDisplayedUID = userIDs[14];
        } else if (lastNameDisplayed.equals(lastNames[15])) {
            userDisplayedUID = userIDs[15];
        } else if (lastNameDisplayed.equals(lastNames[16])) {
            userDisplayedUID = userIDs[16];
        } else if (lastNameDisplayed.equals(lastNames[17])) {
            userDisplayedUID = userIDs[17];
        }

        if (userDisplayedUID != null) {
            mReference.child("Users").child(userDisplayedUID).child("status").setValue(statusResult);
        }
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }


}