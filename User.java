package com.company;

import com.google.inject.spi.Message;

import javax.ws.rs.NotSupportedException;

public class User
{
    String userId;
    public void identifyUserIdByMessage(Message message)
    {
        throw new NotSupportedException();
    }
}
