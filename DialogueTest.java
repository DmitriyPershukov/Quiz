package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class DialogueTest
{
    @Test
    public void returnQuizAnswer() throws Exception
    {
        QuestionListFactory.modifyQuestionsList();
        Dialogue dialogue = new Dialogue("nau");
        String output0 = dialogue.returnQuizAnswer("/restart you").text;
        assertEquals("Эта команда аргумента не имеет", output0);
        String output1 = dialogue.returnQuizAnswer("/repeat").text;
        assertEquals("Вы не задали вопрос", output1);
        String output2 = dialogue.returnQuizAnswer("/ruy").text;
        assertEquals("Такой команды нет", output2);
        /*String output3 = dialogue.returnQuizAnswer("/add").text;
        assertEquals("Этой команде нужен аргумент",output3);*/
        /*Dialogue dialogue1 = new Dialogue("nau");
        dialogue.addQuestions(Config.path);
        dialogue1.returnQuizAnswer("/addQuestions" + " " + Config.path);
        Quiz quiz0 = dialogue.getQuiz();
        Quiz quiz1 = dialogue1.getQuiz();
        boolean areArraysEqual = true;
        for(int pit = 0; pit < quiz0.questionsList.size(); pit++) {
            Question question = quiz0.questionsList.get(pit);
            Question question1 = quiz1.questionsList.get(pit);
            if (question.type == question1.type)
            {
                if(Question.questionType.oneAnswerQuestion.equals(question.type))
                {
                    OneAnswerQuestion onewAnswerQuestion1 = (OneAnswerQuestion) question1;
                    OneAnswerQuestion oneAnswerQuestion = (OneAnswerQuestion) question;
                    boolean areEqual = true;
                    for (int j = 0; j < oneAnswerQuestion.possibleAnswers.length; j++)
                    {
                        if (oneAnswerQuestion.getPossibleAnswers()[j].equals(onewAnswerQuestion1.getPossibleAnswers()[j]))
                        {}
                        else
                        {
                            areEqual = false;
                        }
                    }
                    if (areEqual == false)
                    {
                        areArraysEqual = false;
                    }
                    if(!oneAnswerQuestion.rightAnswer.equals(onewAnswerQuestion1.rightAnswer))
                    {
                        areArraysEqual = false;
                    }
                }
                if (!question1.question.equals(question.question))
                {
                    areArraysEqual = false;
                }
            }
            else
            {
                areArraysEqual = false;
            }
        }
        assertTrue(areArraysEqual);*/
    }
}