/**
 * Created by riqbal on 5/20/2017.
 */

package com.bignerdranch.android.geoquiz;

import org.json.simple.*;

import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter {

    public static void main(String args[]) {

        String[] Str_Questions = {

                "What is the Japanese word for dog?",
                "What is the Spanish word for cat?",
                "What is the French word for fish?",
                "What is the German word for rabbit?",
                "What is the Japanese word for bird?" };

        String[][] Str_Choices = {

                {"Chien", "Pero", "Inu", "Hund"},
                {"Chat", "Gato", "Neko", "Catze"},
                {"Poisson", "Sakana", "Fisch", "Peces"},
                {"Conejo", "Hase", "Lapin", "Usagi"},
                {"Vogel", "Pajaro", "Oiseau", "Tori"} };

        String[] Str_Answers = {"C", "B", "A", "B", "D"};

        JSONArray json_arr = BuildQuizItemArray(5, Str_Questions, Str_Choices, Str_Answers);

        try (FileWriter file = new FileWriter("C:\\Users\\riqbal\\Desktop\\test.json")) {
            file.write(json_arr.toJSONString());
            file.flush();

        } catch (IOException e) {
            System.out.println("There was an I/O Exception!");
            e.printStackTrace();
        }

    } // main

    static JSONArray BuildQuizItemArray(int length, String[] questions, String[][] choices, String[] answers) {

        JSONArray quiz_item_array = new JSONArray();

        for (int i = 0; i < length; i++) {
            JSONObject quiz_item = BuildQuizItem(questions[i], choices[i], answers[i]);
            quiz_item_array.add(quiz_item);
        }

        return quiz_item_array;
    } // BuildQuizItemArray
    //
    static JSONObject BuildQuizItem(String question, String[] choices, String answer) {

        JSONObject quiz_item = new JSONObject();

        String key1 = "Question";
        quiz_item.put(key1, question);

        String key2 = "Choices";
        JSONArray choice_array = BuildChoiceArray(choices);
        quiz_item.put(key2, choice_array);

        String key3 = "Answer";
        quiz_item.put(key3, answer);

        return quiz_item;

    } // BuildQuizItem

    static JSONArray BuildChoiceArray(String[] str_array) {

        JSONArray json_array = new JSONArray();

        for (int i = 0; i < str_array.length; i++) {
            json_array.add(str_array[i]);
        }
        return json_array;

    } // BuildChoiceArray

} // TestJSON

