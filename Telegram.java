package com.company;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Telegram extends TelegramLongPollingBot {
    private static Dialogue dialogue;
    private static HashMap<String, Dialogue> chats;

    public static void init() throws Exception {
        ApiContextInitializer.init();
        chats = new HashMap<String, Dialogue>();
        TelegramBotsApi botApi = new TelegramBotsApi();
        //dialogue = new Dialogue();
        try {
            botApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String user = message.getFrom().toString();
        if (!chats.containsKey(user)) {
            try {
                chats.put(user, new Dialogue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String getString = "";
        try {
            getString = chats.get(user).returnQuizAnswer(message.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendMsg(message, getString);
    }

    private void sendMsg(Message msg, String s){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(msg.getChatId().toString());
        sendMessage.setReplyToMessageId(msg.getMessageId());
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return Config.botName;
    }

    @Override
    public String getBotToken() {
        return Config.botToken;
    }
}
