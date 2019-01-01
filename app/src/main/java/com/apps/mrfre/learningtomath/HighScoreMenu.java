package com.apps.mrfre.learningtomath;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HighScoreMenu extends AppCompatActivity {

    TextView nameOne;
    TextView scoreOne;
    TextView nameTwo;
    TextView scoreTwo;
    TextView nameThree;
    TextView scoreThree;
    DataBaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_menu);
        setTitle("Learning To Math");
        //create table
        myDatabase = new DataBaseHelper(this);

        nameOne = (TextView)findViewById(R.id.textViewName1);
        scoreOne = (TextView)findViewById(R.id.textViewScore1);
        nameTwo = (TextView)findViewById(R.id.textViewName2);
        scoreTwo = (TextView)findViewById(R.id.textViewScore2);
        nameThree = (TextView)findViewById(R.id.textViewName3);
        scoreThree = (TextView)findViewById(R.id.textViewScore3);
        display();
    }

    private void display(){
        Cursor res = myDatabase.getAllData();
        String[] names = new String[3];
        int[] scores = new int[3];
        int i = 0;
        while (res.moveToNext()) {
            names[i] = res.getString(1);
            scores[i] = res.getInt(2);
            Log.i("Name", names[i]);
            Log.i("Score", "" + scores[i]);
            i++;
        }
        if(i < 3) {
            for (int j = i; j < 3; j++) {
                names[j] = "   ";
                scores[j] = 0;
            }
        }

        nameOne.setText(names[0]);scoreOne.setText(Integer.toString(scores[0]));
        nameTwo.setText(names[1]);scoreTwo.setText(Integer.toString(scores[1]));
        nameThree.setText(names[2]);scoreThree.setText(Integer.toString(scores[2]));

    }

    public void goToMainMenu(View view) {
        Intent i = new Intent(HighScoreMenu.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
