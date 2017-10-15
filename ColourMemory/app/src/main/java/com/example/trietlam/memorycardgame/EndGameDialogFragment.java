package com.example.trietlam.memorycardgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by trietlam on 28/9/17.
 */

public class EndGameDialogFragment extends DialogFragment {
    private Button positiveButton;
    public interface EndGameDialogListener {
        public void onFinishUserDialog();
    }

    EndGameDialogListener mListener;
    EditText tv_username;

    static EndGameDialogFragment newInstance(int score) {
        EndGameDialogFragment f = new EndGameDialogFragment();

        Bundle args = new Bundle();
        args.putInt("score", score);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onStart(){
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if (d!=null){
            positiveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
        }
    }

    //endregion
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int score = getArguments().getInt("score");
        AlertDialog dialog;

        if (isInTopRank(score)){
            dialog = initEndGameDialogWithSaveScore(score);
        } else {
            dialog = initEndGameDialogNoSaveScore(score);
        }
        return dialog;
    }

    private AlertDialog initEndGameDialogWithSaveScore(int i_score){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Holo_Dialog_MinWidth);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final int score = i_score;
        View view = inflater.inflate(R.layout.fragment_user_input, null);
        tv_username = (EditText) view.findViewById(R.id.username);

        builder.setTitle("CONGRATULATION!!!")
                .setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveRanking(tv_username.getText().toString(), score);
                        EndGameDialogListener activity = (EndGameDialogListener) getActivity();
                        activity.onFinishUserDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EndGameDialogListener activity = (EndGameDialogListener) getActivity();
                        activity.onFinishUserDialog();
                    }
                });
        final AlertDialog dialog = builder.create();

        tv_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
        return dialog;
    }

    private AlertDialog initEndGameDialogNoSaveScore(int score){
        //Not qualify for High Score, just pop up to show final score
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Holo_Dialog_MinWidth);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Your score is: " + Integer.toString(score));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EndGameDialogListener activity = (EndGameDialogListener) getActivity();
                activity.onFinishUserDialog();
            }
        });
        return builder.create();
    }

    private void saveRanking(String name, int score){
        RankingDBHandler rankingDB = new RankingDBHandler(getActivity());
        int rankingListSize = rankingDB.getRankingCount();
        if (rankingListSize < 10){
            rankingDB.addRanking(new Ranking(name, score));
        } else {
            Ranking r = rankingDB.getLowestRank();
            rankingDB.updateLastRanking(new Ranking(name, score));
        }
    }

    private boolean isInTopRank(int score){
        RankingDBHandler rankingDB = new RankingDBHandler(getActivity());
        int rankingListSize = rankingDB.getRankingCount();
        if (rankingListSize < 10){
            return true;
        }

        int lowestScore = rankingDB.getLowestScore();
        if (lowestScore < score){
            return true;
        }
        return false;
    }

}
