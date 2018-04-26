package com.tjobdev.randoworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class GeneratedRandoWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_random_workout);

        Bundle bundle = getIntent().getExtras();

        String exerciseListArray = bundle.getString("exerciseList");

        Log.i("extras", exerciseListArray);

        JSONArray exerciseList = null;

        try {
            exerciseList = new JSONArray(exerciseListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView randomWorkoutList = (ListView) findViewById(R.id.randomWorkoutList);


    }
}
