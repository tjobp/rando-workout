package com.tjobdev.randoworkout;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to connect to the Exercise Database API
 * Created by obp on 4/17/18.
 */

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

    }
}
