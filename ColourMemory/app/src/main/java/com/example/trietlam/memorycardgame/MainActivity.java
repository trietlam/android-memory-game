package com.example.trietlam.memorycardgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements EndGameDialogFragment.EndGameDialogListener {
    private static int BOARD_SIZE = 4;
    private static int PAIR_COUNT = BOARD_SIZE * BOARD_SIZE / 2;

    int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    int[] imgSource = new int[BOARD_SIZE * BOARD_SIZE / 2];
    int score = 0;
    int pairMatched = 0;
    TextView tv_score;

    Card firstCard, secondCard;

    //
    @Override
    public void onFinishUserDialog() {
        endGameProcess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_score = (TextView) findViewById(R.id.tv_score);
        loadImage();
        initGame();
    }

    private void initGame() {
        pairMatched = 0;
        score = 0;
        shuffleArray();

        LinearLayout parentView = (LinearLayout) findViewById(R.id.main_layout);

        int count = parentView.getChildCount();
        Log.d("childCount", Integer.toString(count));
        int index = 0;

        //use tag to store card value
        for (int i = 0; i < BOARD_SIZE; i++) {
            LinearLayout rowView = (LinearLayout) parentView.getChildAt(i);
            for (int j = 0; j < BOARD_SIZE; j++) {
                ImageView iv = (ImageView) rowView.getChildAt(j);
                iv.setTag(arr[index]);
                index++;
            }
        }
    }

    private void loadImage() {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE / 2 ; i++) {
            int id = getResources().getIdentifier("colour" + i, "drawable", getPackageName());
            imgSource[i] = id;
        }
    }


    //Simple array shuffling instead of using Collection
    private void shuffleArray() {
        Random r = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int index = r.nextInt(i + 1);
            int a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
        for (int i = 0; i < arr.length; i++) {
            Log.d("array value", Integer.toString(arr[i]));
        }
    }

    private void calculate() {
        boolean check = false;

        //Check if 2 cards having same image
        if (firstCard.value % PAIR_COUNT == secondCard.value % PAIR_COUNT) {
            score = score + 2;
            pairMatched++;
            check = true;
        } else {
            score = score - 1;
        }

        if (check) {
            firstCard.imageView.setVisibility(View.INVISIBLE);
            secondCard.imageView.setVisibility(View.INVISIBLE);
        } else {
            firstCard.imageView.setImageResource(R.drawable.card_bg);
            secondCard.imageView.setImageResource(R.drawable.card_bg);
        }

        tv_score.setText(getString(R.string.yourscore) + ": " + Integer.toString(score));
        //reset
        firstCard = null;
        secondCard = null;

        //Game Ended
        if (pairMatched == PAIR_COUNT){
            EndGameDialogFragment dialog = EndGameDialogFragment.newInstance(score);
            dialog.show(getFragmentManager(), "EndGame");
        }
    }

    private void endGameProcess(){
        resetBoard();
        initGame();
    }

    private boolean isTop20(){

        return true;
    }
    private void resetBoard(){
        LinearLayout parentView = (LinearLayout) findViewById(R.id.main_layout);
        tv_score.setText("score");

        for (int i = 0; i < BOARD_SIZE; i++) {
            LinearLayout rowView = (LinearLayout) parentView.getChildAt(i);
            for (int j = 0; j < BOARD_SIZE; j++) {
                ImageView iv = (ImageView) rowView.getChildAt(j);
                iv.setImageResource(R.drawable.card_bg) ;
                iv.setVisibility(View.VISIBLE);
            }
        }
    }

    //region UI Event Handler
    public void onCardClick(View v) {
        // does something very interesting
        int tag = (Integer) v.getTag();

        if (firstCard == null) {
            firstCard = new Card((ImageView) v, tag);
            ((ImageView) v).setImageResource(imgSource[tag%PAIR_COUNT]);
            return; //don't need to calculate if only 1 card is turned
        } else {
            if (firstCard.value == tag) {
                return; //Do nothing if same card selected twice
            } else if (secondCard == null) {
                secondCard = new Card((ImageView) v, tag);
                ((ImageView) v).setImageResource(imgSource[tag%PAIR_COUNT]);
            } else {
                //do nothing for any other exeption case
                return;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calculate();
            }
        }, 1000);
    }

    public void highScoreButtonClicked(View view) {
        Intent intent = new Intent(this, HighScoreActivity.class);

        startActivity(intent);

    }

    //endregion

}
