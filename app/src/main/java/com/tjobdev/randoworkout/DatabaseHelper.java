package com.tjobdev.randoworkout;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by obp on 7/16/18.
 *
 * Code from: https://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Declare database helper as a static instance variable and use the singelton pattern to guarantee the singleton property
    private static DatabaseHelper dbInstance;

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.tjobdev.randoworkout/databases/";

    private static String DB_NAME = "Events";

    private SQLiteDatabase myDatabase;

    private final Context myContext;

    // used to keep only one database instance in the app - use this instead of constructor
    public static synchronized DatabaseHelper getInstance(Context context) {

        if (dbInstance == null) {

            dbInstance = new DatabaseHelper(context.getApplicationContext());

        }

        return dbInstance;
    }

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    private DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);

        this.myContext = context;

        //default when implemented this constructor
        //super(context, name, factory, version);
    }

    /**
     * Creates a empty database on the system and rewrites it with the Events database from the assets folder.
     **/
    public void createDatabase() throws IOException{

        boolean dbExist = checkDatabase();

        if(dbExist){
            Log.i("createDatabase()", "database exists");
            //do nothing - database already exist
        }else{

            //By calling this method an empty database will be created into the default system path
            //of application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                Log.i("createDatabase()", "database copied");
                copyDatabase();

            } catch (IOException e) {

                throw new Error("Error copying database createDatabase()");

            }
        }

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        boolean dbExists = checkDatabase();

        if (dbExists) {

            Log.i("onCreate()", "database exists");
            // do nothing - database already exists

        } else {

            // By calling this method an empty database will be created into the default system path
            // of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                Log.i("onCreate()", "database copied");
                copyDatabase();

            } catch (IOException e) {

                throw new Error("Error copying database in onCreate()");

            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public synchronized void close() {

        if (myDatabase != null) {

            myDatabase.close();

        }

        //super.close();
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.
            Log.e("SQLiteException", "database doesn't exist yet");

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDatabase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDatabase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    public List<Exercise> getExercises(int selectedMuscle) {

        List<Exercise> muscleExerciseList = new ArrayList<Exercise>();

        SQLiteDatabase exerciseDatabase = this.getReadableDatabase();

        String muscleSubstring = "'%"+selectedMuscle+"%'";

        // query for only exercises that involve chest muscle
        Cursor exerciseDbCursor = exerciseDatabase.rawQuery("Select * from exercises WHERE primaryMuscles LIKE " + muscleSubstring +"", null);

        try {
            while (exerciseDbCursor.moveToNext()) {

                String name = exerciseDbCursor.getString(0);
                String description = exerciseDbCursor.getString(1);
                String primaryMuscle = exerciseDbCursor.getString(2);
                String secondaryMuscles = exerciseDbCursor.getString(3);
                String equipment = exerciseDbCursor.getString(4);

                /*
                Log.d("db ex name", name);
                Log.d("db ex desc", description);
                Log.d("db ex primaryMuscle", primaryMuscle);
                Log.d("db ex secondaryMuscle", secondaryMuscles);
                Log.d("db ex equipment", equipment);*/

                Exercise exerciseToAddToList = new Exercise(name, description);
                muscleExerciseList.add(exerciseToAddToList);

            }
        } finally {
            exerciseDbCursor.close();
        }

        return muscleExerciseList;

    }

}
