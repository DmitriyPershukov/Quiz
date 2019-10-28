package com.company;

import java.nio.file.Paths;
import java.nio.file.Files;

public class Config
{
    public static String path = "Supply/questions.txt";
    public static String configPath = "Supply/localConfig.txt";
    public static String botToken = "";
    public static String botName = "";
    static
    {
        String[] config = new String[0];
        try
        {
            config = Files.readString(Paths.get(configPath)).split("\r\n");
        }
        catch (Exception tro)
        {

        }
        for(String configString: config)
        {
            if(configString.split(" ")[0].equals("botName"))
            {
                botName = configString.split(" ")[1];
            }
            if(configString.split(" ")[0].equals("botToken"))
            {
                botToken = configString.split(" ")[1];
            }
        }
    }
}
