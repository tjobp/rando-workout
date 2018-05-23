package com.tjobdev.randoworkout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by obp on 5/21/18.
 */

public class AdapterRandoWorkoutList extends ArrayAdapter<Exercise> {

    private Context randoWorkoutListContext;

    private List<Exercise> randoWorkoutExerciseList = new ArrayList<>();


    public AdapterRandoWorkoutList(@NonNull Context context, @NonNull List<Exercise> objects) {

        super(context, 0, objects);

        randoWorkoutListContext = context;

        randoWorkoutExerciseList = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null) {
            listItem = LayoutInflater.from(randoWorkoutListContext).inflate(R.layout.list_view_exercises, parent, false);
        }

        Exercise currentExercise = randoWorkoutExerciseList.get(position);

        TextView exerciseName = (TextView) listItem.findViewById(R.id.exerciseName);
        exerciseName.setText(currentExercise.getExerciseName());

        TextView sets = (TextView) listItem.findViewById(R.id.setsNumber);
        sets.setText("3");

        TextView reps = (TextView) listItem.findViewById(R.id.repsNumber);
        reps.setText("10");

        return listItem;

    }
}
