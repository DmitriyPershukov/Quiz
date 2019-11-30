package com.company;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import javax.ws.rs.NotSupportedException;

import org.telegram.telegrambots.meta.api.objects.Update;

public class User
{
    String userId;
    Dialogue dialogue;
    public void identifyUserIdByMessage(Message message)
    {
        userId = message.getFrom().getId().toString();
    }
    User(Message message)
    {
        identifyUserIdByMessage(message);
    }
}
