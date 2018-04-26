package com.tjobdev.randoworkout;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to connect to the Exercise Database API
 * Created by obp on 4/17/18.
 */

public class APIConneciton extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL exerciseAPIUrl;
        HttpURLConnection exerciseAPIUrlConnection = null;

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


            return result;

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}
