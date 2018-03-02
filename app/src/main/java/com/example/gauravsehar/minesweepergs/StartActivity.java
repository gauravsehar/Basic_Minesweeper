package com.example.gauravsehar.minesweepergs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    EditText nameTV, ageTV;
    TextView layoutSizeTV, noOfMinesTV;
    String name;
    int age, layoutSize = 0, noOfMines = 3;
    public static final String NAME_KEY = "name", AGE_KEY = "age", LAYOUT_KEY = "layout", MINES_KEY = "mines", EDITOR_KEY = "editor mode";
    SeekBar layoutSizeSeeker, noOfMinesSeeker;
    SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sharedPreferences = getSharedPreferences("GS", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isFirstStart", true)) {
            nameTV.setText(sharedPreferences.getString("UserName", ""));
            ageTV.setText(sharedPreferences.getString("UserAge", "0"));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstStart", false);
            editor.apply();
            Toast.makeText(this, "Nice to See You Again !!!", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();

        nameTV = findViewById(R.id.playerName);
        ageTV = findViewById(R.id.playerAge);
        layoutSizeSeeker = findViewById(R.id.layoutSizeSeeker);
        noOfMinesSeeker = findViewById(R.id.noOfMinesSeeker);
        layoutSizeTV = findViewById(R.id.layoutSizeTV);
        noOfMinesTV = findViewById(R.id.noOfMinesTV);

        ageTV.addTextChangedListener(new checkAge());
        layoutSizeSeeker.setOnSeekBarChangeListener(new checkSize());
        noOfMinesSeeker.setOnSeekBarChangeListener(new checkUpdate());

        intent = getIntent();
        if (intent.getBooleanExtra(EDITOR_KEY, false)) {
            nameTV.setText(intent.getStringExtra(StartActivity.NAME_KEY));
            ageTV.setText(String.valueOf(intent.getIntExtra(StartActivity.AGE_KEY, 0)));
            layoutSizeSeeker.setProgress(intent.getIntExtra(StartActivity.LAYOUT_KEY, 0));
            noOfMinesSeeker.setProgress(intent.getIntExtra(StartActivity.MINES_KEY, 1) - 1);
        }
//        Log.d("GSDebug", "onCreate: "+intent);


    }

    public void StartButtonClick(View view) {

        if (!nameTV.getText().toString().isEmpty() || !ageTV.getText().toString().isEmpty()) {
            if (!nameTV.getText().toString().isEmpty()) name = nameTV.getText().toString();
            else Toast.makeText(this, "Enter Your Name.", Toast.LENGTH_SHORT).show();
            try {
                age = Integer.parseInt(ageTV.getText().toString());
            } catch (Exception e) {
//            Log.e("GS", "StartActivity StartButtonClick :" + e);
                Toast.makeText(this, "Enter Your Age.", Toast.LENGTH_SHORT).show();
            }
        }
        if (intent.getBooleanExtra(EDITOR_KEY, false)) {
            Toast.makeText(this, "Editor Mode", Toast.LENGTH_SHORT).show();
            Intent intentSender = new Intent(this, GameActivity.class);
            intentSender.putExtra(NAME_KEY, name);
            intentSender.putExtra(AGE_KEY, age);
            intentSender.putExtra(LAYOUT_KEY, layoutSize);
            intentSender.putExtra(MINES_KEY, noOfMines);
            setResult(1, intentSender);
            finishThis();
        } else {
            Toast.makeText(this, "Starter Mode", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(NAME_KEY, name);
            intent.putExtra(AGE_KEY, age);
            intent.putExtra(LAYOUT_KEY, layoutSize);
            intent.putExtra(MINES_KEY, noOfMines);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                //finishAndRemoveTask();
                StartActivity.this.startActivity(intent);

            finishThis();
        }
    }

    class checkSize implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (progress) {
                case 0:
                    layoutSizeTV.setText("Board Size: 3x5");
                    noOfMinesSeeker.setMax(4);
                    noOfMinesSeeker.setProgress(2);
                    layoutSize = 0;
                    noOfMines = 3;
                    break;
                case 1:
                    layoutSizeTV.setText("Board Size: 6x10");
                    noOfMinesSeeker.setMax(19);
                    noOfMinesSeeker.setProgress(9);
                    layoutSize = 1;
                    noOfMines = 10;
                    break;
                case 2:
                    layoutSizeTV.setText("Board Size: 9x15");
                    noOfMinesSeeker.setMax(49);
                    noOfMinesSeeker.setProgress(24);
                    layoutSize = 2;
                    noOfMines = 25;
                    break;
                default:
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class checkUpdate implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int temp = progress + 1;
            noOfMinesTV.setText("No. of Mines: " + temp);
            noOfMines = temp;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    //    public void updateFields(View view) {
//        String s = editText.getText().toString();
//        Intent intent = new Intent();
//        intent.putExtra(KEY, s);
//        setResult(1, intent);
//        finish();
//    }
    // TODO: 08-Feb-18
    private void finishThis() {
        finish();
    }

}
