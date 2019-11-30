package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

class QuestionBuilder {
    static String type = new String();
    static String question = new String();
    static ArrayList<String> answers = new ArrayList<String>();

    public static String build() {
        String s =  type + "\n" + question + "\n";
        for (int i = 0; i < answers.size(); ++i)
            s += answers.get(i) + "\n";
        return s;
    }

    public static void add() throws IOException {
        Files.write(Paths.get(Config.path), build().getBytes(), StandardOpenOption.APPEND);
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

    private static int creationStep = 0;
    private static QuestionBuilder question;

    Editor() {
        running = false;
        state = State.MENU;
    }

    public static Output parseData(String s) throws IOException {
        Output output = new Output();
        switch (state) {
            case MENU:
                switch (s) {
                    case "Создать":
                        state = State.TYPE_OF_QUESTION;
                        creationStep = 0;
                        break;
                    case "Список":
                        state = State.QUESTION_LIST;
                        break;
                    case "Выход":
                        state = State.NONE;
                        break;
                    default:
                        output.text = "Неизвестная команда\nИспользуйте кнопки\n";
                }
                break;
            case TYPE_OF_QUESTION: //{"Один вариант", "Несколько вариантов", "Слово"}
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
                   default:
                       output.text = "Неизвестная команда\nИспользуйте кнопки\n";
               }
               break;
            case CREATION_OAQ:
                output.text += creationOaq(s);
                break;
            case PREVIEW:
                switch (s) {
                    case "Создать":
                        QuestionBuilder.add();
                        output.text = "Вопрос добавлен";
                        break;
                    case "Отменить":
                        state = State.MENU;
                        output.text = "Создание вопроса отменено";
                        break;
                    default:
                        output.text = "Неизвестная команда\nИспользуйте кнопки\n";
                }
                break;
            case QUESTION_LIST:
                break;
        }

        switch (state) {
            case MENU:
                output.text += "Добро пожаловать в редактор";
                output.possibleAnswers = new String[]{"Создать", "Список", "Выйти"};
                break;
            case TYPE_OF_QUESTION:
                output.text += "Выберите тип вопроса.\n" +
                        "Один вариант - тестовый вопрос с одним верным вариантом ответа\n" +
                        "Несколько вариантов - тестовый вопрос с, возможно, несколькими правильными вариантами ответа\n" +
                        "Слово - вопрос, ответить на который необходимо вручную\n";
                output.possibleAnswers = new String[]{"Один вариант", "Несколько вариантов", "Слово"};
                break;
            case CREATION_OAQ:
                output.possibleAnswers = new String[]{"Готово"};
            case PREVIEW:
                output.text += "Созданный вами вопрос\n" + question.build();
                output.possibleAnswers = new String[]{"Создать", "Отменить"};
                break;
            case QUESTION_LIST:
                output.text += "Вот список вопросов\n";
                break;
        }
        return output;
    }

    private static String creationOaq(String text) {
        if (text == "Готово") {
            switch (creationStep) {
                case 0:
                    return "Вы не задали вопрос";
                case 1:
                    return "Вы не задали правильный ответ";
                case 2:
                    return "Вы не задали ни одного неверного ответа";
                default:
                    state = State.PREVIEW;
                    break;
            }
        }

        creationStep++;
        if (creationStep == 1) {
            question.question = text;
            return "Введите **правильный** ответ";
        }
        else {
            question.answers.add(text);
            String message = "Введите **ложный** вариант ответа";
            if (creationStep > 2)
                message += "\nВы можете завершить создание вопроса, нажав готово";
            return message;
        }
    }
}