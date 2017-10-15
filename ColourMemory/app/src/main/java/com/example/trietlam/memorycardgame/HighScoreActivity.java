package com.example.trietlam.memorycardgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        TableLayout tableLayout =(TableLayout)findViewById(R.id.table_high_score);


        RankingDBHandler db = new RankingDBHandler(this);
        List<Ranking> rankingList = db.getAllRanking();

        int count = rankingList.size();
        for (int i = 0;i<count;i++) {
            Ranking r = rankingList.get(i);
            View tableRow = LayoutInflater.from(this).inflate(R.layout.score_row_template, null);

            String log = "id: " + r.getId() + " ,Name: " + r.getName() + " ,Score: " + r.getScore();
            // Writing Contacts to log
            Log.d("Name: ", log);

            TextView tv_rank  = (TextView) tableRow.findViewById(R.id.tv_hs_rank);
            TextView tv_name  = (TextView) tableRow.findViewById(R.id.tv_hs_name);
            TextView tv_score  = (TextView) tableRow.findViewById(R.id.tv_hs_score);

            tv_rank.setText(Integer.toString(i+1));
            tv_name.setText(r.getName());
            tv_score.setText(Integer.toString(r.getScore()));
            tableLayout.addView(tableRow);
        }
    }

    public void backButtonClicked(View view) {
        this.onBackPressed();
    }
}
