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

        // get the answer character from QuizActivity through Intent's extra
        // also get whether the answer was shown
        mAnswerCharacter = getIntent().getCharExtra(EXTRA_ANSWER_CHARACTER, 'X');
        mIsAnswerShown = getIntent().getBooleanExtra(EXTRA_ANSWER_SHOWN, false);

        setAnswerShownResult();

        // grab the TextView object for use by CheatActivity
        mTextViewAnswer = (TextView) findViewById(R.id.text_answer);

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

    //////////////////////////
    // setAnswerShownResult //
    //////////////////////////

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
        setResult(RESULT_OK, data);
    } // setAnswerShownResult

} // CheatActivity
