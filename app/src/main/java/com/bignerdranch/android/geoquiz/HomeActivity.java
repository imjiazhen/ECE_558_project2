// TODO : use Spinner to select a different quiz

package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
/**
 * Created by riqbal on 5/18/2017.
 */

public class HomeActivity extends Activity implements AdapterView.OnItemSelectedListener {

    // tag for debug printing & identification
    private static final String TAG = "HomeActivity";

    // extra data being passed from HomeActivity --> QuizActivity
    private static final int ACTIVITY_QUIZ = 7;

    // preferences file for persistent storage
    public static final String PREF_FILE_NAME = "QuizPrefsFile";

    // Constants for quiz selection
    private static final int CODE_ANIMAL_LANGUAGE = 1;
    private static final int CODE_SCIENCE_FRICTION = 2;

    // GUI elements
    private Spinner mSpinnerQuizzes;
    private Button mButtonStart;
    private TextView  mTextViewPrevScore;

    // Class attributes
    private int mQuizSelection = 0;

    //////////////
    // onCreate //
    //////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_home);

        // grab the Spinner for use by HomeActivity
        mSpinnerQuizzes = (Spinner) findViewById(R.id.spinner_quizzes);
        mSpinnerQuizzes.setOnItemSelectedListener(this);

        // wire up the 'Start' button
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, QuizActivity.class);
                i.putExtra(QuizActivity.EXTRA_QUIZ_SELECTION, mQuizSelection);
                startActivityForResult(i, ACTIVITY_QUIZ);
            } // onClick

        }); // onClickListener -- mButtonStart

        // wire up the PrevScore text
        mTextViewPrevScore = (TextView) findViewById(R.id.text_prev_score);

        // restore the previous quiz score
        SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
        float PrevScore = settings.getFloat("PrevQuizScore", -1.0f);
        if (PrevScore >= 0.0f) {
            String str = getResources().getString(R.string.text_prev_score) + PrevScore + "%";
            mTextViewPrevScore.setText(str);
        }
    } // onCreate

    //////////////////////
    // onActivityResult //
    //////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_QUIZ) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    } // onActivityResult

    ////////////////////
    // onItemSelected //
    ////////////////////

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String[] quizzes = getResources().getStringArray(R.array.array_quizzes);
        String sel = parent.getItemAtPosition(pos).toString();

        // this will select "Animal Language"
        if (sel.equals(quizzes[0])) {
            mQuizSelection = CODE_ANIMAL_LANGUAGE;
        }

        // this will select "Science Friction"
        else if (sel.equals(quizzes[1])){
            mQuizSelection = CODE_SCIENCE_FRICTION;
        }
    } // onItemSelected

    ///////////////////////
    // onNothingSelected //
    ///////////////////////

    public void onNothingSelected(AdapterView<?> parent) {

    } // onNothingSelected

} // HomeActivity
