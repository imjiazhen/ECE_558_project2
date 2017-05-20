// TODO : save the score with SharedPreferences capability
// TODO : display # of question, # cheated on, etc.

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
    private static final String KEY_QUIZ_PERCENT = "QuizPercent";

    // extra data being passed from QuizActivity --> ResultsActivity
    public static final String EXTRA_QUIZ_PERCENT = "com.bignerdranch.android.geoquiz.quiz_score";

    // GUI elements
    private TextView mTextViewScore;
    private Button mButtonHome;
    
    // Class attributes
    private double mQuizPercent;

    //////////////
    // onCreate //
    //////////////
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_results);
        restoreState(savedInstanceState);

        // get the quiz score from QuizActicity through Intent's extra
        mQuizPercent = getIntent().getDoubleExtra(EXTRA_QUIZ_PERCENT, 0.0);
        
        // grab the TextView object for use by ActivityResults
        // then display score
        mTextViewScore = (TextView) findViewById(R.id.text_score);
        updateTextScore();

        // wire up the 'Home' button
        mButtonHome = (Button) findViewById(R.id.button_home);
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent i = new Intent(ResultsActivity.this, HomeActivity.class);
                startActivity(i);
                setResult(RESULT_OK, null);
                finish();
            } // onClick
        }); // onClickListener -- mButtonHome

    } // onCreate

    //////////////////
    // restoreState //
    //////////////////

    // restore previous question index if available
    // also restore quiz score & cheat status
    public void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mQuizPercent = savedInstanceState.getDouble(KEY_QUIZ_PERCENT, 0.0);
        }
    } // restoreState

    /////////////////////////
    // onSaveInstanceState //
    /////////////////////////

    // save index of current question
    // along with score & cheat status
    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putDouble(KEY_QUIZ_PERCENT, mQuizPercent);

    } // onSaveInstanceState

    /////////////////////
    // updateTextScore //
    /////////////////////

    private void updateTextScore() {
        String str = "" + mQuizPercent + "%";
        mTextViewScore.setText(str);

    } // updateTextScore

} // ResultsActivity
