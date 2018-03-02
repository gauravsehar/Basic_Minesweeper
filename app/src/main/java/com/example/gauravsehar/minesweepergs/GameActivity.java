package com.example.gauravsehar.minesweepergs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    String playerName;
    int playerAge;
    static int boardRows, boardColumns, noOfMines, playerScore = 0, layoutInt,
            rowNeighbours[] = {-1, -1, -1, 0, 0, 1, 1, 1},
            columnNeighbours[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    boolean isGameOver;
    LinearLayout parentLayout;
    MineButton buttonArray[][], button;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        playerName = intent.getStringExtra(StartActivity.NAME_KEY);
        playerAge = intent.getIntExtra(StartActivity.AGE_KEY, 0);
        noOfMines = intent.getIntExtra(StartActivity.MINES_KEY, 1);
        layoutInt = intent.getIntExtra(StartActivity.LAYOUT_KEY, 0);
        setLayoutPredefined(layoutInt);
//        Toast.makeText(this, playerName, Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        parentLayout = findViewById(R.id.parentLayout);
        initializeBoard();
    }

    private void setLayoutPredefined(int num) {
        switch (num) {
            case 0:
                boardRows = 3;
                boardColumns = 5;
                break;
            case 1:
                boardRows = 6;
                boardColumns = 10;
                break;
            case 2:
                boardRows = 9;
                boardColumns = 15;
                break;
            default:
                boardRows = 3;
                boardColumns = 5;
        }
    }

    private void initializeBoard() {
        sharedPreferences = getSharedPreferences("GS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", playerName);
        editor.putString("UserAge", String.valueOf(playerAge));
        editor.apply();
        Toast.makeText(this, "Player Age: " + String.valueOf(playerAge), Toast.LENGTH_SHORT).show();
        isGameOver = false;
        playerScore = 0;
        buttonArray = new MineButton[boardRows][boardColumns];
        setupBoard();
        minePlanter(noOfMines);
        neighbourSetter();
    }

    private void setupBoard() {
        parentLayout.removeAllViews();
        for (int i = 0; i < boardColumns; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            rowLayout.setLayoutParams(layoutParams);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < boardRows; j++) {
                button = new MineButton(this);
                LinearLayout.LayoutParams buttonParams =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                button.setLayoutParams(buttonParams);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                rowLayout.addView(button);
                button.setLocation(j, i);
                buttonArray[j][i] = button;
            }
            parentLayout.addView(rowLayout);
        }
    }

    @Override
    public void onClick(View v) {
        button = (MineButton) v;
        if (!button.isRevealed() && !button.isFlagged() && !isGameOver) {
            if (button.getValue() > 0) {
                button.setText(String.valueOf(button.getValue()));
                button.setBackgroundResource(R.drawable.button_revealed);
            } else if (button.getValue() == -1 && !isGameOver) {
                button.setBackgroundResource(R.drawable.button_blast);
                isGameOver = true;
                Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
                showMines(false);
            } else if (button.getValue() == 0 && !isGameOver) {
                rippleCaller(button);
            }
            button.setRevealed(true);
            button.setOnClickListener(null);
        }
        updateGameScore();
    }

    @Override
    public boolean onLongClick(View v) {
        button = (MineButton) v;
        if (!button.isRevealed() && !button.isFlagged() && !isGameOver) {
            button.setFlagged(true);
            button.setBackgroundResource(R.drawable.button_flagged);
        } else if (!button.isRevealed() && button.isFlagged()) {
            button.setFlagged(false);
            button.setBackgroundResource(R.drawable.button_unrevealed);
        }
        return true;
    }

    public void minePlanter(int noOfMines) {
        for (int i = 0; i < noOfMines; i++) {
            button = buttonArray[randomNoGenerator(0, boardRows)][randomNoGenerator(0, boardColumns)];
            if (button.getValue() != -1) {
                button.setValue(-1);
            } else {
                i--;
            }
        }
    }

    private void neighbourSetter() {
        for (int i = 0; i < boardRows; i++)
            for (int j = 0; j < boardColumns; j++) {
                button = buttonArray[i][j];
                if (button.getValue() == -1) for (int k = 0; k < 8; k++) {
                    if (i + rowNeighbours[k] >= 0 && (j + columnNeighbours[k] >= 0)) {
                        if (i + rowNeighbours[k] < boardRows && j + columnNeighbours[k] < boardColumns) {
                            MineButton buttonTemp = buttonArray[i + rowNeighbours[k]][j + columnNeighbours[k]];
                            if (buttonTemp.getValue() != -1) {
                                button = buttonArray[i + rowNeighbours[k]][j + columnNeighbours[k]];
                                button.setValue(button.getValue() + 1);
                            }
                        }
                    }
                }
            }
    }

    private void updateGameScore() {
        int score = 0;
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardColumns; j++) {
                if (buttonArray[i][j].getValue() >= 0 && buttonArray[i][j].isRevealed() && !buttonArray[i][j].isFlagged()) {
                    score += buttonArray[i][j].getValue();
                }
            }
        }
        playerScore = score;
        checkGameOver();
    }

    public void checkGameOver() {
        int check = 0;
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardColumns; j++) {
                if (buttonArray[i][j].getValue() >= 0) {
                    check += buttonArray[i][j].getValue();
                }
            }
        }
        if (playerScore == check) {
            Toast.makeText(this, "You Won with Score: " + playerScore, Toast.LENGTH_SHORT).show();
            isGameOver = true;
            showMines(true);
        }
    }

    private void rippleCaller(MineButton button) {
        rippleEffect(button);
        updateGameScore();
    }

    private void rippleEffect(MineButton button) {
        button.setRevealed(true);
        button.setOnClickListener(null);
        button.setBackgroundResource(R.drawable.button_revealed);
        if (button.getValue() != 0) button.setText(String.valueOf(button.getValue()));
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if ((button.getRowLocation() + i >= 0) && (button.getColumnLocation() + j >= 0)) {
                    if ((button.getRowLocation() + i < boardRows)
                            && (button.getColumnLocation() + j < boardColumns)) {
                        if (button.getValue() == 0) {
                            if (!buttonArray[button.getRowLocation() + i][button.getColumnLocation() + j].isRevealed()
                                    && !buttonArray[button.getRowLocation() + i][button.getColumnLocation() + j].isFlagged())
                                rippleEffect(buttonArray[button.getRowLocation() + i][button.getColumnLocation() + j]);
                        } else return;
                    }
                }
    }

    private void showMines(boolean won) {
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardColumns; j++) {
                button = buttonArray[i][j];
                if (button.getValue() == -1) {
                    if (won)
                        button.setBackgroundResource(R.drawable.button_mine);
                    else
                        button.setBackgroundResource(R.drawable.button_blast);
                }


            }
        }
    }

    private int randomNoGenerator(int min, int max) {
        Random r = new Random();
        int random = r.nextInt(max) + min;
        return random;
    }

    public void callStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra(StartActivity.NAME_KEY, playerName);
        intent.putExtra(StartActivity.AGE_KEY, playerAge);
        intent.putExtra(StartActivity.LAYOUT_KEY, layoutInt);
        intent.putExtra(StartActivity.MINES_KEY, noOfMines);
        intent.putExtra(StartActivity.EDITOR_KEY, true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            playerName = data.getStringExtra(StartActivity.NAME_KEY);
            playerAge = data.getIntExtra(StartActivity.AGE_KEY, 0);
            noOfMines = data.getIntExtra(StartActivity.MINES_KEY, 1);
            layoutInt = data.getIntExtra(StartActivity.LAYOUT_KEY, 0);
            setLayoutPredefined(layoutInt);
        }
        initializeBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);

//        menuItem.setTitle(playerName);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        MenuItem menuItem = item.findItem(R.id.playerNameDisplay);
        int id = item.getItemId();
        switch (id) {
            case R.id.reset:
                initializeBoard();
                break;
            case R.id.easy:
                item.setChecked(true);
                boardRows = 3;
                boardColumns = 5;
                noOfMines = 2;
                initializeBoard();
                break;
            case R.id.medium:
                item.setChecked(true);
                boardRows = 6;
                boardColumns = 10;
                noOfMines = 8;
                initializeBoard();
                break;
            case R.id.expert:
                item.setChecked(true);
                boardRows = 9;
                boardColumns = 15;
                noOfMines = 15;
                initializeBoard();
                break;
            case R.id.custom:
                item.setChecked(true);
                callStartActivity();
                initializeBoard();
                break;
            default:
//                initializeBoard();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to Exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null).show();
    }
}