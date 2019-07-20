package com.apps.mrfre.learningtomath;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MathGameplay extends AppCompatActivity {

    //group for buttons and texts
    TextView timer;
    TextView problem;
    TextView score;
    TextView displayScore;
    Button zero;
    Button one;
    Button two;
    Button three;
    Button playAgain;
    Button backToMain;
    Button centerMainMenu;

    //objects that will be used in game logic
    Random rand = new Random();
    char operator;
    String difficulty;
    long timerSec = 31000;
    int correct = 0;
    int numOfQuestions = 0;
    int operandLimit = 0;
    int varOne;
    int varTwo;
    int answer;
    int[] divNums;
    int numerator = 0;
    int denominator = 0;
    int dummyLimit = 26;
    ArrayList<Integer> possibleAnswers = new ArrayList<Integer>();

    //create SQL table
    DataBaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_gameplay);
        setTitle(getIntent().getStringExtra("title"));

        //create table
        myDatabase = new DataBaseHelper(this);

        //assign buttons and texts
        timer = (TextView)findViewById(R.id.timerTextView);
        problem = (TextView)findViewById(R.id.problemTextView);
        score = (TextView)findViewById(R.id.scoreTextView);
        displayScore = (TextView)findViewById(R.id.correctTextView);

        zero = (Button)findViewById(R.id.button0);
        one = (Button)findViewById(R.id.button1);
        two = (Button)findViewById(R.id.button2);
        three = (Button)findViewById(R.id.button3);
        playAgain = (Button)findViewById(R.id.playAgainButton);
        backToMain = (Button)findViewById(R.id.mainMenuButton);
        centerMainMenu = (Button)findViewById(R.id.button_CenterMain);


        //check to see if there's a savedInstanceState, meaning there was a screen rotation. If so reassign values with the saved state versions from bundle
        if(savedInstanceState != null){
            timerSec = savedInstanceState.getLong("timer");
            operator = savedInstanceState.getChar("op");
            difficulty = savedInstanceState.getString("diff");
            numOfQuestions = savedInstanceState.getInt("numOfQs");
            operandLimit = savedInstanceState.getInt("opLimit");
            varOne = savedInstanceState.getInt("opOne");
            varTwo = savedInstanceState.getInt("opTwo");
            answer = savedInstanceState.getInt("ans");
            correct = savedInstanceState.getInt("numCorrect");
            numerator = savedInstanceState.getInt("numer");
            denominator = savedInstanceState.getInt("denom");
            divNums = savedInstanceState.getIntArray("divisibleNums");
            possibleAnswers = savedInstanceState.getIntegerArrayList("posAns");
            setViews();
            if(Music.freePlay){
                timer.setText("∞");
                backToMain.setGravity(Gravity.CENTER_HORIZONTAL);
                backToMain.setVisibility(View.VISIBLE);
            }
            else{
                beginTimer();
            }
        }
        else {
            score.setText("0/0");
            assignInitialValues();
            run();
            if(Music.freePlay){
                timer.setText("∞");
                centerMainMenu.setVisibility(View.VISIBLE);
            }
            else{
                beginTimer();
            }
        }
    }

    private void setViews() {
        setEquation();
        score.setText(correct + "/" + numOfQuestions);
        zero.setText(Integer.toString(possibleAnswers.get(0)));
        one.setText(Integer.toString(possibleAnswers.get(1)));
        two.setText(Integer.toString(possibleAnswers.get(2)));
        three.setText(Integer.toString(possibleAnswers.get(3)));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timer", timerSec);
        outState.putChar("op", operator);
        outState.putString("diff", difficulty);
        outState.putInt("numOfQs", numOfQuestions);
        outState.putInt("opLimit", operandLimit);
        outState.putInt("opOne", varOne);
        outState.putInt("opTwo", varTwo);
        outState.putInt("numCorrect", correct);
        outState.putInt("ans", answer);
        outState.putInt("numer", numerator);
        outState.putInt("denom", denominator);
        outState.putIntArray("divisibleNums", divNums);
        outState.putIntegerArrayList("posAns", possibleAnswers);
    }

    private void run(){     //runs essential methods for add, sub and mult
        assignNumbers();
        setButtonNums();
    }

    private void beginTimer() {
        new CountDownTimer(timerSec,1000){            //CountDownTimer used for the game, 30s default time
            @Override
            public void onTick(long l) {
                timerSec -= 1000;
                timer.setText(l/1000 + "s");                                    //updates the text for the timer for every tick
            }

            @Override
            public void onFinish() {
                timer.setText("0s");                                                        //when time is up, set timer text to 0s
                displayScore.setText("Your Score: " + score.getText().toString());      //updates text to display user score at the end of the game
                zero.setClickable(false);                                                    //make buttons unclickable after the game is over
                one.setClickable(false);
                two.setClickable(false);
                three.setClickable(false);
                checkForHighScore();                                                        //call method to check if user got a highscore
                playAgain.setVisibility(View.VISIBLE);                                      //make the play again button visisble
                backToMain.setVisibility(View.VISIBLE);                                      //make the main menu button visisble

            }
        }.start();                                                                          //start timer
    }

    private void assignInitialValues() {
        Intent i = getIntent();
        //get operator user wants to perform. If none found default to !
        operator = i.getCharExtra("op", '!');
        //get user difficulty
        difficulty = i.getStringExtra("dif");

        //multiplication will only have a default bound of 12
        if(operator == 'x'){
            operandLimit = 12;
        }

        //call method to populate array with myappsropriate values
        else if(operator == '/'){
            populateDivArray();
        }
        //call method to determine operand bounds based on difficulty
        else{
            setAddSubOperandLimits();
        }
    }

    private void checkForHighScore() {
        Cursor res = myDatabase.getAllData();
        if(res.getCount() == 0){
            //first score added
            Intent i = new Intent(MathGameplay.this, NewHighScore.class);
            i.putExtra("UserScore", correct);
            startActivity(i);
            finish();
        }
        else{
            while (res.moveToNext()){
                //if user score is greater than any value in the highscores database use has new high score
                if(correct >= res.getInt(2)){
                    Intent i = new Intent(MathGameplay.this, NewHighScore.class);
                    i.putExtra("UserScore", correct);
                    startActivity(i);
                    finish();
                }
            }
        }
    }


    //populate array containing values to be divided to ensure whole numbers.
    private void populateDivArray() {
        operandLimit = 13;
        divNums = new int[13];
        denominator = Integer.parseInt(difficulty);
        int curValue = 0;
        divNums[0] = curValue;
        for(int i = 1; i < divNums.length; i++){
            curValue += denominator;
            divNums[i] = curValue;
        }
    }

    //switch case using user difficulty to set limits of operands used in equations
    private void setAddSubOperandLimits() {
        switch (difficulty){
            case "EASY":
                operandLimit = 11;
                break;
            case "MEDIUM":
                operandLimit = 51;
                break;
            case "HARD":
                operandLimit = 101;
                break;
            default:
                System.out.println("Hopefully you don't run into this problem");
        }

    }

    public void assignNumbers(){        //generates the numbers that will be used in the equation for add, sub and mult
        if(operator == '/'){
            varOne = rand.nextInt(operandLimit);
            numerator = divNums[varOne];
        }
        else{
            varOne = rand.nextInt(operandLimit);
            varTwo = rand.nextInt(operandLimit);
        }
        setEquation();
    }

    //set equation depending on the operator specified by the user
    private void setEquation() {
        switch (operator){
            case '+':
                answer = varOne + varTwo;
                problem.setText(varOne + " + " + varTwo);
                break;
            case '-':
                answer = varOne - varTwo;
                problem.setText(varOne + " - " + varTwo);
                break;
            case '/':
                answer = numerator / denominator;
                problem.setText(numerator + " / " + denominator);
                break;
            case 'x':
                answer = varOne * varTwo;
                problem.setText(varOne + " x " + varTwo);
                break;
            default:
                System.exit(0);
        }
    }

    public void setButtonNums(){
        int answerButton = rand.nextInt(4);             //picks a number between 0-3 to assign the correct answer to
        Log.i("Answer", "AnswerButton: " + Integer.toString(answerButton));
        int incorrectAnswer = 0;
        int limit = 0;
        //since divisions operandLimit is 13 max, there are only 13 possible outcomes for incorrect answers
        //in order to provide more variety for incorrect answers, a dummy upper bound is used to generate
        if(operator == '/')
            limit = dummyLimit;
        else
            limit = operandLimit;


        for(int i = 0; i < 4; i++){
            if(i == answerButton){
                possibleAnswers.add(answer);
            }
            else{
                incorrectAnswer = rand.nextInt(limit);
                while(possibleAnswers.contains(incorrectAnswer) || incorrectAnswer == answer){
                    incorrectAnswer = rand.nextInt(limit);
                }
                possibleAnswers.add(incorrectAnswer);
            }
        }
        zero.setText(Integer.toString(possibleAnswers.get(0)));
        one.setText(Integer.toString(possibleAnswers.get(1)));
        two.setText(Integer.toString(possibleAnswers.get(2)));
        three.setText(Integer.toString(possibleAnswers.get(3)));
    }

    public void checkAnswer(View view) {                       //call to checkAnswer, generates new variables to be used for equation
        Button pressed = (Button)view;
        String userAnswer = pressed.getText().toString();
        if(Integer.parseInt(userAnswer) == answer){             //checks if the answer selected by the user is = to the answer of the generated equation
            correct++;                                      //if so increments number of correct answers and the textview displaying if the answer is correct or wrong
            displayScore.setText("Correct!");
        }
        else{
            displayScore.setText("Wrong!");                 //else displays textview to show the answer was wrong
        }
        //updates score tracker
        numOfQuestions++;
        score.setText(correct + "/" + numOfQuestions);
        possibleAnswers.clear();
        run();
    }

    public void backToMain(View view) {
        Intent i = new Intent(MathGameplay.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void playAgain(View view) {
        Intent i = getIntent();
        i.putExtra("op", operator);
        i.putExtra("dif", difficulty);
        finish();
        startActivity(i);
    }
}