package com.bignerdranch.android.geoquiz; /**
 * @author Rehan Iqbal(riqbal@pdx.edu) <BR> <BR>
 *
 * This module handles the functionality for one multiple-choice question.
 * It holds the question statement, possible choices, user answer, and
 * whether they got it right or not. It also has methods to 'ask' itself
 * to the user, collect the response, and check for correctness.
 */

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class QuizItem implements Serializable{

    private char        QuizItemAnswer;
    private String      QuizItemQuestion;
    private String[]    QuizItemChoices;

    private boolean     AnsweredCorrect;
    private char        UserResponse;
    private boolean     CheatStatus;

    /**
     * Default constructor.  Checks the number of answer choices
     * and validity of the answer argument before assigning to object fields.
     * @param QuizItemQuestion - the question expressed as a sentence or equation
     * @param QuizItemChoices - an array of possible choices
     * @param QuizItemAnswer - the correct answer for the QuizItemQuestion
     */

    public QuizItem(String QuizItemQuestion, String[] QuizItemChoices, char QuizItemAnswer) {

        this.QuizItemQuestion = QuizItemQuestion;

        // validate the multiple-choice options

        if (QuizItemChoices.length >= 4) {
            this.QuizItemChoices = QuizItemChoices;
        }

        else throw new Error("\nNeed at least 4 multiple-choice options!");

        // validate the provided answer

        if ( String.valueOf(QuizItemAnswer).matches("[abcdABCD]") ) {
            this.QuizItemAnswer = QuizItemAnswer;
        }

        else throw new Error("\nNeed a valid character (A,B,C,D) for answer!");

    }

    public String[] getQuizItemChoices() {
        return QuizItemChoices;
    }

    public char getQuizItemAnswer() {
        return QuizItemAnswer;
    }

    /**
     * Method to ask grab the user's input. It will check for
     * a valid entry (i.e. a,b,c,d character) and store it
     * in the object's UserResponse field.
     */

    public void collectResponse(char UserAnswer) {

        String str = String.valueOf(UserAnswer);

        if (str.matches("[abcdABCD]")) UserResponse = str.toUpperCase().charAt(0);
        else throw new Error("\nInvalid input character for question!");
    }

    /**
     * Method to compare the user's answer against the official answer
     * (as provided at Quiz creation time).
     * @return AnsweredCorrect - boolean reflecting whether user got it right or not
     */

    public boolean checkResult() {

        // set boolean based on whether user's answer matches the official one
        AnsweredCorrect = (UserResponse == QuizItemAnswer) ? true : false;
        return AnsweredCorrect;
    }

    /**
     * Method to return cheat status of the question,
     * @return CheatStatus - whether or not the user cheated on this question
     */

    public boolean getCheatStatus() {
        return CheatStatus;
    }

    /**
     * Method to return cheat status of the question,
     * @param status - whether or not the user cheated on this question
     */

    public void setCheatStatus(boolean status) {
        CheatStatus = status;
    }

    /**
     * Method to print the question statement - not used by Quiz,
     * but may be useful for log files.
     * @return QuizItemQuestion - the question statement given to this QuizItem
     */

    public String toString() {

            return QuizItemQuestion;
    }

}