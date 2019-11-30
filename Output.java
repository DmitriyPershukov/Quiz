package com.company;

public class Output
{
    public Output setButtons(Quiz quiz)
    {
        if (quiz.quizWantsAnswer && (quiz.getCurrentQuestion().getQuestionType().equals(Question.questionType.oneAnswerQuestion)))
        {
            this.possibleAnswers = ((OneAnswerQuestion)quiz.getCurrentQuestion()).getPossibleAnswersForButtons();
            this.wantsAnswers = quiz.quizWantsAnswer;
        }
        return this;
    }
    Output (String... input)
    {
        if(input.length !=0)
        {
            text = input[0];
        }
    }
    public String text = "";
    public boolean wantsAnswers;
    public String[] possibleAnswers;
}
