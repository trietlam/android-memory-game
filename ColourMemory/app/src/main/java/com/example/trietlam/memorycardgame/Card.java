package com.example.trietlam.memorycardgame;

import android.widget.ImageView;

/**
 * Created by trietlam on 28/9/17.
 */

public class Card {
    public int value;
    public int index;
    public ImageView imageView;

    public Card(ImageView iv, int v) {
        this.imageView = iv;
        this.value = v;
        //this.index = index;
    }

}

