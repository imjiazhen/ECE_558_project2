/**
 * Created by riqbal on 5/20/2017.
 */

package com.bignerdranch.android.geoquiz;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class JSONReader {

    public static void main (String args[]) {

        JSONParser parser = new JSONParser();

        try {
            Object arr = parser.parse(new FileReader("C:\\Users\\riqbal\\Desktop\\test.json"));
            JSONArray json_file = (JSONArray) arr;

            QuizItem[] QuizItemArray = parseQuizItemArray(json_file);
            System.out.println(QuizItemArray[3]);

        } catch (FileNotFoundException e) {
            System.out.println("File not found exception!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("(I/O exception!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Parse exception!");
            e.printStackTrace();
        }

    } // main

    static QuizItem[] parseQuizItemArray(JSONArray json_arr) {

        List<QuizItem> QuizItemList = new ArrayList<QuizItem>();

        for (int i = 0; i < json_arr.size(); i++) {
            JSONObject obj = (JSONObject) json_arr.get(i);
            QuizItem item = parseQuizItem(obj);
            QuizItemList.add(item);
        }

        QuizItem[] QuizItemArray = new QuizItem[QuizItemList.size()];
        QuizItemList.toArray(QuizItemArray);

        return QuizItemArray;

    } // parseQuizItemArray

    static QuizItem parseQuizItem(JSONObject obj) {

        String question = (String) obj.get("Question");

        JSONArray arr = (JSONArray) obj.get("Choices");
        String[] choices = parseChoiceArray(arr);

        String str = (String) obj.get("Answer");
        char answer = str.charAt(0);

        QuizItem item = new QuizItem(question, choices, answer);
        return item;

    } // parseQuizItem

    static String[] parseChoiceArray(JSONArray json_arr) {

        List<String> str = new ArrayList<String>();

        for (int i = 0; i < json_arr.size(); i ++) {

            String temp = (String) json_arr.get(i);
            str.add(temp);
        }

        String[] choices = new String[str.size()];
        str.toArray(choices);

        return choices;

    } // parseChoiceArray

} // JSONReader
