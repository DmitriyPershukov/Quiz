package com.company;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config
{
    public static String path = "Supply/questions.txt";
    public static String configPath = "Supply/localConfig.txt";
    public static String botToken = "";
    public static String botName = "";
    public static void setConfig() throws IOException
    {
        String[] config = Files.readAllLines(Paths.get(configPath)).toArray(new String[0]);
        Map<String, String> fieldsContain = new HashMap<>();
        for(String configString: config)
        {
            fieldsContain.put(configString.split(" ")[0], configString.split(" ")[1]);
        }
        botName = fieldsContain.get("botName");
        botToken = fieldsContain.get("botToken");
    }
}
