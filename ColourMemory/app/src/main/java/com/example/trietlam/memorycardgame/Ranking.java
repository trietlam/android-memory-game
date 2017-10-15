package com.example.trietlam.memorycardgame;

/**
 * Created by trietlam on 28/9/17.
 */

public class Ranking {
    //private variables
    int _id;
    String _name;
    int _score;

    // Empty constructor
    public Ranking(){

    }

    // constructor
    public Ranking(String name, int score){
        this._name = name;
        this._score = score;
    }
    // getting Score
    public int getScore(){
        return this._score;
    }

    // setting Score
    public void setScore(int value){
        this._score = value;
    }

    // getting ID
    public int getId(){
        return this._id;
    }

    // setting id
    public void setId(int value){
        this._id = value;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }
}
