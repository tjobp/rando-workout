package com.tjobdev.randoworkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class GeneratedRandoWorkoutActivity extends AppCompatActivity {

    // Hashtable to map muscle name to its ID number (for REST API)
    Hashtable<String, Integer> muscleNamesAndIds = new Hashtable<String, Integer>();

    ListView randoWorkoutListView;

    AdapterRandoWorkoutList randoWorkoutListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_random_workout);

        // base randoWorkoutListView on ArrayList of exercisesItems
        randoWorkoutListView = (ListView) findViewById(R.id.randomWorkoutList);

        ArrayList<Exercise> randoWorkoutExerciseList = new ArrayList<>();

        randoWorkoutListAdapter = new AdapterRandoWorkoutList(this, new ArrayList<Exercise>());

        randoWorkoutListView.setAdapter(randoWorkoutListAdapter);

        // determine which muscles were picked from WorkoutParameterSelectionActivity
        Intent intent = getIntent();

        String firstMuscle = intent.getStringExtra("firstMuscle");
        String secondMuscle = intent.getStringExtra("secondMuscle");

        Log.i("First Muscle", firstMuscle);
        Log.i("Second Muscle", secondMuscle);

        new TaskToGenerateWorkout().execute(firstMuscle);

        new TaskToGenerateWorkout().execute(secondMuscle);

        // map muscles to their IDs (for REST API)
        muscleNamesAndIds.put("Biceps", 1); //Biceps brachii (Biceps femoris are 11)
        muscleNamesAndIds.put("Shoulders", 2); //Anterior deltoid
        muscleNamesAndIds.put("Chest", 4); //Pectoralis major
        muscleNamesAndIds.put("Triceps", 5); //Triceps brachii
        muscleNamesAndIds.put("Abdominal", 6); //Rectus abdominis
        muscleNamesAndIds.put("Calves", 7); //Gastrocnemius (soleus is 15)
        muscleNamesAndIds.put("Legs", 8); //Gluteus maximus (quads are 10)
        muscleNamesAndIds.put("Traps", 9); //Trapezius
        muscleNamesAndIds.put("Back", 12); //Latissimus dorsi

        // display dialog when exercise is selected from list view
        randoWorkoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d("lvi clicked", "in hurr");

                Exercise exerciseItemClicked = (Exercise) adapterView.getItemAtPosition(position);

                DialogFragment exerciseInfoFragment = AlertDialogExerciseInfo.newInstance(1, exerciseItemClicked);

                exerciseInfoFragment.show(getFragmentManager(), "exercise" + position);

            }

        });

    }



    private class TaskToGenerateWorkout extends AsyncTask<String, Exercise, String> {

        AdapterRandoWorkoutList randoWorkoutListAdapter;

        @Override
        protected void onPreExecute() {

            /*randoWorkoutListAdapter= (ArrayAdapter<String>)randoWorkoutListView.getAdapter();*/
            randoWorkoutListAdapter = (AdapterRandoWorkoutList)randoWorkoutListView.getAdapter();

            //randoWorkoutListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, randoWorkoutExerciseList);

        }

        @Override
        protected String doInBackground(String... muscle) {

            Exercise randoExerciseOne, randoExerciseTwo, randoExerciseThree;

            // Get all exercises for selected muscles from REST API
            JSONArray firstMuscleExerciseList = getFullExerciseListForMuscle(muscle[0]);

            // Array for final workout list (3 exercises from each muscle)
            JSONArray randoWorkout = new JSONArray();

            JSONObject jsonData = null;
            String exerciseName = "";

            int firstMuscleExerciseListSize = firstMuscleExerciseList.length();

            Random randomNumber = new Random();
            int firstExerciseIndex, secondExerciseIndex, thirdExerciseIndex;

            JSONObject firstExercise, secondExercise, thirdExercise;

            // Randomly select 3 exercises from each exercise list and push each one to the list view on the Main UI thread
            try {

                firstExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                firstExercise = firstMuscleExerciseList.getJSONObject(firstExerciseIndex);

                randoWorkout.put(firstExercise);

                exerciseName = firstExercise.getString("name");

                randoExerciseOne = new Exercise(firstExercise.getString("name"), firstExercise.getString("description"), firstExercise.getInt("category"));

                publishProgress(randoExerciseOne);


                secondExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                while(secondExerciseIndex == firstExerciseIndex) {

                    secondExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                }

                secondExercise = firstMuscleExerciseList.getJSONObject(secondExerciseIndex);

                randoWorkout.put(secondExercise);

                exerciseName = secondExercise.getString("name");

                randoExerciseTwo = new Exercise(secondExercise.getString("name"), secondExercise.getString("description"), secondExercise.getInt("category"));

                publishProgress(randoExerciseTwo);


                thirdExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                while(thirdExerciseIndex == firstExerciseIndex || thirdExerciseIndex == secondExerciseIndex) {

                    thirdExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                }

                thirdExercise = firstMuscleExerciseList.getJSONObject(thirdExerciseIndex);

                randoWorkout.put(thirdExercise);

                exerciseName = thirdExercise.getString("name");

                randoExerciseThree = new Exercise(thirdExercise.getString("name"), thirdExercise.getString("description"), thirdExercise.getInt("category"));

                publishProgress(randoExerciseThree);


                return "exercises added";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;

        }

        @Override
        protected void onProgressUpdate(Exercise... exercise) {

            Log.i("Got to progress", exercise[0].getExerciseName());
            Log.i("Description", exercise[0].getExerciseDescription());

            randoWorkoutListAdapter.add(exercise[0]);

            randoWorkoutListAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        }

        protected JSONArray getFullExerciseListForMuscle(String muscle) {

            URL exerciseApiUrl;
            HttpURLConnection exerciseApiUrlConnection;
            JSONArray fullMuscleExerciseList;
            BufferedReader exerciseApiBufferReader;

            try {

                exerciseApiUrl = new URL("https://wger.de/api/v2/exercise/?muscles=" + muscleNamesAndIds.get(muscle) + "&status=2&format=json&language=2&limit=100");

                Log.i("Rest API URL", "https://wger.de/api/v2/exercise/?muscles=" + muscleNamesAndIds.get(muscle) + "&status=2&format=json&language=2&limit=100");

                exerciseApiUrlConnection = (HttpURLConnection) exerciseApiUrl.openConnection();

                exerciseApiUrlConnection.connect();

                InputStream exerciseAPIInputStream = exerciseApiUrlConnection.getInputStream();

                exerciseApiBufferReader = new BufferedReader(new InputStreamReader(exerciseAPIInputStream));

                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = exerciseApiBufferReader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                // only return JSON Object containing list of exercises and their info - gets rid of other unnecessary JSON data
                JSONObject jsonResult = new JSONObject(buffer.toString());
                fullMuscleExerciseList = jsonResult.getJSONArray("results");

                Log.i("Full Exercise List, " + muscle, fullMuscleExerciseList.toString());

                return fullMuscleExerciseList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

    }

}
