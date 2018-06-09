package com.tjobdev.randoworkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by obp on 5/27/18.
 */

public class AlertDialogExerciseInfo extends DialogFragment {

    private Exercise exerciseWithInfo;

    public static AlertDialogExerciseInfo newInstance(int arg, Exercise exerciseWithInfo) {

        AlertDialogExerciseInfo alertDialogExerciseInfo = new AlertDialogExerciseInfo();
        Bundle args = new Bundle();
        args.putInt("count", arg);
        alertDialogExerciseInfo.setArguments(args);
        alertDialogExerciseInfo.setExercise(exerciseWithInfo);
        return alertDialogExerciseInfo;

    }

    public void setExercise(Exercise exerciseWithInfo) {

        this.exerciseWithInfo = exerciseWithInfo;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        String exerciseDescription = exerciseWithInfo.getExerciseDescription().replaceAll("(<p>|</p>)", "");

        // get the layout inflater
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View dialogView = layoutInflater.inflate(R.layout.dialog_exercise_info, null);

        // inflate and set layout for the dialog
        // pass null as the parent view because its going in the dialog layout
        alertDialogBuilder  .setView(dialogView);

        TextView exerciseTitleTextView = (TextView) dialogView.findViewById(R.id.exerciseName);
        exerciseTitleTextView.setText(exerciseWithInfo.getExerciseName());

        TextView exerciseDescriptionTextView = (TextView) dialogView.findViewById(R.id.exerciseDescription);
        exerciseDescriptionTextView.setText(exerciseDescription);

        Button gotItButton = (Button) dialogView.findViewById(R.id.gotItButton);
        gotItButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        setRetainInstance(true);

        return alertDialogBuilder.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // delete background of alert default so it looks like corners are rounded
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
