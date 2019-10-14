package com.company;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class Dialogue
{
    private static Map<String, Command> commandsList = new HashMap<>();
    private Quiz quiz = new Quiz();

    String returnQuizAnswer(String input)
    {
        String output = "";
        if(input.length() >= 1 && input.substring(0,1).equals("&")) {
            String newInput = input.substring(2);
            String commandName = newInput.split(" ")[0];
            String[] commandInput = null;
            if (newInput.split(" ").length > 1){
                commandInput = new String[]{newInput.split(" ")[1]};
            }
            if(commandsList.containsKey(commandName))
            {
                try{
                    return commandsList.get(commandName).startProcess(commandInput);
                }
                catch (Exception y){
                    return y.getMessage();
                }
            }
            else
            {
                return "Такой команды нет";
            }
        }
        else
        {
            if(quiz.quizWantsAnswer)
            {
                return checkAnswer(input);
            }
        }
        return "";
    }

    public Dialogue() throws Exception {
        quiz.modifyQuestionsList(Config.path);
        commandsList.put("help", new CommandWithoutInput("выводит список команд") {
            @Override
            public String process(String[] args) {
                return help();
            }
        });
        commandsList.put("questionPlease", new CommandWithoutInput("задает новый вопрос") {
            @Override
            public String process(String[] args) {
                return getNewQuestion();
            }
        });
        commandsList.put("restart", new CommandWithoutInput("начинает новую игру") {
            @Override
            public String process(String[] args) {
                return restart();
            }
        });
        commandsList.put("shuffleQuestions", new CommandWithoutInput("перемешивает вопросы") {
            @Override
            public String process(String[] args) {
                return shuffleQuestions();
            }
        });
        commandsList.put("closeProgramm", new CommandWithoutInput("закрывает программу") {
            @Override
            public String process(String[] args) {
                return close();
            }
        });
        commandsList.put("getScore", new CommandWithoutInput("печатает результат") {
            @Override
            public String process(String[] args) {
                return getScore();
            }
        });
        commandsList.put("repeatQuestion", new CommandWithoutInput("повторяет текущий вопрос") {
            @Override
            public String process(String[] args) {
                return repeatQuestion();
            }
        });
        commandsList.put("commandHelp", new CommandWithInput("отображает справку по одной команде") {
            @Override
            public String process(String[] args) {
                return commandHelp(args[0]);
            }
        });
        commandsList.put("addQuestions", new CommandWithInput("добавляет вопросы по указанному пути") {
            @Override
            public String process(String[] args) throws Exception {
                return addQuestions(args[0]);
            }
        });
    }

    private String checkAnswer(String ipnut)
    {
        return quiz.checkAnswer(ipnut);
    }

    public Quiz getQuiz()
    {
        return quiz;
    }

    private String close()
    {
        System.exit(0);
        return "";
    }

    String addQuestions(String path) throws Exception
    {
        try
        {
            quiz.modifyQuestionsList(path);
        }
        catch (java.nio.file.NoSuchFileException wjr)
        {
            return "Такого файла нет";
        }
        return "";
    }

    private String shuffleQuestions()
    {
        quiz.shuffleQuestions();
        return "";
    }

    private String getScore()
    {
        return quiz.getScore();
    }

    private String help()
    {
        StringBuilder stringBuilder = new StringBuilder("");
        commandsList.forEach((x, c) -> stringBuilder.append("Команда ").append(x).append(" - ").append(c.description).append("\n"));
        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

    private String restart()
    {
        quiz.clearScore();
        return "";
    }

    private String commandHelp(String input)
    {
        if (commandsList.containsKey(input))
        {
            return "Команда" + " " + input + " " + commandsList.get(input).description;
        }
        else
        {
            return "Такой команды нет";
        }
    }

    private String getNewQuestion()
    {
        if (quiz.quizWantsAnswer)
        {
            return "Вы ещё не ответили на предыдуший вопрос";
        }
        else
        {
            return quiz.getNewQuestion();
        }
    }
    private String repeatQuestion()
    {
        return quiz.repeatQuestion();
    }
}

abstract class Command
{
    String description;
    commandType type;

    public Command(String description)
    {
        this.description = description;
    }
    public abstract String process(String[] args) throws Exception;

    public abstract String startProcess(String[] args) throws Exception;

    enum commandType
    {
        CommandWithInput,
        CommandWithoutInput,
    }
}

abstract class CommandWithoutInput extends Command
{
    CommandWithoutInput(String description)
    {
        super(description);
        type = commandType.CommandWithoutInput;
    }

    public String startProcess(String[] args) throws Exception
    {
        if (args != null) {
            throw new Exception("Эта команда аргумента не имеет");
        }
        return process(args);
    }
}

abstract class CommandWithInput extends Command
{
    CommandWithInput(String description)
    {
        super(description);
        type = commandType.CommandWithInput;
    }

    public String startProcess(String[] args) throws Exception
    {
        if(args == null)
        {
            throw new Exception("Этой команде нужен аргумент");
        }
        return process(args);
    }
}

