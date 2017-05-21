package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Intent;
import java.io.File;

import static java.lang.Math.round;

public class QuizActivity extends Activity implements View.OnClickListener {

    // tag for debug printing & identification
    private static final String TAG = "QuizActivity";
    
    // key-value pairs to stash when activity is interrupted
    private static final String KEY_INDEX = "CurrentIndex";
    private static final String KEY_QUIZ_SCORE = "QuizScore";
    private static final String KEY_QUIZ_ITEM_ARRAY = "QuizItemArray";

    // Constants for quiz selection
    private static final int CODE_ANIMAL_LANGUAGE = 1;
    private static final int CODE_SCIENCE_FRICTION = 2;

    // extra data being passed from QuizActivity --> CheatActivity & ResultsActivity
    private static final int ACTIVITY_CHEAT = 8;
    private static final int ACTIVITY_RESULTS = 9;

    public static final String EXTRA_QUIZ_SELECTION = "com.bignerdranch.android.geoquiz.quiz_selection";

    // GUI elements
    private TextView mTextViewQuestion;
    private RadioButton mRadioButtonA;
    private RadioButton mRadioButtonB;
    private RadioButton mRadioButtonC;
    private RadioButton mRadioButtonD;
    private Button mButtonNext;
    private Button mButtonCheat;

    // Class attributes
    private int     mCurrentIndex = 0;
    private float   mQuizScore = 0.0f;
    private int     mQuizLength;

    // Data structure (Model) for the quiz
    private QuizItem[] mQuizItemArray;

    //////////////
    // onCreate //
    //////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // start off the app by inflating the view
        // then create a quiz from scratch
        // finally, restore previous quiz if you have it

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        createQuiz();
        restoreState(savedInstanceState);

        // wire up the text for question
        mTextViewQuestion = (TextView) findViewById(R.id.text_question);

        // wire up Radio Button A
        mRadioButtonA = (RadioButton) findViewById(R.id.radio_A);
        mRadioButtonA.setOnClickListener(this);

        // wire up Radio Button B
        mRadioButtonB = (RadioButton) findViewById(R.id.radio_B);
        mRadioButtonB.setOnClickListener(this);

        // wire up Radio Button C
        mRadioButtonC = (RadioButton) findViewById(R.id.radio_C);
        mRadioButtonC.setOnClickListener(this);

        // wire up Radio Button D
        mRadioButtonD = (RadioButton) findViewById(R.id.radio_D);
        mRadioButtonD.setOnClickListener(this);

        // now that all radio buttons are wired,
        // can update the question text & radio text
        updateQuestion();

        // wire up the Next button
        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numCheatOn = 0;

                // if we've hit the end of the quiz, go to ResultsActivity
                // need to pass a good amount of data over
                // percentage correct, number of questions, number cheated on...
                if (mCurrentIndex == (mQuizLength - 1)) {

                    Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);

                    // calculate score percentage & send it
                    float QuizPercentage = round((mQuizScore / ((float)(mQuizLength)))*100.0f);
                    intent.putExtra(ResultsActivity.EXTRA_QUIZ_PERCENT, QuizPercentage);

                    // send over quiz length
                    intent.putExtra(ResultsActivity.EXTRA_QUIZ_LENGTH, mQuizLength);

                    // tally up the cheating
                    for (int i = 0; i < mQuizLength; i++) {
                        if (mQuizItemArray[i].getCheatStatus() == true) numCheatOn++;
                    }

