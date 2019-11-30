package com.company;

import javafx.animation.Animation;
import org.apache.commons.io.output.BrokenOutputStream;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.util.*;
import java.io.FileNotFoundException;

public class Dialogue {
    private static Map<String, Command> commandsList = new HashMap<>();
    private Quiz quiz = new Quiz();
    private Editor editor = new Editor();
    private String user = "";

    String getCommandName(String[] command) {
        return command[0];
    }

    String[] parseInput(String input) {
        return input.substring(1).split(" ");
    }

    String[] getCommandInput(String[] command) {
        String[] commandInput = Arrays.copyOfRange(command, 1, command.length);
        if (commandInput.length == 0) {
            return null;
        } else {
            return commandInput;
        }
    }

    Output returnQuizAnswer(String input) throws IOException {
        if (input.length() >= 1 && input.substring(0, 1).equals("/")) {
            String[] newInput = parseInput(input);
            String commandName = getCommandName(newInput);
            String[] commandInput = getCommandInput(newInput);
            if (commandsList.containsKey(commandName)) {
                try {
                    return commandsList.get(commandName).startProcess(commandInput).setButtons(quiz);
                } catch (Exception y) {
                    return new Output(y.getMessage()).setButtons(quiz);
                }
            } else {
                return new Output("Такой команды нет").setButtons(quiz);
            }
        } else {
            if (quiz.quizWantsAnswer)
                return checkAnswer(input).setButtons(quiz);
        }
        return new Output().setButtons(quiz);
    }

    public Dialogue(String userData) throws Exception {

        quiz.modifyQuestionsList(Config.path);
        user = userData;
        commandsList.put("help", new CommandWithPossibleInput("выводит список команд") {
            @Override
            public Output process(String[] args) {
                return help(args);
            }
        });
        commandsList.put("question", new CommandWithoutInput("задает новый вопрос") {
            @Override
            public Output process() {
                return getNewQuestion();
            }
        });
        commandsList.put("restart", new CommandWithoutInput("начинает новую игру") {
            @Override
            public Output process() {
                return restart();
            }
        });
        commandsList.put("editor", new CommandWithoutInput("открывает редактор вопросов") {
            @Override
            public Output process() {
                return callEditor();
            }
        });
        commandsList.put("shutdown", new CommandWithoutInput("закрывает программу") {
            @Override
            public Output process() {
                return close();
            }
        });
        commandsList.put("repeat", new CommandWithoutInput("повторяет текущий вопрос") {
            @Override
            public Output process() {
                return repeatQuestion();
            }
        });
        commandsList.put("shuffle", new CommandWithoutInput("перемешивает вопросы") {
            @Override
            public Output process() {
                return shuffleQuestions();
            }
        });
        commandsList.put("score", new CommandWithoutInput("печатает результат") {
            @Override
            public Output process() {
                return getScore();
            }
        });
        commandsList.put("opme", new CommandWithInput("выдаёт права администратора текущему пользователю, если был подан правильный ключ") {
            @Override
            public Output process(String args[]) throws FileNotFoundException {
                return opme(args[0]);
            }
        });
        commandsList.put("op", new CommandWithInput("выдаёт права администратора пользователю, принимает как аргумент имя пользователя") {
            @Override
            public Output process(String args[]) throws FileNotFoundException {
                return op(args[0]);
            }
        });

        commandsList.put("deop", new CommandWithInput("забирает права у пользователя, принимает как аргумент имя пользователя") {
            @Override
            public Output process(String args[]) throws FileNotFoundException {
                return deop(args[0]);
            }
        });
    }

    private Output checkAnswer(String ipnut) {
        return new Output(quiz.checkAnswer(ipnut));
    }

    public Quiz getQuiz() {
        return quiz;
    }

    private Output close() {
        System.exit(0);
        return new Output();
    }

