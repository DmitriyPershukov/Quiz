package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Console
{
    public static void startConsole() throws Exception {
        hello();
        Dialogue dialogue = new Dialogue();
        while (true)
        {
            Scanner textReceiver = new Scanner(System.in);
            String input = textReceiver.nextLine();
            String output = dialogue.returnQuizAnswer(input).text;
            if(!output.equals(""))
            {
                System.out.println(output);
            }
        }
    }

   public static void hello()
   {
       System.out.println("Приветсвую, это программа-викторина, которая может задавать вопросы.\n" +
               "Чтобы увидеть список команд введи команду \"& help\"");
   }

}


