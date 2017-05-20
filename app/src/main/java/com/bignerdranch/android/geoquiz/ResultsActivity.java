// TODO : save the score with SharedPreferences capability

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
    private static final String KEY_QUIZ_LENGTH = "QuizLength";
    private static final String KEY_QUIZ_CHEAT_TOTAL = "CheatTotal";

    // extra data being passed from QuizActivity --> ResultsActivity
    public static final String EXTRA_QUIZ_PERCENT = "com.bignerdranch.android.geoquiz.quiz_score";
    public static final String EXTRA_QUIZ_LENGTH = "com.bignerdranch.android.geoquiz.quiz_length";
    public static final String EXTRA_QUIZ_CHEAT_TOTAL = "com.bignerdranch.android.geoquiz.cheat_total";

    // GUI elements
    private TextView mTextViewScore;
    private TextView mTextViewQuizLength;
    private TextView mTextViewCheatTotal;
    private Button mButtonHome;
    
    // Class attributes
    private double mQuizPercent;
    private int mQuizLength;
    private int mCheatTotal;

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
        mQuizLength = getIntent().getIntExtra(EXTRA_QUIZ_LENGTH, 1);
        mCheatTotal = getIntent().getIntExtra(EXTRA_QUIZ_CHEAT_TOTAL, 0);
        
        // grab the TextView object for displaying correct %
        // then display score
        mTextViewScore = (TextView) findViewById(R.id.text_score);
        updateTextScore();

        // grab the TextView object for display # of questions
        mTextViewQuizLength = (TextView) findViewById(R.id.text_total_questions);
        updateQuizLength();

        // grab the TextView object for displaying cheat total
        mTextViewCheatTotal = (TextView) findViewById(R.id.text_cheat_total);
        updateCheatTotal();

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
            mQuizLength = savedInstanceState.getInt(KEY_QUIZ_LENGTH, 1);
            mCheatTotal = savedInstanceState.getInt(KEY_QUIZ_CHEAT_TOTAL, 0);
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
        savedInstanceState.putInt(KEY_QUIZ_LENGTH, mQuizLength);
        savedInstanceState.putInt(KEY_QUIZ_CHEAT_TOTAL, mCheatTotal);

    } // onSaveInstanceState

    /////////////////////
    // updateTextScore //
    /////////////////////

    private void updateTextScore() {
        String str = "" + mQuizPercent + "%";
        mTextViewScore.setText(str);

    } // updateTextScore

    //////////////////////
    // updateQuizLength //
    //////////////////////

    private void updateQuizLength() {
        String str = getResources().getString(R.string.text_total_questions) + mQuizLength;
        mTextViewQuizLength.setText(str);

    } // updateTextScore

    //////////////////////
    // updateCheatTotal //
    //////////////////////

    private void updateCheatTotal() {
        String str = getResources().getString(R.string.text_cheat_questions) + mCheatTotal;
        mTextViewCheatTotal.setText(str);

    } // updateTextScore

} // ResultsActivity
