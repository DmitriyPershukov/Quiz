package com.company;

public class Output
{
    public Output setButtons(Quiz quiz)
    {
        if (quiz.quizWantsAnswer && (quiz.getCurrentQuestion().getQuestionType().equals(Question.questionType.oneAnswerQuestion)))
        {
            this.possibleAnswers = ((OneAnswerQuestion)quiz.getCurrentQuestion()).getPossibleAnswersForButtons();
        }
        return this;
    }
    public static Output concatinateOutputs(Output output, Output newOutput)
    {
        if(output.text != null)
        {
            output.text += newOutput.text;
        }
        else
        {
            output.text = newOutput.text;
        }
        output.possibleAnswers = Utility.concatinateArrays(output.possibleAnswers, newOutput.possibleAnswers);
        return output;
    }

    Output (String... input)
    {
        if(input.length != 0)
            text = input[0];
    }
    public String text = null;
    public String[] possibleAnswers = null;
}
