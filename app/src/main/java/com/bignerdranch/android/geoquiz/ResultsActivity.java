package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;


public class ResultsActivity extends Activity {

    // tag for debug printing & identification
    private static final String TAG = "ResultsActivity";

    // key-value pairs to stash when activity is interrupted
    // (changing orientation to landscape)
    private static final String KEY_QUIZ_PERCENT = "QuizPercent";
    private static final String KEY_QUIZ_LENGTH = "QuizLength";
    private static final String KEY_QUIZ_CHEAT_TOTAL = "CheatTotal";

    // data being passed from QuizActivity --> ResultsActivity
    public static final String EXTRA_QUIZ_PERCENT = "com.bignerdranch.android.geoquiz.quiz_score";
    public static final String EXTRA_QUIZ_LENGTH = "com.bignerdranch.android.geoquiz.quiz_length";
    public static final String EXTRA_QUIZ_CHEAT_TOTAL = "com.bignerdranch.android.geoquiz.cheat_total";

    // preferences file for persistent storage
    public static final String PREF_FILE_NAME = "QuizPrefsFile";

    // GUI elements
    private TextView mTextViewScore;
    private TextView mTextViewQuizLength;
    private TextView mTextViewCheatTotal;
    private Button mButtonHome;
    
    // Class attributes
    private float mQuizPercent;
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

        // get the quiz score from QuizActivity through extra data
        mQuizPercent = getIntent().getFloatExtra(EXTRA_QUIZ_PERCENT, 0.0f);
        mQuizLength = getIntent().getIntExtra(EXTRA_QUIZ_LENGTH, 1);
        mCheatTotal = getIntent().getIntExtra(EXTRA_QUIZ_CHEAT_TOTAL, 0);
        
        // wire up the TextView object to display score
        mTextViewScore = (TextView) findViewById(R.id.text_score);
        updateTextScore();

        // wire up the TextView to display number of questions
        mTextViewQuizLength = (TextView) findViewById(R.id.text_total_questions);
        updateQuizLength();

        // wire up the TextView object to display cheat total
        mTextViewCheatTotal = (TextView) findViewById(R.id.text_cheat_total);
        updateCheatTotal();

        // wire up the 'Home' button
        mButtonHome = (Button) findViewById(R.id.button_home);
        mButtonHome.setOnClickListener(new View.OnClickListener() {

            // set result codes to terminate previous activities
            // keeps back stack from growing unnecessarily
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
            mQuizPercent = savedInstanceState.getFloat(KEY_QUIZ_PERCENT, 0.0f);
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

        savedInstanceState.putFloat(KEY_QUIZ_PERCENT, mQuizPercent);
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

    /////////////
    // onPause //
    /////////////

    // save the quiz results to persistent storage
    // HomeActivity will display this, too
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("PrevQuizScore", mQuizPercent);
        editor.commit();
    }

} // ResultsActivity
