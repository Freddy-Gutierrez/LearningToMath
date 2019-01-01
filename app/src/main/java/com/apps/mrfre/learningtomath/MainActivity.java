package com.apps.mrfre.learningtomath;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static MediaPlayer mediaPlayer;
    public static boolean isPlaying = false;
    int songPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Menu");
        if(!Music.isPlaying){
            mediaPlayer = MediaPlayer.create(this, R.raw.uke);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            Music.isPlaying = true;
        }
        else{

        }
        //if screen rotates go to position where song left off
        if(savedInstanceState != null){
            songPosition = savedInstanceState.getInt("songTime");
            Music.userPause = savedInstanceState.getBoolean("state");
            mediaPlayer.seekTo(songPosition);
            if(!Music.userPause)
                mediaPlayer.start();
        }
//        DataBaseHelper myDB = new DataBaseHelper(this);
//        myDB.clearTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater(); //create inflator
        menuInflater.inflate(R.menu.menu, menu);  //inflate the menu

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        new AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage("Freddy Gutierrez built the Learning To Math myapps as a Free myapps. This SERVICE is provided by Freddy Gutierrez at no cost and is intended for use as is.\n\n" +
                        "This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.\n\n" +
                        "Information Collection and Use\n\n" +
                        "The only data stored within Learning To Math are the user’s initials (which the user can choose to provide or not) which are used to display in the “High Scores” section of the myapps. The data itself is not used in any other way. Other than this, none of the user’s data is stored nor used. \n")
                .show();
        return true;
    }

    public void mathMenu(View view) {
        //start math menu activity
        Intent i = new Intent(MainActivity.this, MathOperator.class);
        startActivity(i);
        finish();
    }

    public void goToHighscores(View view) {
        Intent i = new Intent(MainActivity.this, HighScoreMenu.class);
        startActivity(i);
        finish();
    }

    public void playPause(View view) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Music.userPause = true;
        }
        else {
            mediaPlayer.start();
            Music.userPause = false;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("songTime", mediaPlayer.getCurrentPosition());
        outState.putBoolean("state", Music.userPause);
        mediaPlayer.pause();
    }
}
