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

    private static final String TAG = "CheatActivity";
    public static final String EXTRA_ANSWER_CHARACTER = "com.bignerdranch.android.geoquiz.answer_character";
    public static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    private char mAnswerCharacter;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    //////////////
    // onCreate //
    //////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);

        mAnswerCharacter = getIntent().getCharExtra(EXTRA_ANSWER_CHARACTER, 'x');

        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        setAnswerShownResult(false);

        // wire up the 'Show Answer' button
        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAnswerTextView.setText(String.valueOf(mAnswerCharacter));
                setAnswerShownResult(true);
            } // onClick
        }); // onClickListener

    } // onCreate

    //////////////////////////
    // setAnswerShownResult //
    //////////////////////////

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    } // setAnswerShownResult

} // CheatActivity
