package com.company;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilityTest {
    Quiz quiz = new Quiz();

    @Test
    public void shuffleList() throws Exception
    {
        quiz.clearQuestionsList();
        quiz.modifyQuestionsList(Config.path);
        List<Question> list = quiz.questionsList;
        Utility.shuffleList(quiz.questionsList);
        List<Question> shuffledList = quiz.questionsList;
        boolean areEqual = true;
        for(int j = 0; j < quiz.questionsList.size(); j++)
        {
            if(!list.get(j).equals(shuffledList.get(j)));
            {
                areEqual = false;
            }
        }
        assertFalse(areEqual);
    }

    @Test
    public void isNumeric()
    {
        assertTrue(Utility.isNumeric("3"));
        assertFalse(Utility.isNumeric("quz"));
        assertFalse(Utility.isNumeric("?"));
    }

    @Test
    public void shuffleArray() throws Exception
    {
        quiz.clearQuestionsList();
        OneAnswerQuestion newQuestion = new OneAnswerQuestion();
        newQuestion.possibleAnswers = new String[]{"2", "2", "3"};
        String[] array = newQuestion.possibleAnswers;
        Utility.shuffleArray(newQuestion.possibleAnswers);
        String[] newArray = newQuestion.possibleAnswers;
        boolean areEqual;
        areEqual = true;
        for(int i = 0; i < newQuestion.possibleAnswers.length; i++)
        {
            if(!array[i].equals(newArray[i]));
            {
                areEqual = false;
            }
        }
        assertFalse(areEqual);
    }
}