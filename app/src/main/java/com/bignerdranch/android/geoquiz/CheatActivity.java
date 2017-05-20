// TODO : need to handle landscape changes
// TODO : need to pass QuizActivity the state (score, cheat status, etc.)

package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

/**
 * Created by riqbal on 5/12/2017.
 */

public class CheatActivity extends Activity {

    // tag for debug printing & identification
    private static final String TAG = "CheatActivity";

    // key-value pairs to stash when activity is interrupted
    private static final String KEY_ANSWER_CHARACTER = "AnswerCharacter";
    private static final String KEY_IS_ANSWER_SHOWN = "IsAnswerShown";

    // extra data being passed from QuizActivity --> CheatActivity
    public static final String EXTRA_ANSWER_CHARACTER = "com.bignerdranch.android.geoquiz.answer_character";
    public static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    // GUI elements
    private TextView mTextViewAnswer;
    private Button mButtonShowAnswer;
    
    // Class attributes
    private char mAnswerCharacter;
    private boolean mIsAnswerShown;

    //////////////
    // onCreate //
    //////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);
        restoreState(savedInstanceState);

        // get the answer character from QuizActivity through Intent's extra
        // also get whether the answer was shown
        mAnswerCharacter = getIntent().getCharExtra(EXTRA_ANSWER_CHARACTER, 'X');
        mIsAnswerShown = getIntent().getBooleanExtra(EXTRA_ANSWER_SHOWN, false);

        setAnswerShownResult();

        // grab the TextView object for use by CheatActivity
        // also show the answer text if orientation was changed
        mTextViewAnswer = (TextView) findViewById(R.id.text_answer);
        if (mIsAnswerShown) {
            mTextViewAnswer.setText(String.valueOf(mAnswerCharacter));
        }

        // wire up the 'Show Answer' button
        mButtonShowAnswer = (Button) findViewById(R.id.button_answer);
        mButtonShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTextViewAnswer.setText(String.valueOf(mAnswerCharacter));
                mIsAnswerShown = true;
                setAnswerShownResult();

            } // onClick
        }); // onClickListener -- mButtonShowAnswer

    } // onCreate

    //////////////////
    // restoreState //
    //////////////////

    // restore previous question index if available
    // also restore quiz score & cheat status
    public void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mAnswerCharacter = savedInstanceState.getChar(KEY_ANSWER_CHARACTER, 'X');
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOWN, false);
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

        savedInstanceState.putChar(KEY_ANSWER_CHARACTER, 'X');
        savedInstanceState.putBoolean(KEY_IS_ANSWER_SHOWN, mIsAnswerShown);

    } // onSaveInstanceState

    //////////////////////////
    // setAnswerShownResult //
    //////////////////////////

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
        setResult(RESULT_OK, data);
    } // setAnswerShownResult

} // CheatActivity
