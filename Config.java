package com.company;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;

public class Config
{
    public static String path = "Supply/questions.txt";
    public static String configPath = "Supply/localConfig.txt";
    public static String adminListPath = "Supply/adminList.txt";
    public static String botToken = "";
    public static String botName = "";
    public static String key = "";
    public static Set<String> admins = new HashSet<String>();
    public static void setConfig() throws IOException
    {
        Collections.addAll(admins, Files.readAllLines(Paths.get(adminListPath)).toArray(new String[0]));
        String[] config = Files.readAllLines(Paths.get(configPath)).toArray(new String[0]);
        Map<String, String> fieldsContain = new HashMap<>();
        for(String configString: config)
        {
            fieldsContain.put(configString.split(" ")[0], configString.split(" ")[1]);
        }
        botName = fieldsContain.get("botName");
        botToken = fieldsContain.get("botToken");
        key = fieldsContain.get("key");
    }

    private static void saveAdminList() throws FileNotFoundException {
        PrintWriter out = new PrintWriter(adminListPath);
        for (String i : admins)
            out.println(i);
        out.close();
    }

    public static String addAdmin(String user) throws FileNotFoundException {
        if (!admins.contains(user)) {
            admins.add(user);
            saveAdminList();
            return user + " added to admin list";
        }
        else
            return user + " already exist in admin list";
    }

    public static String deleteAdmin(String user) throws FileNotFoundException {
        if (admins.contains(user)) {
            admins.remove(user);
            saveAdminList();
            return user + " removed from admin list";
        }
        else
            return user + " not exist in admin list";
    }
}
