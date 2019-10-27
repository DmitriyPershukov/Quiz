package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalConfig
{
    public LocalConfig(String path)
    {
        Path localConfogPath = Paths.get(path);
        String localConfig = "";
        try
        {
            localConfig = Files.readString(localConfogPath);
        }
        catch (Exception kya) {
        };
        String[] configList = localConfig.split("\n");
        botToken = configList[0];
        botName = configList[1];
    }
    public static String botToken = "653958636:AAEPjcFM0AY0fVCloKRsinujpezFNzq3a1M";
    public static String botName = "oopDPxBdEbot";
}
