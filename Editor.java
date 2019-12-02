package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

class QuestionBuilder {
    static String type = null;
    static String question = null;
    static ArrayList<String> answers = new ArrayList<String>();

    public static String build() {
        String s = type + "\r\n" + question + "\r\n" + String.valueOf(answers.size());
        for (int i = 0; i < answers.size(); ++i)
            s += "\r\n" + answers.get(i);
        return s;
    }

    public static void add() throws Exception {
        Files.write(Paths.get(Config.path), ("\r\n" + build()).getBytes(), StandardOpenOption.APPEND);
        QuestionListFactory.modifyQuestionsList();
    }
}

class OAQuestionBuilder extends QuestionBuilder {
    OAQuestionBuilder() {
        type = "oaq";
    }
}

public class Editor {
    private enum State {
        MENU,
        TYPE_OF_QUESTION,
        CREATION_OAQ,
        PREVIEW,
        QUESTION_LIST,
        NONE
    }

    public static boolean running = false;
    private static State state;

    private static QuestionBuilder question;

    Editor() {
        running = false;
        state = State.NONE;
    }

    public static Output parseData(String s) throws Exception {
        Output output = new Output("");
        switch (state) {
            case NONE:
            default:
                state = State.MENU;
                break;
            case MENU:
                switch (s) {
                    case "Создать":
                        state = State.TYPE_OF_QUESTION;
                        break;
                    case "Список":
                        state = State.QUESTION_LIST;
                        break;
                    case "Выход":
                        state = State.NONE;
                        running = false;
                        break;
                }
                break;
            case TYPE_OF_QUESTION:
               switch (s) {
                   case "Один вариант":
                       state = State.CREATION_OAQ;
                       question = new OAQuestionBuilder();
                       output.text = "Введите вопрос";
                       break;
                   case "Несколько вариантов":
                       state = State.MENU;
                       output.text = "Несколько вариантов для ответа еще не реализованы :(\n";
                       break;
                   case "Слово":
                       state = State.MENU;
                       output.text = "Ответ словом еще не реализован :(\n";
                       break;
                   case "Назад":
                       state = State.MENU;
                       break;
                   default:
                       output.text = "Неизвестная команда\nИспользуйте кнопки\n";
               }
               break;
            case CREATION_OAQ:
                output.text = creationOaq(s);
                break;
            case PREVIEW:
                switch (s) {
                    case "Создать":
                        state = State.MENU;
                        QuestionBuilder.add();
                        output.text = "Вопрос добавлен\n";
                        break;
                    case "Отменить":
                        state = State.MENU;
                        output.text = "Создание вопроса отменено\n";
                        break;
                    default:
                        output.text = "Неизвестная команда\nИспользуйте кнопки\n";
                }
                break;
            case QUESTION_LIST:
                switch (s) {
                    case "Назад":
                        state = State.MENU;
                        break;
                    default:
                        output.text = "Неизвестная команда\nИспользуйте кнопки\n";
                }
                break;
        }

        switch (state) {
            case MENU:
                output.text += "Добро пожаловать в редактор\nДля управления используйте кнопки\n";
                output.possibleAnswers = new String[]{"Создать", "Список", "Выход"};
                break;
            case TYPE_OF_QUESTION:
                output.text += "Выберите тип вопроса.\n" +
                        "Один вариант - тестовый вопрос с одним верным вариантом ответа\n" +
                        "Несколько вариантов - тестовый вопрос с, возможно, несколькими правильными вариантами ответа\n" +
                        "Слово - вопрос, ответить на который необходимо вручную\n";
                output.possibleAnswers = new String[]{"Один вариант", "Несколько вариантов", "Слово", "Назад"};
                break;
            case CREATION_OAQ:
                output.possibleAnswers = new String[]{"Готово"};
                break;
            case PREVIEW:
                output.text += "Созданный вами вопрос\n```\n" + question.build() + "\n```";
                output.possibleAnswers = new String[]{"Создать", "Отменить"};
                break;
            case QUESTION_LIST:
                output.text = "Вот список вопросов\n" + showQuestions();
                output.possibleAnswers = new String[]{"Назад"};
                break;
        }
        return output;
    }

    private static String showQuestions() {
        ArrayList<Question> ar = (ArrayList<Question>) QuestionListFactory.questionsList;
        String s = "";
        for (int i = 0; i < ar.size(); ++i)
            s += String.valueOf(i + 1) + ". ```\n" + ar.get(i).getQuestion() + "\n```\n";
        return s;
    }

    private static String creationOaq(String text) {
        if (text.equals("Готово")) {
            if (question.question == null)
                return "Вы не задали вопрос";
            else if (question.answers.size() == 0)
                return "Вы не задали правильный ответ";
            else if (question.answers.size() == 1)
                return "Вы не задали ни одного неверного ответа";
            else {
                state = State.PREVIEW;
                return "Окей\n";
            }
        }

        if (question.question == null) {
            question.question = text;
            return "Введите **правильный** ответ";
        }
        else {
            question.answers.add(text);
            String message = "Введите **дополнительный** вариант ответа";
            if (question.answers.size() > 1)
                message += "\nВы можете завершить создание вопроса, нажав готово\n";
            return message;
        }
    }
}