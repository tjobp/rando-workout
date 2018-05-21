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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_random_workout);

        // base randoWorkoutListView on ArrayList of exercisesItems
        randoWorkoutListView = (ListView) findViewById(R.id.randomWorkoutList);

        //create ArrayAdapter for the randoWorkoutListView and set the text color to white
        ArrayAdapter<String> randoWorkoutListAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, new ArrayList<String>()){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.WHITE);

                return view;

            }

        };

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

    }

    private class TaskToGenerateWorkout extends AsyncTask<String, String, String> {

        ArrayAdapter<String> randoWorkoutListAdapter;

        @Override
        protected void onPreExecute() {

            randoWorkoutListAdapter= (ArrayAdapter<String>)randoWorkoutListView.getAdapter();

            //randoWorkoutListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, randoWorkoutExerciseList);

        }

        @Override
        protected String doInBackground(String... muscle) {

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

                publishProgress(exerciseName);


                secondExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                while(secondExerciseIndex == firstExerciseIndex) {

                    secondExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                }

                secondExercise = firstMuscleExerciseList.getJSONObject(secondExerciseIndex);

                randoWorkout.put(secondExercise);

                exerciseName = secondExercise.getString("name");

                publishProgress(exerciseName);


                thirdExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                while(thirdExerciseIndex == firstExerciseIndex || thirdExerciseIndex == secondExerciseIndex) {

                    thirdExerciseIndex = randomNumber.nextInt(firstMuscleExerciseListSize);

                }

                thirdExercise = firstMuscleExerciseList.getJSONObject(thirdExerciseIndex);

                randoWorkout.put(thirdExercise);

                exerciseName = thirdExercise.getString("name");

                publishProgress(exerciseName);


                return "exercises added";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {

            randoWorkoutListAdapter.add(values[0]);

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
