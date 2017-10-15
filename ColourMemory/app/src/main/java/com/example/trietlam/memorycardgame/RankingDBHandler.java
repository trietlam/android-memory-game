package com.example.trietlam.memorycardgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trietlam on 28/9/17.
 */

public class RankingDBHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "rankingDB";

    // table name
    private static final String TABLE_RANKING = "TB_Ranking";

    // Table Columns names
    private static final String KEY_SCORE = "score";
    private static final String KEY_RANK = "rank";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";

    public RankingDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RANKING + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SCORE + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKING);

        // Create tables again
        onCreate(db);
    }

    void addRanking(Ranking r) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, r.getName());
        values.put(KEY_SCORE, r.getScore());

        db.insert(TABLE_RANKING, null, values);
        db.close(); // Closing database connection
    }

    //Replace lowest rank record with new data if list reach max capacity (e.g 20)
    void updateLastRanking(Ranking r){
        Ranking lowRank = getLowestRank();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, r.getName());
        values.put(KEY_SCORE, r.getScore());

        db.update(TABLE_RANKING, values, KEY_ID + "=" + lowRank.getId(), null);
        db.close();
    }

    public List<Ranking> getAllRanking() {
        List<Ranking> rankingList = new ArrayList<Ranking>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RANKING + " ORDER BY " + KEY_SCORE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ranking r = new Ranking();
                r.setId(Integer.parseInt(cursor.getString(0)));
                r.setName(cursor.getString(1));
                r.setScore(Integer.parseInt(cursor.getString(2)));
                rankingList.add(r);
            } while (cursor.moveToNext());
        }

        return rankingList;
    }

    public int getRankingCount(){
        int result = 0;
        String selectQuery = "SELECT COUNT(1)  FROM " + TABLE_RANKING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            result = Integer.parseInt(cursor.getString(0));
        }
        return result;
    }

    public int getLowestScore(){
        int result = 0;
        String selectQuery = "SELECT MIN(score)  FROM " + TABLE_RANKING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            result = Integer.parseInt(cursor.getString(0));
        }
        return result;
    }

    public Ranking getLowestRank(){
        Ranking r = new Ranking();
        String selectQuery = "SELECT * FROM " + TABLE_RANKING +
                " WHERE " + KEY_SCORE + " = (SELECT  MIN(" + KEY_SCORE + ") FROM " + TABLE_RANKING + ") LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            r.setId(Integer.parseInt(cursor.getString(0)));
            r.setName(cursor.getString(1));
            r.setScore(Integer.parseInt(cursor.getString(2)));
        }
        return r;
    }

}
