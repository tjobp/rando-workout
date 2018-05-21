package com.tjobdev.randoworkout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class WorkoutParametersSelectionActivity extends AppCompatActivity {

    // Spinner variables for muscle spinners
    Spinner topMuscleSpinner;
    Spinner bottomMuscleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_parameters_selection);

        // instantiate spinners
        topMuscleSpinner = (Spinner) findViewById(R.id.topMuscleSpinner);
        bottomMuscleSpinner = (Spinner) findViewById(R.id.bottomMuscleSpinner);


        // create list of muscle group options to select from muscle spinners
        ArrayList<String> muscleGroups = new ArrayList<>();
        muscleGroups.add("Abdominal");
        muscleGroups.add("Back");
        muscleGroups.add("Biceps");
        muscleGroups.add("Calves");
        muscleGroups.add("Chest");
        muscleGroups.add("Legs");
        muscleGroups.add("Triceps");
        muscleGroups.add("Traps");
        muscleGroups.add("Shoulders");

        // connect array list to spinners with adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.top_spinner_style, muscleGroups);

        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        topMuscleSpinner.setAdapter(arrayAdapter);
        bottomMuscleSpinner.setAdapter(arrayAdapter);
    }

    // this function is called when the generateWorkoutButton is clicked
    public void generateWorkoutButtonClick(View v) {

        String firstMuscle = topMuscleSpinner.getSelectedItem().toString();
        String secondMuscle = bottomMuscleSpinner.getSelectedItem().toString();

        goToGeneratedRandoWorkoutActivity(firstMuscle, secondMuscle);

    }

    // this function puts selected muscles into intent and changes activity to 'GeneratedRandoWorkoutActivity'
    public void goToGeneratedRandoWorkoutActivity(String firstMuscle, String secondMuscle) {

        Intent goToRandoWorkoutActivityIntent = new Intent(getApplicationContext(), GeneratedRandoWorkoutActivity.class);

        goToRandoWorkoutActivityIntent.putExtra("firstMuscle", firstMuscle);

        goToRandoWorkoutActivityIntent.putExtra("secondMuscle", secondMuscle);

        startActivity(goToRandoWorkoutActivityIntent);

    }

}
