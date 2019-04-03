package com.me.beem.beep_me;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.me.beem.beep_me.Activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendBeepFragment extends Fragment {
    String beeperString = null;
    String[] beepedStringArray = null;
    String beepMessage = null;
    String key;
    String userId = null;

    private TextView beeperTextView;
    private TextView teacherBeepedLastNameTextView;
    private TextView teacherBeepedFirstNameTextView;
    private RadioGroup radioGroup;
    private Button beepButton;
    private Button retryButton;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;


    public SendBeepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_beep, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();


        teacherBeepedLastNameTextView = view.findViewById(R.id.teacherBeepedLastNameLanding);
        teacherBeepedFirstNameTextView = view.findViewById(R.id.teacherBeepedFirstNameLanding);
        beeperTextView = view.findViewById(R.id.beeperLanding);
        beepButton = view.findViewById(R.id.finalBeepButton);
        retryButton = view.findViewById(R.id.retryBeepButton);

        beepButton.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.GONE);

        beepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBeepButton();
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRetryButton();
            }
        });


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            beeperString = bundle.getString("beeper");
            beepedStringArray = bundle.getStringArray("beeped");
            beeperTextView.setText(beeperString);
            teacherBeepedLastNameTextView.setText(beepedStringArray[0] + ",");
            teacherBeepedFirstNameTextView.setText(beepedStringArray[1]);

        }

        radioGroup = view.findViewById(R.id.beepRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioB = radioGroup.findViewById(radioButtonID);
                int position = group.indexOfChild(radioB);
                if (position == 0) {
                    retryButton.setVisibility(View.VISIBLE);
                    beepButton.setVisibility(View.GONE);
                } else if (position == 1) {
                    beepButton.setVisibility(View.VISIBLE);
                    retryButton.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void setBeepButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("One Last Thing");
        builder.setIcon(R.drawable.ic_sentiment_satisfied_black_24dp);
        builder.setMessage("By clicking Accept, you are guaranteeing that you are" +
                " using Beep Me's notification system responsibly and without any ill intentions");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String beeper = beeperTextView.getText().toString();
                String beepedTeacher = teacherBeepedLastNameTextView.getText().toString();
                beepMessage = beeper + " beeped you!";
                dialog.dismiss();
                sendNotification();


            }
        });
        builder.show();
    }

    private void setRetryButton() {
        getActivity().getFragmentManager().popBackStack();
    }

    private void sendNotification() {
        if (hasInternetConnection()) {
            String lastNameBeeper;
            lastNameBeeper = beepedStringArray[0];

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
            if (lastNameBeeper.equals(lastNames[0])) {
                userId = userIDs[0];
            } else if (lastNameBeeper.equals(lastNames[1])) {
                userId = userIDs[1];
            } else if (lastNameBeeper.equals(lastNames[2])) {
                userId = userIDs[2];
            } else if (lastNameBeeper.equals(lastNames[3])) {
                userId = userIDs[3];
            } else if (lastNameBeeper.equals(lastNames[4])) {
                userId = userIDs[4];
            } else if (lastNameBeeper.equals(lastNames[5])) {
                userId = userIDs[5];
            } else if (lastNameBeeper.equals(lastNames[6])) {
                userId = userIDs[6];
            } else if (lastNameBeeper.equals(lastNames[7])) {
                userId = userIDs[7];
            } else if (lastNameBeeper.equals(lastNames[8])) {
                userId = userIDs[8];
            } else if (lastNameBeeper.equals(lastNames[9])) {
                userId = userIDs[9];
            } else if (lastNameBeeper.equals(lastNames[10])) {
                userId = userIDs[10];
            } else if (lastNameBeeper.equals(lastNames[11])) {
                userId = userIDs[11];
            } else if (lastNameBeeper.equals(lastNames[12])) {
                userId = userIDs[12];
            } else if (lastNameBeeper.equals(lastNames[13])) {
                userId = userIDs[13];
            } else if (lastNameBeeper.equals(lastNames[14])) {
                userId = userIDs[14];
            } else if (lastNameBeeper.equals(lastNames[15])) {
                userId = userIDs[15];
            } else if (lastNameBeeper.equals(lastNames[16])) {
                userId = userIDs[16];
            } else if (lastNameBeeper.equals(lastNames[17])) {
                userId = userIDs[17];
            }

            key = mReference.child("Users").child(userId).child("notifications").push().getKey();
            mReference.child("Users").child(userId).child("notifications").child(key).setValue(beepMessage);

            DatabaseReference notificationReference = mReference.child("Users").child(userId).child("notification").child(key);
            String finalUserId = userId;
            notificationReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                sendNotificationHandler(beepMessage);

                                Fragment success = new SuccessBeepingFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.add(R.id.beepActivityParentLayout, success).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                    //     String token = mReference.child("Users").child(finalUserId).child("notifications").child(key).getKey();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            String RED = "#de5750";
            CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.beepActivityParentLayout);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Network connection is required", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor(RED));
            snackbar.show();
        }
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void sendNotificationHandler(String beepMessage) {
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

        String userNotificationsFromDb;
        String userMessagingToken;
        userNotificationsFromDb = mReference.child("Users").child(userId).child("notifications").getKey();
        userMessagingToken = mReference.child("Users").child(userId).child("messagingToken").toString();
        //send Push Notification
        HttpsURLConnection connection = null;

        if (userId != null) {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=" +
                        "AAAAjeyFQ_0:APA91bE6yhcV0JemPZ9547X2a1mhYZY3pd4w6KuSoDtIpOTGnIK7OLcxA-Vi4anIfH2eKFULWtSuJbut00m2G59YxTg0F9QHoqZY3h0UNkM4IHZ_ZmocPreY0aO4eZV0-VGVPKG7XKrN");

                JSONObject root = new JSONObject();
                JSONObject data = new JSONObject();
               // data.put("Beep Me", senderId);
                root.put("data", data);
                root.put("to",  "/topics/user_" + userId);
                data.put("message", beepMessage);

                byte[] outputBytes = root.toString().getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);
                os.flush();
                os.close();
                connection.getInputStream(); //do not remove this line. request will not work without it gg

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
        }

    }
}
