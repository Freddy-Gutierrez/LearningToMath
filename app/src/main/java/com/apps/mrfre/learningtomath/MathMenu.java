package com.apps.mrfre.learningtomath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MathMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_menu);
        setTitle("Difficulty");
    }

    public void backToMain(View view) {
        //back button returns user to main menu activity
        Intent i = new Intent(MathMenu.this, MathOperator.class);
        startActivity(i);
        finish();
    }

    public void startGame(View view) {
        Intent operator = getIntent();
        char userChar = operator.getCharExtra("op", '1');
        Button pressed = (Button)view;
        Intent i = new Intent(MathMenu.this, MathGameplay.class);
        //save game difficulty from button and operator
        i.putExtra("dif", pressed.getText().toString());
        i.putExtra("op", userChar);
        startActivity(i);
        finish();
    }
}
