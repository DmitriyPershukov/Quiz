package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class OneAnswerQuestionTest {
    OneAnswerQuestion testQuestion = new OneAnswerQuestion();

    @Test
    public void checkAnswer()
    {

    }

    @Test
    public void setQuestion()
    {
        String[] input = new String[]{"oaq","Текст вопроса","3","13","23","33"};
        testQuestion.setQuestion(input);
        String[] rightAnswers = new String[] {"13", "23", "33"};
        boolean areListsEqual = true;
        for (int i = 0; i < testQuestion.getPossibleAnswers().length ; i++)
        {
            if(rightAnswers[i].equals(testQuestion.getPossibleAnswers()[i]));
        }
        assertEquals("Текст вопроса", testQuestion.question);
        assertEquals("13", testQuestion.rightAnswer);
    }

    @Test
    public void getQuestion()
    {

    }
}