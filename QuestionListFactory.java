package com.company;

import javax.ws.rs.NotSupportedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.company.Quiz.splitByMarks;

public class QuestionListFactory
{
    public static List<Question> questionsList = new ArrayList<Question>();
    public static void modifyQuestionsList() throws Exception
    {
        questionsList = new ArrayList<Question>();
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
}
