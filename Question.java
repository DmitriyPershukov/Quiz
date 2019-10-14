package com.company;

import java.security.SignatureException;

public class Question
{
    public int setQuestion(String[] questionsText, int i)
    {
        return 3;
    }

    questionType type;

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

    public boolean checkAnswer(String userAnswer) throws SignatureException {
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


