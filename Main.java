package com.company;



public class Main
{
    public static void main(String[] args) throws Exception
    {
        QuestionListFactory.modifyQuestionsList();
        //Console.startConsole();
        Telegram.init();
    }
}