                    // send the cheat number
                    intent.putExtra(ResultsActivity.EXTRA_QUIZ_CHEAT_TOTAL, numCheatOn);
                    startActivityForResult(intent, ACTIVITY_RESULTS);
                }

                // otherwise, we still have quiz questions
                // increment & update
                else {
                    mCurrentIndex = (mCurrentIndex + 1);
                    updateQuestion();
                }

            } // onClick
        }); // onClickListener -- mButtonNext

        // wire up the Cheat button
        // with anonymous inner class

        mButtonCheat = (Button) findViewById(R.id.button_cheat);
        mButtonCheat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // send over the correct answer (char)
                // and start activity for result (need to check result)

                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                char correctAnswer = mQuizItemArray[mCurrentIndex].getQuizItemAnswer();
                i.putExtra(CheatActivity.EXTRA_ANSWER_CHARACTER, correctAnswer);
                startActivityForResult(i, ACTIVITY_CHEAT);

            } // onClick

        }); // onClickListener -- mButtonCheat

    } // onCreate

    //////////////////
    // restoreState //
    //////////////////

    // restore previous question index if available
    // also restore quiz score & cheat status
    public void restoreState(Bundle savedInstanceState) {

        Log.d(TAG, "restoreState(Bundle) called");

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuizScore = savedInstanceState.getFloat(KEY_QUIZ_SCORE, 0.0f);
            mQuizItemArray = (QuizItem[]) savedInstanceState.getSerializable(KEY_QUIZ_ITEM_ARRAY);
            mQuizLength = mQuizItemArray.length;
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

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putFloat(KEY_QUIZ_SCORE, mQuizScore);
        savedInstanceState.putSerializable(KEY_QUIZ_ITEM_ARRAY, mQuizItemArray);

    } // onSaveInstanceState

    ////////////////
    // createQuiz //
    ////////////////

    void createQuiz() {

        // attempt to create quiz from JSON
        // we will pass file over to JSONReader item to handle heavy-lifting
        boolean okToRead = isExternalStorageReadable();
        int quizSelection = getIntent().getIntExtra(EXTRA_QUIZ_SELECTION, 0);
        String fileName = "";

        if (okToRead) {
            JSONReader json_read = new JSONReader();

            String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            // pick which JSON file to load based on HomeActivity quiz selection
            if (quizSelection == CODE_SCIENCE_FRICTION) fileName = "friction.json";
            else if (quizSelection == CODE_ANIMAL_LANGUAGE) fileName = "animals.json";
            else {
                Log.d(TAG, "Couldn't determine quiz selection!");
                finish();
            }
            String fullpath = baseDir + File.separator + fileName;

            final File root = new File(fullpath);

            // now that JSON file determined, pass file to JSONReader
            mQuizItemArray = json_read.createQuizFromJSON(root);
            mQuizLength = mQuizItemArray.length;
        }

        else {
            Log.d(TAG, "Can't read the external storage!");
            finish();
        }

    } // createQuiz

    /////////////////////
    // OnClickListener //
    /////////////////////

    // listener for the radio buttons
    // QuizItem object will collect & set the responses
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.radio_A):
                mQuizItemArray[mCurrentIndex].collectResponse('A');
                break;
            case (R.id.radio_B):
                mQuizItemArray[mCurrentIndex].collectResponse('B');
                break;
            case (R.id.radio_C):
                mQuizItemArray[mCurrentIndex].collectResponse('C');
                break;
            case (R.id.radio_D):
                mQuizItemArray[mCurrentIndex].collectResponse('D');
                break;
        }

        checkAnswer();

    } // onClick -- mRadioButton(A,B,C,D)

    ////////////////////
    // updateQuestion //
    ////////////////////

    private void updateQuestion() {

        // method for updating the question statement
        // and radio button labels
        QuizItem question = mQuizItemArray[mCurrentIndex];
        String question_text = question.toString();

        mTextViewQuestion.setText(question_text);

        String choice_A = question.getQuizItemChoices()[0];
        mRadioButtonA.setText(choice_A);

        String choice_B = question.getQuizItemChoices()[1];
        mRadioButtonB.setText(choice_B);

        String choice_C = question.getQuizItemChoices()[2];
        mRadioButtonC.setText(choice_C);

        String choice_D = question.getQuizItemChoices()[3];
        mRadioButtonD.setText(choice_D);

    } // updateQuestion

    /////////////////
    // checkAnswer //
    /////////////////

    // check on QuizItem object if user's cheated on this
    // if so, display punish toast
    // otherwise, check for correctness

    private void checkAnswer() {

        QuizItem question = mQuizItemArray[mCurrentIndex];
        boolean answerCorrect = question.checkResult();
        int messageResId = 0;

        if (question.getCheatStatus()) {
            messageResId = R.string.toast_cheat;
        }

        else {
            if (answerCorrect) {
                messageResId = R.string.toast_correct;
                mQuizScore = mQuizScore + 1;
            }
            else {
                messageResId = R.string.toast_incorect;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    } // checkAnswer

    //////////////////////
    // onActivityResult //
    //////////////////////

    // check if cheat answer was shown or not
    // by looking at result code from CheatActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult called");

        if (requestCode == ACTIVITY_CHEAT) {
            if (data == null) return;
            else {
                boolean cheat_status = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
                mQuizItemArray[mCurrentIndex].setCheatStatus(cheat_status);
            }
        }
        else if (requestCode == ACTIVITY_RESULTS) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                finish();
            }
        }
    } // onActivityResult

    ///////////////////////////////
    // isExternalStorageReadable //
    ///////////////////////////////

    // Checks if external storage is available to at least read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    } // isExternalStorageReadable

} // QuizActivity