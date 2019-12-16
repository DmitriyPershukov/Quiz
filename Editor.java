package com.company;

import com.google.inject.internal.cglib.core.$ClassInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class QuestionBuilder {
    String type = null;
    String question = null;
    ArrayList<String> answers = new ArrayList<String>();

    public String build() {
        String s = type + "\r\n" + question + "\r\n" + String.valueOf(answers.size());
        for (int i = 0; i < answers.size(); ++i)
            s += "\r\n" + answers.get(i);
        return s;
    }

    public void add() throws Exception {
        Files.write(Paths.get(Config.path), ("\r\n" + build()).getBytes(), StandardOpenOption.APPEND);
        QuestionListFactory.modifyQuestionsList();
    }
}

abstract class InputHandler
{
    abstract Output action(Editor editor, String input) throws Exception;
}

abstract class Action
{
    abstract Output action(Editor editor);
}

class OAQuestionBuilder extends QuestionBuilder
{
    OAQuestionBuilder() {
        type = "oaq";
    }
}

public class Editor {
    public enum State {
        MENU,
        TYPE_OF_QUESTION,
        CREATION_OAQ,
        PREVIEW,
        QUESTION_LIST,
    }

    public static Map<State, Action> actionsUponState= new HashMap<>();
    public static Map<State, InputHandler> inputHandlersUponState = new HashMap<>();
    static
    {
        actionsUponState.put(State.CREATION_OAQ, new Action() {
            @Override
            Output action(Editor editor) {
                Output output = new Output();
                return output;
            }
        });
        actionsUponState.put(State.PREVIEW, new Action() {
            @Override
            Output action(Editor editor) {
                Output output = new Output();
                output.text += "Созданный вами вопрос\n```\n" + editor.questionBuilder.build() + "\n```";
                output.possibleAnswers = new String[]{"Создать", "Отменить"};
                return output;
            }
        });
        actionsUponState.put(State.QUESTION_LIST, new Action() {
            @Override
            Output action(Editor editor) {
                Output output = new Output();
                output.text = "Вот список вопросов\n" + editor.showQuestions();
                output.possibleAnswers = new String[]{"Назад"};
                return output;
            }
        });
        actionsUponState.put(State.TYPE_OF_QUESTION, new Action() {
            @Override
            Output action(Editor editor)
            {
                Output output = new Output();
                output.text += "Выберите тип вопроса.\n" +
                        "Один вариант - тестовый вопрос с одним верным вариантом ответа\n" +
                        "Несколько вариантов - тестовый вопрос с, возможно, несколькими правильными вариантами ответа\n" +
                        "Слово - вопрос, ответить на который необходимо вручную\n";
                output.possibleAnswers = new String[]{"Один вариант", "Несколько вариантов", "Слово", "Назад"};
                return output;
            }
        });
        actionsUponState.put(State.MENU, new Action() {
            @Override
            Output action(Editor editor) {
                Output output = new Output();
                output.text += "Добро пожаловать в редактор\nДля управления используйте кнопки\n";
                output.possibleAnswers = new String[]{"Создать", "Список", "Выход"};
                return output;
            }
        });

        inputHandlersUponState.put(State.TYPE_OF_QUESTION, new InputHandler() {
            @Override
            protected Output action(Editor editor, String input)
            {
                switch (input) {
                    case "Один вариант":
                        editor.state = State.CREATION_OAQ;
                        editor.questionBuilder = new OAQuestionBuilder();
                        return new Output("Введите вопрос");
                    case "Несколько вариантов":
                        editor.state = State.MENU;
                        return new Output("Несколько вариантов для ответа еще не реализованы :(\n");
                    case "Слово":
                        editor.state = State.MENU;
                        return new Output("Ответ словом еще не реализован :(\n");
                    case "Назад":
                        editor.state = State.MENU;
                        break;
                    default:
                        return new Output("Неизвестная команда\nИспользуйте кнопки\n");
                }
                return new Output();
            }
        });
        inputHandlersUponState.put(State.MENU, new InputHandler() {
            @Override
            Output action(Editor editor, String input)
            {
                switch (input) {
                    case "Создать":
                        editor.state = State.TYPE_OF_QUESTION;
                        break;
                    case "Список":
                        editor.state = State.QUESTION_LIST;
                        break;
                    case "Выход":
                        editor.shutdownEditor();
                        break;
                }
                return new Output();
            }
        });
        inputHandlersUponState.put(State.CREATION_OAQ, new InputHandler() {
            @Override
            protected Output action(Editor editor, String input)
            {
                Output output = new Output();
                output.text = editor.creationOaq(input);
                return output;
            }
        });
        inputHandlersUponState.put(State.PREVIEW, new InputHandler() {
            @Override
            protected Output action(Editor editor, String input) throws Exception {
                switch (input)
                {
                    case "Создать":
                        editor.state = State.MENU;
                        editor.questionBuilder.add();
                        return new Output("Вопрос добавлен\n");
                    case "Отменить":
                        editor.state = State.MENU;
                        return new Output("Создание вопроса отменено\n");
                    default:
                        return new Output("Неизвестная команда\nИспользуйте кнопки\n");
                }
            }
        });
        inputHandlersUponState.put(State.QUESTION_LIST, new InputHandler() {
            @Override
            protected Output action(Editor editor, String input)
            {
                switch (input) {
                    case "Назад":
                        editor.state = State.MENU;
                        return new Output();
                    default:
                        return new Output("Неизвестная команда\nИспользуйте кнопки\n");
                }
            }
        });
    }

    public boolean running = false;

    public State state;

    private QuestionBuilder questionBuilder;

    Editor()
    {}
    public void shutdownEditor()
    {
        running = false;
    }

    public void startEditor()
    {
        running = true;
        state = State.MENU;
    }

    public Output parseData(String input) throws Exception {
        Output output = new Output("");
        output = Output.concatinateOutputs(output, inputHandlersUponState.get(state).action(this, input));
        if (running) output = Output.concatinateOutputs(output, actionsUponState.get(state).action(this));
        return output;
    }

    private String showQuestions()
    {
        ArrayList<Question> ar = (ArrayList<Question>) QuestionListFactory.questionsList;
        String s = "";
        for (int i = 0; i < ar.size(); ++i)
            s += String.valueOf(i + 1) + ". ```\n" + ar.get(i).getQuestion() + "\n```\n";
        return s;
    }

    public String creationOaq(String text)
    {
        if (text.equals("Готово"))
        {
            if (questionBuilder.question == null)
                return "Вы не задали вопрос";
            else if (questionBuilder.answers.size() == 0)
                return "Вы не задали правильный ответ";
            else if (questionBuilder.answers.size() == 1)
                return "Вы не задали ни одного неверного ответа";
            else {
                state = State.PREVIEW;
                return "Окей\n";
            }
        }
        if (text.equals("Отменить")) {
            state = State.MENU;
            return "Создание вопроса отменено\n";
        }

        if (questionBuilder.question == null) {
            questionBuilder.question = text;
            return "Введите **правильный** ответ";
        }
        else {
            questionBuilder.answers.add(text);
            String message = "Введите **дополнительный** вариант ответа";
            if (questionBuilder.answers.size() > 1)
                message += "\nВы можете завершить создание вопроса, нажав готово\n";
            return message;
        }
    }
}