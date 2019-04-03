package com.me.beem.beep_me.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.me.beem.beep_me.R;
import com.me.beem.beep_me.SendBeepFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeepActivity extends AppCompatActivity {
    private TextView landingLastName;
    private TextView landingFirstName;
    private ImageButton closeButton;
    private Spinner gradeLevelSpinner;
    private Spinner sectionSpinner;
    private TextInputLayout beeperNameLayout;
    private TextInputEditText beeperNameEditText;

    private TextView beeper;
    String beeperNameAndGradeString;
    String beeperNameString;
    String[] name;
    private Button nextButton;

    private String gradeLevel;
    private String section;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beep);

        landingLastName = findViewById(R.id.beepLastNameLanding);
        landingFirstName = findViewById(R.id.beepFirstNameLanding);
        beeperNameLayout = findViewById(R.id.beeperNameLayout);
        beeperNameEditText = findViewById(R.id.beeperEditText);

        beeperNameLayout.setVisibility(View.INVISIBLE);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextButton();
            }
        });


        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCloseButton();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("name")) {
            name = intent.getStringArrayExtra("name");
            landingLastName.setText(name[0] + ",");
            landingFirstName.setText(name[1]);
        }

        beeper = findViewById(R.id.beeperTextview);

        gradeLevelSpinner = findViewById(R.id.gradeLevelSpinner);

        sectionSpinner = findViewById(R.id.sectionsSpinner);
        sectionSpinner.setVisibility(View.INVISIBLE);


        String[] grade = new String[]{
                "Select grade level...",
                "Grade 11",
                "Grade 12"
        };
        final List<String> gradeList = new ArrayList<>(Arrays.asList(grade));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, gradeList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        gradeLevelSpinner.setAdapter(spinnerArrayAdapter);


        gradeLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                beeperNameEditText.setText("");

                if (position == 1) {
                    gradeLevel = selectedItemText;
                    sectionSpinner.setVisibility(View.VISIBLE);
                    setSectionSpinner();
                } else if (position == 2) {
                    gradeLevel = selectedItemText;
                    sectionSpinner.setVisibility(View.VISIBLE);
                    setSectionSpinner12();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSectionSpinner() {
        String[] section11 = new String[]{
                "Select section...",
                "TOP 1",
                "CULARTS 1",
                "GAS 1",
                "ABM 1",
                "ITMAWD 1",
                "ITMAWD 2",
                "ITMAWD 3",
                "STEM 1",
                "STEM 2",
                "STEM 3"
        };

        final List<String> section11List = new ArrayList<>(Arrays.asList(section11));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, section11List) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        sectionSpinner.setAdapter(spinnerArrayAdapter);


        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beeperNameEditText.setText("");
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position == 0) {
                    section = "";
                } else if (position == 1) {
                    section = selectedItemText;
                } else if (position == 2) {
                    section = selectedItemText;
                } else if (position == 3) {
                    section = selectedItemText;
                } else if (position == 4) {
                    section = selectedItemText;
                } else if (position == 5) {
                    section = selectedItemText;
                } else if (position == 6) {
                    section = selectedItemText;
                } else if (position == 7) {
                    section = selectedItemText;
                } else if (position == 8) {
                    section = selectedItemText;
                } else if (position == 9) {
                    section = selectedItemText;
                } else if (position == 10) {
                    section = selectedItemText;
                } else if (position == 11) {
                    section = selectedItemText;
                }

                beeperNameAndGradeString = gradeLevel + " " + section;
                beeper.setText(beeperNameAndGradeString);

                if (!section.equals("")) {
                    beeperNameLayout.setVisibility(View.VISIBLE);
                    setBeeperNameInput();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSectionSpinner12() {
        String[] section12 = new String[]{
                "Select section...",
                "CULARTS 1",
                "TOP 1",
                "HOP 1",
                "GAS 1",
                "ABM 1",
                "ABM 2",
                "ABM 3",
                "ITMAWD 1",
                "ITMAWD 2",
                "ITMAWD 3",
                "STEM 1",
                "STEM 2",
                "STEM 3",
                "STEM 4"
        };
        final List<String> section12List = new ArrayList<>(Arrays.asList(section12));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, section12List) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        sectionSpinner.setAdapter(spinnerArrayAdapter);

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beeperNameEditText.setText("");
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position == 0) {
                    section = "";
                } else if (position == 1) {
                    section = selectedItemText;
                } else if (position == 2) {
                    section = selectedItemText;
                } else if (position == 3) {
                    section = selectedItemText;
                } else if (position == 4) {
                    section = selectedItemText;
                } else if (position == 5) {
                    section = selectedItemText;
                } else if (position == 6) {
                    section = selectedItemText;
                } else if (position == 7) {
                    section = selectedItemText;
                } else if (position == 8) {
                    section = selectedItemText;
                } else if (position == 9) {
                    section = selectedItemText;
                } else if (position == 10) {
                    section = selectedItemText;
                } else if (position == 11) {
                    section = selectedItemText;
                } else if (position == 12) {
                    section = selectedItemText;
                } else if (position == 13) {
                    section = selectedItemText;
                } else if (position == 14) {
                    section = selectedItemText;
                }
                beeperNameAndGradeString = gradeLevel + " " + section;
                beeper.setText(beeperNameAndGradeString);

                if (!section.equals("")) {
                    beeperNameLayout.setVisibility(View.VISIBLE);
                    setBeeperNameInput();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setBeeperNameInput() {

        beeperNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beeperNameString = beeperNameEditText.getText().toString();
                if (beeperNameString.equals("")) {
                    nextButton.setVisibility(View.INVISIBLE);
                } else {
                    beeperNameAndGradeString = beeperNameString + " from " +gradeLevel + " " + section;
                    beeper.setText(beeperNameAndGradeString);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                beeperNameString = beeperNameEditText.getText().toString();
                if (beeperNameString.equals("") || section.equals("")) {
                    nextButton.setVisibility(View.INVISIBLE);
                } else {
                    beeperNameAndGradeString = beeperNameString + " from " +gradeLevel + " " + section;
                    beeper.setText(beeperNameAndGradeString);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    private void setNextButton() {
        Bundle bundle = new Bundle();
        bundle.putString("beeper", beeperNameAndGradeString);
        bundle.putStringArray("beeped", name);
        Fragment sendBeep = new SendBeepFragment();
        sendBeep.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.sendBeepLanding, sendBeep)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    private void setCloseButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to cancel beeping?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}