    Output addQuestions(String path) throws Exception {
        try {
            quiz.modifyQuestionsList(path);
        } catch (java.nio.file.NoSuchFileException wjr) {
            return new Output("Такого файла нет");
        }
        return new Output();
    }

    private Output shuffleQuestions() {
        quiz.shuffleQuestions();
        return new Output();
    }

    private Output op(String name) throws FileNotFoundException {
        if (Config.admins.contains(user))
            return new Output(Config.addAdmin(name));
        else
            return new Output("Get admin rights first. Use /opme key");
    }

    private Output opme(String key) throws FileNotFoundException {
        if (key.equals(Config.key))
            return new Output(Config.addAdmin(user));
        else
            return new Output("Wrong key");
    }
    private Output deop(String name) throws FileNotFoundException {
        if (Config.admins.contains(user))
            return new Output(Config.deleteAdmin(name));
        else
            return new Output("Get admin rights first. Use /opme key");
    }

    private Output callEditor() {
        if (!editor.running) {
            if (!quiz.quizWantsAnswer) {
                editor.running = true;
                return new Output("Редактор НИХУЯ НЕ ОТКРЫТ ПУШТО НАДО ПРЯМО ВЫЗЫВАТЬ");
            } else
                return new Output("Для начала ответьте на вопрос");
        } else
            return new Output("Редактор уже открыт");
    }

    private Output closeEditor() {
        if (editor.running) {
            editor = new Editor();
            return new Output("Редактор закрыт");
        } else
            return new Output("Редактор не открыт");
    }

    private Output getScore() {
        return new Output(quiz.getScore());
    }

    private Output help(String... input)
    {
        if (input != null)
        {
            if (commandsList.containsKey(input[0]))
            {
                return new Output("Команда" + " " + input[0] + " " + commandsList.get(input[0]).description);
            } else
            {
                return new Output("Такой команды нет");
            }
        } else
            {
            StringBuilder stringBuilder = new StringBuilder("");
            commandsList.forEach((x, c) -> stringBuilder.append("Команда ").append(x).append(" - ").append(c.description).append("\n"));
            return new Output(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
    }

    private Output restart() {
        quiz.clearScore();
        return new Output();
    }

    private Output commandHelp(String input)
    {
        if (commandsList.containsKey(input)) {
            return new Output("Команда" + " " + input + " " + commandsList.get(input).description);
        } else {
            return new Output("Такой команды нет");
        }
    }

    private Output getNewQuestion()
    {
        if (quiz.quizWantsAnswer)
        {
            return new Output("Вы ещё не ответили на предыдуший вопрос");
        } else if (editor.running) {
            return new Output("Для начала выйдите из редактора");
        } else
        {
            return new Output(quiz.getNewQuestion());
        }
    }

    private Output repeatQuestion()
    {
        return new Output(quiz.repeatQuestion());
    }
}

abstract class CommandWithPossibleInput extends Command
{
    protected abstract Output process(String[] args) throws Exception;
    CommandWithPossibleInput(String description)
    {
        super(description);
    }

    public Output startProcess(String[] args) throws Exception
    {
        return process(args);
    }
}

abstract class Command
{
    String description;
    public Command(String description)
    {
        this.description = description;
    }
    public abstract Output startProcess(String[] args) throws Exception;
}

abstract class CommandWithoutInput extends Command
{
    protected abstract Output process() throws Exception;
    CommandWithoutInput(String description)
    {
        super(description);
    }
    public Output startProcess(String[] args) throws Exception
    {
        if (args != null) {
            throw new Exception("Эта команда аргумента не имеет");
        }
        return process();
    }
}

abstract class CommandWithInput extends Command {
    protected abstract Output process(String[] args) throws Exception;

    CommandWithInput(String description) {
        super(description);
    }

    public Output startProcess(String[] args) throws Exception {
        if (args == null) {
            throw new Exception("Этой команде нужен аргумент");
        }
        return process(args);
    }
}


