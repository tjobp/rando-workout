package com.tjobdev.randoworkout;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class GeneratedRandoWorkoutActivity extends AppCompatActivity {

    // Hashtable to map muscle name to its ID number (for REST API)
    Hashtable<String, Integer> muscleNamesAndIds = new Hashtable<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_random_workout);

        ListView workoutList = (ListView) findViewById(R.id.randomWorkoutList);

        // map muscles to their IDs (API)
        muscleNamesAndIds.put("Biceps", 1); //Biceps brachii (Biceps femoris are 11)
        muscleNamesAndIds.put("Shoulders", 2); //Anterior deltoid
        muscleNamesAndIds.put("Chest", 4); //Pectoralis major
        muscleNamesAndIds.put("Triceps", 5); //Triceps brachii
        muscleNamesAndIds.put("Abdominal", 6); //Rectus abdominis
        muscleNamesAndIds.put("Calves", 7); //Gastrocnemius (soleus is 15)
        muscleNamesAndIds.put("Legs", 8); //Gluteus maximus (quads are 10)
        muscleNamesAndIds.put("Traps", 9); //Trapezius
        muscleNamesAndIds.put("Back", 12); //Latissimus dorsi

        // determine which muscles were picked from WorkoutParameterSelectionActivity
        Intent intent = getIntent();

        String firstMuscle = intent.getStringExtra("firstMuscle");
        String secondMuscle = intent.getStringExtra("secondMuscle");

        Log.i("First Muscle", firstMuscle);
        Log.i("Second Muscle", secondMuscle);

        // generate workout of 3 exercises from firstMuscle and 3 exercises from secondMuscle
        JSONArray randoWorkout = generateWorkout(firstMuscle, secondMuscle);

        // publish workout list to UI list view
        ArrayList<String> items = new ArrayList<String>();
        for(int i =0; i < randoWorkout.length(); i++) {

            JSONObject jsonData = null;

            String exerciseName = "";

            try {
                jsonData = randoWorkout.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                exerciseName = jsonData.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            items.add(exerciseName);

            Log.d(exerciseName, "JSON exercise");

        }

        ArrayAdapter<String> exercisesToListViewAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, items){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);

                return view;

            }

        };

        workoutList.setAdapter(exercisesToListViewAdapter);


    }

    private JSONArray generateWorkout(String firstMuscle, String secondMuscle) {

        JSONArray firstMuscleExerciseList = getMuscleWorkoutList(firstMuscle);
        JSONArray secondMuscleExerciseList = getMuscleWorkoutList(secondMuscle);


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

    private JSONArray getMuscleWorkoutList(String muscle) {

        ExerciseAPIConnection connectionToExerciseAPITask = new ExerciseAPIConnection();

        JSONArray muscleExerciseList = null;

        try {

            // get list of exercises for selected muscle
            muscleExerciseList = connectionToExerciseAPITask.execute("https://wger.de/api/v2/exercise/?muscles=" + muscleNamesAndIds.get(muscle).toString() + "&status=2&format=json&language=2&limit=100").get();

            Log.i(muscle + " Exercise List", muscleExerciseList.toString());

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        return muscleExerciseList;

    }


    public class ExerciseAPIConnection extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {

            String result = "";
            URL exerciseAPIUrl;
            HttpURLConnection exerciseAPIUrlConnection = null;
            JSONArray exerciseList = null;

            try {

                exerciseAPIUrl = new URL(urls[0]);

                exerciseAPIUrlConnection = (HttpURLConnection) exerciseAPIUrl.openConnection();

                InputStream exerciseAPIInputStream = exerciseAPIUrlConnection.getInputStream();

                InputStreamReader exerciseAPIInputStreamReader = new InputStreamReader(exerciseAPIInputStream);

                int exerciseData = exerciseAPIInputStreamReader.read();

                while (exerciseData != -1) {

                    char current = (char) exerciseData;

                    result += current;

                    exerciseData = exerciseAPIInputStreamReader.read();

                }

                // only return JSON Object containing list of exercises and their info
                JSONObject jsonResult = new JSONObject(result);
                exerciseList = jsonResult.getJSONArray("results");

                return exerciseList;

            }
            catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            super.onPostExecute(result);

        }
    }

}
