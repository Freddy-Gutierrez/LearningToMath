package com.apps.mrfre.learningtomath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MathOperator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_operator);
        setTitle("Operators");
        MainActivity.isPlaying = true;
    }

    public void getChar(View view) {
        Button pressed = (Button)view;
        char charPressed =  pressed.getText().toString().charAt(0);
        Intent i;
        //multiplication does not need a user difficulty, go to gameplay and skip MathMenu
        if(charPressed == 'x'){
            i = new Intent(MathOperator.this, MathGameplay.class);
            i.putExtra("op", charPressed);
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
}
