package com.company;

public class Output
{
    public void setAnswers(Quiz quiz)
    {
        if (quiz.quizWantsAnswer && (quiz.getCurrentQuestion().getQuestionType().equals(Question.questionType.oneAnswerQuestion)))
        {
            this.possibleAnswers = ((OneAnswerQuestion)quiz.getCurrentQuestion()).getPossibleAnswersForButtons();
            this.wantsAnswers = quiz.quizWantsAnswer;
        }
    }
    public String text = "";
    public boolean wantsAnswers;
    public String[] possibleAnswers;
}
