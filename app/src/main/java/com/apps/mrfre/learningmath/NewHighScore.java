package com.apps.mrfre.learningmath;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewHighScore extends AppCompatActivity {

    TextView msg;
    TextView enterName;
    TextView userName;
    Button save;

    //used for new user name and score
    String newName= "";
    int score;

    //used to save the name and score of data already in the table
    String saveName = "";
    int saveScore = 0;

    DataBaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_high_score);
        setTitle("New High Score");

        //create table
        myDatabase = new DataBaseHelper(this);
        msg = (TextView)findViewById(R.id.highscoreMsg);
        enterName = (TextView)findViewById(R.id.usernameMsg);
        userName = (TextView)findViewById(R.id.username);
        save = (Button)findViewById(R.id.save);

    }

    public void saveClick(View view) {
        Cursor res = myDatabase.getAllData();
        if(res.getCount() < 3){
            //first 3 scores added are high scores
            insertScore();
            Intent i = new Intent(NewHighScore.this, HighScoreMenu.class);
            startActivity(i);
            finish();
        }
        else{
            findRow();
            Intent i = new Intent(NewHighScore.this, HighScoreMenu.class);
            startActivity(i);
            finish();
        }
    }

    //finds proper row to insert new score into
    public void findRow() {
        newName = userName.getText().toString().toUpperCase();
        score = getIntent().getIntExtra("UserScore", 0);
        //insert into proper row (descending by score) and shift values below down
        Cursor res = myDatabase.getAllData();
        int i = 0;
        while (res.moveToNext() && i < 3) {
            //if new score is greater than score of current row update row with new score then shift current entries below down
            if(score >= res.getInt(2)){
                //save name and score of entry about to be replaced
                saveName = res.getString(1);
                saveScore = res.getInt(2);
                //replace data in row with new user data
                updateLeaderboard(res.getString(0));
                //assign the previously saved data to variables used in updateLeaderboard method
                newName = saveName;
                score = saveScore;
                i++;
            }
        }
    }

    public void insertScore(){
        newName = userName.getText().toString().toUpperCase();
        score = getIntent().getIntExtra("UserScore", 0);
        boolean isInserted = myDatabase.insertData(newName, score);
        if(isInserted){
            Log.i("DATA", "SUCCESS");
        }
        else{
            Log.i("DATA", "FAILED");
        }
    }

    //updates table by replacing row with new user data using row id
    public void  updateLeaderboard(String id){
        boolean isUpdated = myDatabase.updateData(id, newName, score);
        if(isUpdated){
            Log.i("DATA", "SUCCESS");
        }
        else{
            Log.i("DATA", "FAILED");
        }
    }
}
