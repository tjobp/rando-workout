
package com.tjobdev.randoworkout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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


public class ActivityWorkoutParametersSelection extends AppCompatActivity
{

    // Spinner variables for muscle spinners
    Spinner   topMuscleSpinner;
    Spinner   bottomMuscleSpinner;

    // Populate exercise database
    JSONArray allExercises = null;


    @Override protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_workout_parameters_selection );

        // populate the exercise database
        // new GetJsonOfExercises().execute();

        // Instantiate spinners
        topMuscleSpinner = (Spinner) findViewById( R.id.topMuscleSpinner );
        bottomMuscleSpinner = (Spinner) findViewById( R.id.bottomMuscleSpinner );

        // Create list of muscle group options to select from muscle spinners
        ArrayList<String> muscleGroups = new ArrayList<>();
        muscleGroups.add( "Abdominal" );
        muscleGroups.add( "Back" );
        muscleGroups.add( "Biceps" );
        muscleGroups.add( "Calves" );
        muscleGroups.add( "Chest" );
        muscleGroups.add( "Legs" );
        muscleGroups.add( "Triceps" );
        muscleGroups.add( "Traps" );
        muscleGroups.add( "Shoulders" );

        // Connect array list to spinners with adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( getApplicationContext(), R.layout.top_spinner_style, muscleGroups );

        arrayAdapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item );

        topMuscleSpinner.setAdapter( arrayAdapter );

        // TODO: Use separate array adapter for second spinner and change muscles in
        //   second array adapter based off of selection of first spinner
        bottomMuscleSpinner.setAdapter( arrayAdapter );

        // connect to Exercise Database that is in the assets folder
        DatabaseHelper exerciseDatabaseHelper = DatabaseHelper.getInstance( getApplicationContext() );

        try
        {

            // create the exercise database if it is not already created
            exerciseDatabaseHelper.createDatabase();

        }
        catch( IOException e )
        {

            throw new Error( "Unable to create database" );

        }

    }


    // This function is called when the generateWorkoutButton is clicked
    public void generateWorkoutButtonClick( View v )
    {

        String firstMuscle = topMuscleSpinner.getSelectedItem().toString();
        String secondMuscle = bottomMuscleSpinner.getSelectedItem().toString();

        goToGeneratedRandoWorkoutActivity( firstMuscle, secondMuscle );

    }


    // This function puts selected muscles into intent and changes activity to
    // 'ActivityGeneratedRandoWorkout'
    public void goToGeneratedRandoWorkoutActivity( String firstMuscle, String secondMuscle )
    {

        Intent goToRandoWorkoutActivityIntent = new Intent( getApplicationContext(), ActivityGeneratedRandoWorkout.class );

        goToRandoWorkoutActivityIntent.putExtra( "firstMuscle", firstMuscle );

        goToRandoWorkoutActivityIntent.putExtra( "secondMuscle", secondMuscle );

        startActivity( goToRandoWorkoutActivityIntent );

    }


    // asynch task to populate database
    private class GetJsonOfExercises extends AsyncTask<String, String, JSONArray>
    {

        @Override protected JSONArray doInBackground( String... strings )
        {

            // temporary load of all muscle exercises to database
            URL popDbUrl;
            HttpURLConnection popDbUrlConnection;
            JSONArray popDbExerciseList = null;
            BufferedReader popDbBufferReader;

            try
            {

                popDbUrl = new URL( "https://wger.de/api/v2/exercise/?status=2&format=json&language=2&limit=200" );

                Log.i( "Pop DB URL", "https://wger.de/api/v2/exercise/?status=2&format=json&language=2&limit=200" );

                popDbUrlConnection = (HttpURLConnection) popDbUrl.openConnection();

                popDbUrlConnection.connect();

                InputStream exerciseAPIInputStream = popDbUrlConnection.getInputStream();

                popDbBufferReader = new BufferedReader( new InputStreamReader( exerciseAPIInputStream ) );

                StringBuffer buffer = new StringBuffer();
                String line;

                while( ( line = popDbBufferReader.readLine() ) != null )
                {

                    buffer.append( line + "\n" );

                }

                // only return JSON Object containing list of exercises and their info - gets
                // rid of other unnecessary JSON data
                JSONObject jsonResult = new JSONObject( buffer.toString() );
                popDbExerciseList = jsonResult.getJSONArray( "results" );

                Log.i( "Full Exercise List", popDbExerciseList.toString() );

                allExercises = popDbExerciseList;

                // populate the database
                /******************************************************************************************/
                SQLiteDatabase exerciseDB = openOrCreateDatabase( "Events", MODE_PRIVATE, null );

                // Delete the database "Events"
                // getApplicationContext().deleteDatabase("Events");

                exerciseDB.execSQL( "CREATE TABLE IF NOT EXISTS exercises (" +
                                    "name VARCHAR, " +
                                    "description VARCHAR, " +
                                    "primaryMuscles VARCHAR, " +
                                    "secondaryMuscles VARCHAR, " +
                                    "equipment VARCHAR)" );

                for ( int i = 0; i < allExercises.length(); i++ )
                {

                    try
                    {

                        Log.i( "Exercise Description", allExercises.getJSONObject( i ).getString( "description" ) );
                        String exerciseDescription = allExercises.getJSONObject( i ).getString( "description" );
                        exerciseDescription = exerciseDescription.replaceAll( "(<p>|</p>|<ul>|</ul>|<li>|</li>|<ol>|</ol>|<em>|</em>|')", "" );
                        Log.i( "2nd ex desc", exerciseDescription );
                        Log.i( "Exercise Name", allExercises.getJSONObject( i ).getString( "name" ) );
                        String exerciseName = allExercises.getJSONObject( i ).getString( "name" );
                        Log.i( "Exercise Category", allExercises.getJSONObject( i ).getString( "category" ) );
                        Log.i( "Exercise Muscles", String.valueOf( allExercises.getJSONObject( i ).getJSONArray( "muscles" ) ) );
                        String exercisePrimaryMuscles = String.valueOf( allExercises.getJSONObject( i ).getJSONArray( "muscles" ) );
                        Log.i( "Exercise Muscles Sec", String.valueOf( allExercises.getJSONObject( i ).getJSONArray( "muscles_secondary" ) ) );
                        String exerciseSecondaryMuscles = String.valueOf( allExercises.getJSONObject( i ).getJSONArray( "muscles_secondary" ) );
                        Log.i( "Exercise Equipment", String.valueOf( allExercises.getJSONObject( i ).getJSONArray( "equipment" ) ) );
                        String exerciseEquipment = String.valueOf( allExercises.getJSONObject( i ).getJSONArray( "equipment" ) );

                        /*
                         * ContentValues insertValues = new ContentValues(); insertValues.put("name",
                         * exerciseName); insertValues.put("description", exerciseDescription);
                         * insertValues.put("primaryMuscles", exercisePrimaryMuscles);
                         * insertValues.put("secondaryMuscles", exerciseSecondaryMuscles);
                         * insertValues.put("equipment", exerciseEquipment);
                         * exerciseDB.insert("exercises", null, insertValues);
                         */

                        // insert values into exerciseDB
                        exerciseDB.execSQL( "INSERT INTO exercises ( name, description, primaryMuscles, secondaryMuscles, equipment )" +
                                            " VALUES ( '" +
                                            exerciseName +
                                            "' , '" +
                                            exerciseDescription +
                                            "' , '" +
                                            exercisePrimaryMuscles +
                                            "' , '" +
                                            exerciseSecondaryMuscles +
                                            "' , '" +
                                            exerciseEquipment +
                                            "' )" );

                        // exerciseDB.execSQL("INSERT INTO exercises ( name ) VALUES ( '" + exerciseName
                        // + "' )");
                        // + " VALUES ( " + exerciseName + " , " + exerciseDescription + " , " +
                        // exercisePrimaryMuscles + " , " + exerciseSecondaryMuscles + " , " +
                        // exerciseEquipment + " )");

                        Log.i( "Past INSERT", "successfully" );

                    }
                    catch( JSONException e )
                    {

                        Toast.makeText( ActivityWorkoutParametersSelection.this, "Error " + e.toString(), Toast.LENGTH_LONG ).show();

                        e.printStackTrace();

                    }

                    // query the database and display each entry
                    Cursor eventsDbCursor = exerciseDB.rawQuery( "Select * from exercises", null );
                    eventsDbCursor.moveToFirst();
                    String name = eventsDbCursor.getString( 0 );
                    String description = eventsDbCursor.getString( 1 );
                    String primaryMuscle = eventsDbCursor.getString( 2 );
                    String secondaryMuscles = eventsDbCursor.getString( 3 );
                    String equipment = eventsDbCursor.getString( 4 );

                    Log.d( "db ex name", name );
                    Log.d( "db ex desc", description );
                    Log.d( "db ex primaryMuscle", primaryMuscle );
                    Log.d( "db ex secondaryMuscle", secondaryMuscles );
                    Log.d( "db ex equipment", equipment );

                }

                // end of populating database
                /******************************************************************************************/

            }
            catch( MalformedURLException e )
            {
                e.printStackTrace();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
            catch( JSONException e )
            {
                e.printStackTrace();
            }

            return popDbExerciseList;
        }
    }

}
