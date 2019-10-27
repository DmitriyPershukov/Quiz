package com.company;

import javax.ws.rs.NotSupportedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.util.*;

public class Quiz
{
    List<Question> questionsList = new ArrayList<Question>();
    private int score = 0;
    private int currentQuestion = -1;
    private int i = 0;
    private int askedQuestionsNumber = 0;
    boolean quizWantsAnswer = false;
    public String getScore()
    {
        return "Your score is " + score + " out of " + askedQuestionsNumber;
    }

    public String getNewQuestion()
    {
        currentQuestion++;
        if(questionsList.size() == 0)
        {
            return  "База вопросов пуста";
        }
        if(currentQuestion >= questionsList.size())
        {
            return "Вопросы в базе вопросов кончились";
        }
        quizWantsAnswer = true;
        return questionsList.get(currentQuestion).getQuestion();
    }
    public String checkAnswer(String userAnswer)
    {
        askedQuestionsNumber++;
        quizWantsAnswer = false;
        boolean result = false;
        try
        {
            result = questionsList.get(currentQuestion).checkAnswer(userAnswer);
        }
        catch (SignatureException t)
        {
            askedQuestionsNumber--;
            quizWantsAnswer = true;
            return "Неправильный формат ответа !!";
        }
        if (result)
        {
            score++;
            return "Правильно !!!";
        }
        else
        {
            return "Неправильно !";
        }
    }

    public void clearScore()
    {
        score = 0;
        currentQuestion = -1;
        quizWantsAnswer = false;
        askedQuestionsNumber = 0;
    }

    public Question getCurrentQuestion()
    {
        return questionsList.get(currentQuestion);
    }

    public String repeatQuestion()
    {
        if(questionsList.size() == 0)
        {
            return  "База вопросов пуста";
        }
        if(currentQuestion >= questionsList.size())
        {
            return "Вопросы в базе вопросов кончились";
        }
        if(!quizWantsAnswer || currentQuestion == -1)
        {
            return "Вы не задали вопрос";
        }
        return questionsList.get(currentQuestion).getQuestion();
    }

    public List<String[]> splitByMarks (String[] questionsText, String[] markers)
    {
        List<String[]> outputArray = new ArrayList<>();
        List<String> markerList = new ArrayList<String>(Arrays.asList(markers));
        for (int you = 0; you < questionsText.length;)
        {
            List<String> output = new ArrayList<>();
            String currentString = questionsText[you];
            if (markerList.contains(currentString))
            {
                you++;
                output.add(currentString);
                currentString = questionsText[you];
                while (!markerList.contains(currentString))
                {
                    output.add(currentString);
                    if(you == questionsText.length - 1)
                    {
                        you++;
                        break;
                    }
                    you++;
                    currentString = questionsText[you];
                }
            }
            String[] addOutput = output.toArray(String[]::new);
            outputArray.add(addOutput);
        }
        return outputArray;
    }

    public void modifyQuestionsList(String path) throws Exception
    {
        String text = Files.readString(Paths.get(Config.path));
        String[] questionsText = text.split("\r\n");
        List<String[]> questionsSource = splitByMarks(questionsText, new String[]{"oaq","maq","faq"});
        for(int i = 0; i < questionsSource.size(); i++)
        {
            Question.questionType questionType = Question.questionType.getQuestionTypeByLabel(questionsSource.get(i)[0]);
            switch (questionType) {
                case oneAnswerQuestion:
                    OneAnswerQuestion newQuestion = new OneAnswerQuestion();
                    newQuestion.setQuestion(questionsSource.get(i));
                    questionsList.add(newQuestion);
                    break;
                case multipleAnswerQuestion:
                    throw new NotSupportedException();
                case freeAnswerQuestion:
                    throw new NotSupportedException();
                default:
                    throw new Exception("Format of question file is wrong");
            };
        }
    }

    public void clearQuestionsList()
    {
        currentQuestion = 0;
        questionsList.clear();
    }


    void shuffleQuestions()
    {
        currentQuestion = 0;
        Utility.shuffleList(questionsList);
    }
}