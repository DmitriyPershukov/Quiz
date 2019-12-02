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
    Output (String... input)
    {
        if(input.length != 0)
            text = input[0];
    }
    public String text = null;
    public String[] possibleAnswers = null;
}
