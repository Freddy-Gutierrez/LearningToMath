package com.apps.mrfre.learningmath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PickDenominator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_denominator);
    }

    public void startGame(View view) {
        Intent operator = getIntent();
        char userChar = operator.getCharExtra("op", '1');
        Button pressed = (Button)view;
        Intent i = new Intent(PickDenominator.this, MathGameplay.class);
        //save game difficulty from button and operator
        i.putExtra("title", "DIVISION: " + pressed.getText().toString());
        i.putExtra("dif", pressed.getText().toString());
        i.putExtra("op", userChar);
        startActivity(i);
        finish();
    }

    public void back(View view) {
        Intent i = new Intent(PickDenominator.this, MathOperator.class);
        startActivity(i);
        finish();
    }
}
