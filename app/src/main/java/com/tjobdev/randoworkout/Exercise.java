package com.tjobdev.randoworkout;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by obp on 4/19/18.
 */

public class Exercise {

    private String exerciseName;
    //private int[] primaryMuscleExercised;
    //private int[] secondaryMusclesExercised;
    private String exerciseDescription;
    //private int equipment;
    private int category;
    //private ArrayList<Integer> exercisePhotos = new ArrayList<Integer>();

    public Exercise(String          exerciseName,
                    String          exerciseDescription)
    {

        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        //this.category = category;

    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }


}
