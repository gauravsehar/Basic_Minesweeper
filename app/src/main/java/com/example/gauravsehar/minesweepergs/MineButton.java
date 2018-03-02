package com.example.gauravsehar.minesweepergs;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.TypedValue;

/**
 * Created by Gaurav Sehar on 02-Feb-18.
 */

public class MineButton extends AppCompatButton {

    private int value;
    private boolean isRevealed = false;
    private boolean isFlagged = false;
    private int rowLocation, columnLocation;
    // TODO: 03-Feb-18 textsize

    public MineButton(Context context) {
        super(context);
        setBackgroundResource(R.drawable.button_unrevealed);
        switch (GameActivity.boardRows){
            case 3:
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 70f);
                break;
            case 6:
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);
                break;
            case 9:
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);
                break;
            default:
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);
        }
//        setText(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
//        if (value == -1)
//            setText(String.valueOf(this.value));
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getRowLocation() {
        return rowLocation;
    }

    public int getColumnLocation() {
        return columnLocation;
    }

    public void setLocation(int rowLocation, int columnLocation) {
        this.rowLocation = rowLocation;
        this.columnLocation = columnLocation;
    }
}
