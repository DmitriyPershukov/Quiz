package com.company;

import org.junit.Test;

import java.io.IOException;
import java.security.SignatureException;

import static org.junit.Assert.*;

public class QuizTest
{
    Quiz quiz = new Quiz();

    @Test
    public void getScore()
    {
        assertEquals("Your score is 0 out of 0", quiz.getScore());
    }

    @Test
    public void getNewQuestion() throws Exception
    {

        assertEquals("База вопросов пуста", quiz.getNewQuestion());
        quiz.modifyQuestionsList(Config.path);
        for(int i = 0; i < 5; i++)
        {
            quiz.getNewQuestion();
        }
        assertEquals("Вопросы в базе вопросов кончились", quiz.getNewQuestion());
        quiz.clearQuestionsList();
    }

    @Test
    public void checkAnswer() throws SignatureException {
        OneAnswerQuestion newQuestion = new OneAnswerQuestion();
        newQuestion.question = "";
        newQuestion.possibleAnswers = new String[]{"2", "2", "3"};
        newQuestion.rightAnswer = "3";
        for (int o = 0; o < 3; o++) {
            quiz.questionsList.add(newQuestion);
        }
        quiz.getNewQuestion();
        assertEquals("Неправильный формат ответа !!", quiz.checkAnswer("tru"));
        quiz.getNewQuestion();
        assertEquals("Неправильно !", quiz.checkAnswer(newQuestion.answerNumber + "" + 1));
        quiz.getNewQuestion();
        assertEquals("Правильно !!!", quiz.checkAnswer("" + newQuestion.answerNumber));
    }

    @Test
    public void clearQuestionsList()
    {
        quiz.clearQuestionsList();

        assertArrayEquals(new Question[0], quiz.questionsList.toArray());
    }
}