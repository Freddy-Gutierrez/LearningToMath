package com.apps.mrfre.learningmath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MathOperator extends AppCompatActivity {

    Button bFreePlay;
    boolean freePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_operator);
        setTitle("Operators");
        bFreePlay = (Button)findViewById(R.id.button_freeplay);
        MainActivity.isPlaying = true;
        //check to see if there's a savedInstanceState, meaning there was a screen rotation. If so reassign values with the saved state versions from bundle
        if(savedInstanceState != null){
            Music.freePlay = savedInstanceState.getBoolean("freePlay");
            Music.freePlayValue = savedInstanceState.getString("freePlayValue");
            bFreePlay.setText(Music.freePlayValue);
        }
        else {
            bFreePlay.setText(Music.freePlayValue);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("freePlay", Music.freePlay);
        outState.putString("freePlayValue", Music.freePlayValue);
    }

    public void getChar(View view) {
        Button pressed = (Button)view;
        char charPressed =  pressed.getText().toString().charAt(0);
        Intent i;
        //multiplication does not need a user difficulty, go to gameplay and skip MathMenu
        if(charPressed == 'x'){
            i = new Intent(MathOperator.this, MathGameplay.class);
            i.putExtra("op", charPressed);
            i.putExtra("title", "MULTIPLICATION");
            i.putExtra("dif", "Multiplication");
            startActivity(i);
            finish();
        }
        else if(charPressed == '/'){
            i = new Intent(MathOperator.this, PickDenominator.class);
            i.putExtra("op", charPressed);
            startActivity(i);
            finish();
        }
        //else go to MathMenu to get difficulty
        else{
            i =  new Intent(MathOperator.this, MathMenu.class);
            i.putExtra("op", charPressed);
            startActivity(i);
            finish();
        }
    }

    public void backToMain(View view) {
        Intent i = new Intent(MathOperator.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void isFreePlay(View view) {
        if(Music.freePlay == false){
            Music.freePlay = true;
            Music.freePlayValue = "Free Play : On";
            bFreePlay.setText(Music.freePlayValue);
        }
        else{
            Music.freePlay = false;
            Music.freePlayValue = "Free Play : Off";
            bFreePlay.setText(Music.freePlayValue);
        }
    }
}
