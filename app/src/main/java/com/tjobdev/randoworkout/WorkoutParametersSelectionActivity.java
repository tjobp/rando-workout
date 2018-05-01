package com.tjobdev.randoworkout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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
        /*

        ExerciseAPIConnection firstConnectionToExerciseAPITask = new ExerciseAPIConnection();
        ExerciseAPIConnection secondConnectionToExerciseAPITask = new ExerciseAPIConnection();

        JSONArray firstMuscleExerciseList = null;
        JSONArray secondMuscleExerciseList = null;

        try {

            // get list of exercises for selected muscles
            String firstMuscle = topMuscleSpinner.getSelectedItem().toString();
            Log.i("Top Spinner Muscle", muscleNamesAndIds.get(firstMuscle).toString());
            firstMuscleExerciseList = firstConnectionToExerciseAPITask.execute("https://wger.de/api/v2/exercise/?muscles=" + muscleNamesAndIds.get(firstMuscle).toString() + "&status=2&format=json&language=2&limit=100").get();
            Log.i("First Exercise List", firstMuscleExerciseList.toString());

            String secondMuscle = bottomMuscleSpinner.getSelectedItem().toString();
            Log.i("Bottom Spinner Muscle", muscleNamesAndIds.get(secondMuscle).toString());
            secondMuscleExerciseList = secondConnectionToExerciseAPITask.execute("https://wger.de/api/v2/exercise/?muscles=" + muscleNamesAndIds.get(secondMuscle).toString() + "&status=2&format=json&language=2&limit=100").get();
            Log.i("Second Exercise List", secondMuscleExerciseList.toString());

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        JSONArray randoWorkout = generateWorkout(firstMuscleExerciseList, secondMuscleExerciseList);

        goToGeneratedRandoWorkoutActivity(randoWorkout);
        */
    }


    public JSONArray generateWorkout(JSONArray firstMuscleExerciseList, JSONArray secondMuscleExerciseList) {

        JSONArray randoWorkout = new JSONArray();

        int firstMuscleExerciseListSize = firstMuscleExerciseList.length();
        int secondMuscleExerciseListSize = secondMuscleExerciseList.length();

        Random randomNumber = new Random();
        int firstExerciseIndex, secondExerciseIndex, thirdExerciseIndex;


        JSONObject firstExercise;
        JSONObject secondExercise;
        JSONObject thirdExercise;
        JSONObject fourthExercise;
        JSONObject fifthExercise;
        JSONObject sixthExercise;

        try {

            firstExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

            firstExercise = firstMuscleExerciseList.getJSONObject(firstExerciseIndex);

            randoWorkout.put(firstExercise);


            secondExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

            while(secondExerciseIndex == firstExerciseIndex) {

                secondExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

            }

            secondExercise = firstMuscleExerciseList.getJSONObject(secondExerciseIndex);

            randoWorkout.put(secondExercise);


            thirdExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

            while(thirdExerciseIndex == firstExerciseIndex || thirdExerciseIndex == secondExerciseIndex) {

                thirdExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

            }

            thirdExercise = firstMuscleExerciseList.getJSONObject(thirdExerciseIndex);

            randoWorkout.put(thirdExercise);

            // ***********************************************************
            // second muscle exercises
            // ***********************************************************

            firstExerciseIndex = randomNumber.nextInt(secondMuscleExerciseListSize);

            fourthExercise = secondMuscleExerciseList.getJSONObject(firstExerciseIndex);

            randoWorkout.put(fourthExercise);


            secondExerciseIndex = randomNumber.nextInt(secondMuscleExerciseListSize);

            while(secondExerciseIndex == firstExerciseIndex) {

                secondExerciseIndex = randomNumber.nextInt(secondMuscleExerciseListSize);

            }

            fifthExercise = secondMuscleExerciseList.getJSONObject(secondExerciseIndex);

            randoWorkout.put(fifthExercise);


            thirdExerciseIndex = randomNumber.nextInt(secondMuscleExerciseListSize);

            while(thirdExerciseIndex == firstExerciseIndex || thirdExerciseIndex == secondExerciseIndex) {

                thirdExerciseIndex = randomNumber.nextInt(secondMuscleExerciseListSize);

            }

            sixthExercise = secondMuscleExerciseList.getJSONObject(thirdExerciseIndex);

            randoWorkout.put(sixthExercise);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Random workouts", randoWorkout.toString());
        Log.i("Random workout size", Integer.toString(randoWorkout.length()));
        return randoWorkout;

    }

    public void goToGeneratedRandoWorkoutActivity(String firstMuscle, String secondMuscle) {

        Intent goToRandoWorkoutActivityIntent = new Intent(getApplicationContext(), GeneratedRandoWorkoutActivity.class);

        //Bundle bundle = new Bundle();

        //bundle.putString("exerciseList", randoWorkout.toString());

        //goToRandoWorkoutActivityIntent.putExtras(bundle);

        goToRandoWorkoutActivityIntent.putExtra("firstMuscle", firstMuscle);

        goToRandoWorkoutActivityIntent.putExtra("secondMuscle", secondMuscle);

        startActivity(goToRandoWorkoutActivityIntent);

    }

}
