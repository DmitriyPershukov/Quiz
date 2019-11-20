package com.company;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Question
{
    public int setQuestion(String[] questionsText, int i)
    {
        return 3;
    }

    questionType type;
    public questionType getQuestionType()
    {
        return type;
    }

    public String question;

    public boolean checkAnswer(String userAnswer) throws SignatureException {
        return true;
    }

    public String getQuestion()
    {
        return  question;
    }

    enum questionType
    {
        multipleAnswerQuestion("maq"),
        oneAnswerQuestion("oaq"),
        freeAnswerQuestion("faq");

        String label;

        public String getLabel() {
            return label;
        }

        private questionType(String label){
            this.label = label;
        }

        public static questionType getQuestionTypeByLabel(String stringLabel) throws Exception
        {
            for(questionType type: questionType.values())
            {
                if(stringLabel.equals(type.getLabel()))
                {
                    return type;
                }
            }
            throw new Exception("Format of question file is wrong");
        }
    }
}

class OneAnswerQuestion extends Question
{
    String[] possibleAnswers;
    String rightAnswer;
    int answerNumber;

    public String getRightAnswer()
    {
        return rightAnswer;
    }

    public String[] getPossibleAnswersForButtons()
    {
        String[] buttonsPossibleAnswers = new String[possibleAnswers.length];
        for(int u = 0; u < possibleAnswers.length; u++)
        {
            int number =1+ u;
            buttonsPossibleAnswers[u] = number + "";
        }
        return buttonsPossibleAnswers;
    }

    public int getAnswerNumber()
    {
        return answerNumber;
    }

    public String[] getPossibleAnswers()
    {
        return possibleAnswers;
    }

    public void setQuestion(String[] questionsText)
    {
        int j = 1;
        question = questionsText[j];
        type = questionType.oneAnswerQuestion;
        j++;
        int answersNumber = Integer.parseInt(questionsText[j]);
        j++;
        rightAnswer = questionsText[j];
        String[] newAnswers = new String[answersNumber];
        for(int w = 0; w < answersNumber; w++)
        {
            newAnswers[w] = questionsText[j];
            j++;
        }
        possibleAnswers = newAnswers;
    }

    public boolean checkAnswer(String userAnswer) throws SignatureException
    {
        if (!Utility.isNumeric(userAnswer))
        {
            throw new SignatureException();
        }
        int userAnswerNumber = Integer.parseInt(userAnswer);
        if(answerNumber == userAnswerNumber)
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    public String getQuestion()
    {
        StringBuilder outputBuilder = new StringBuilder(question + "\n");
        Utility.shuffleArray(possibleAnswers);
        for(int l = 0; l < possibleAnswers.length; l++)
        {
            int n = 1 + l;
            outputBuilder.append(n + ". " + possibleAnswers[l] + "\n");
            if(rightAnswer.equals(possibleAnswers[l]))
            {
                answerNumber = l + 1;
            }
        }
        return outputBuilder.toString().substring(0, outputBuilder.toString().length() - 1);
    }
}

class FreeAnswerQuestion extends Question
{
    public boolean checkAnswer(String userAnswer)
    {
        if(rightAnswer.equals(userAnswer))
        {
            return true;
        }
        else
        { return false;
        }
    }

    public void setQuestion(String[] questionsText)
    {
        int k = 1;
        question = questionsText[k];
    }
    public String getQuestion()
    {
        StringBuilder output = new StringBuilder();
        output.append(question).append("/n");
        output.append("Введите верный по вашему мнению ответ");
        return output.toString();
    }
    String rightAnswer;
}

class MultipleAnswerQuestion extends Question
{
    String[] possibleAnswers;

    public String[] getRightAnswer()
    {
        return rightAnswers;
    }
    public String getQuestion()
    {
        StringBuilder output = new StringBuilder();
        output.append(question + "/n");
        Arrays.asList(possibleAnswers).forEach((c)-> output.append(c+"/n"));
        output.append("/n");
        return output.toString() + "Введите номера правильных ответов";
    }
    int[] rightAnswerNumber;
    String[] rightAnswers;
    public void setQuestion(String[] questionsText)
    {
        int s = 1;
        type = questionType.multipleAnswerQuestion;
        question = questionsText[s];
        s++;
        int rightAnswerNumber = Integer.parseInt(questionsText[s]);
        s++;
        int answerNumber = Integer.parseInt(questionsText[s]);
        s++;
        String[] newAnswers = new String[answerNumber];
        List<String> newRightAnswers = new ArrayList();
        for(int i = 0; i < answerNumber; i++)
        {
            newAnswers[i] = questionsText[s];
            if(i+1 <= rightAnswerNumber)
            {
                newRightAnswers.add(questionsText[s]);
            }
            s++;
        }
        possibleAnswers = newAnswers;
        rightAnswers = newRightAnswers.toArray(String[]::new);
    }

    public boolean checkAnswer(String userAnswer) throws SignatureException
    {
        String[] parsedUserAnswer = userAnswer.split(",");
        for(int i = 0; i< parsedUserAnswer.length; i++)
        {
            if(!Utility.isNumeric(parsedUserAnswer[i]))
            {
                throw new SignatureException();
            }
        }
        for(int i = 0; i< parsedUserAnswer.length; i++)
        {
            int I = i;
            if(!IntStream.of(rightAnswerNumber).anyMatch(c -> c ==Integer.parseInt(parsedUserAnswer[I])))
           {
               return false;
           }
        }
        return true;
    }
}
