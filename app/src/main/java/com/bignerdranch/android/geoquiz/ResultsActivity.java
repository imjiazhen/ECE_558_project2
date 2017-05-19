package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

/**
 * Created by riqbal on 5/18/2017.
 */

public class ResultsActivity extends Activity {

    // tag for debug printing & identification
    private static final String TAG = "ResultsActivity";

    // key-value pair to stash when activity is interrupted
    private static final String KEY_QUIZ_SCORE = "QuizScore";

    // extra data being passed from QuizActivity --> ResultsActivity
    public static final String EXTRA_QUIZ_SCORE = "com.bignerdranch.android.geoquiz.quiz_score";

    // GUI elements
    private TextView mTextViewScore;
    
    // Class attributes
    private double mQuizScore;

    //////////////
    // onCreate //
    //////////////
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate*Bundle) called");
        setContentView(R.layout.activity_results);

        // get the quiz score from QuizActicity through Intent's extra
        mQuizScore = getIntent().getDoubleExtra(EXTRA_QUIZ_SCORE, 0.0);
        
        // grab the TextView object for use by ActivityResults
        mTextViewScore = (TextView) findViewById(R.id.text_score);
    }
}