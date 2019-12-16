package com.company;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditorTest
{
    @Test
    public void exitButton() throws Exception
    {
        Editor editor = new Editor();
        editor.startEditor();
        editor.parseData("");
        editor.parseData("Выход");
        Assert.assertEquals(editor.running, false);
    }
    @Test
    public void create() throws Exception
    {
        Editor editor = new Editor();
        editor.startEditor();
        editor.parseData("");
        editor.parseData("Создать");
        Assert.assertEquals(editor.state, Editor.State.TYPE_OF_QUESTION);
    }
    @Test
    public void creation() throws Exception
    {
        Editor editor = new Editor();
        editor.startEditor();
        editor.parseData("");
        editor.parseData("Создать");
        editor.parseData("Один вариант");
        Assert.assertEquals(editor.state, Editor.State.CREATION_OAQ);
    }
    @Test
    public void questionList() throws Exception
    {
        Editor editor = new Editor();
        editor.startEditor();
        editor.parseData("");
        editor.parseData("Список");
        Assert.assertEquals(editor.state, Editor.State.QUESTION_LIST);
    }
    @Test
    public void wordbuild() throws Exception
    {
        Editor editor = new Editor();
        editor.startEditor();
        editor.parseData("");
        editor.parseData("Создать");
        editor.parseData("Один вариант");

        editor.parseData("Question?");
        editor.parseData("Answer");
        editor.parseData("Wrong Answer");

        Output output = editor.parseData("Готово");
        Assert.assertEquals(output.text, "Окей\nСозданный вами вопрос\n```\noaq\r\nQuestion?\r\n2\r\nAnswer\r\nWrong Answer\n```");
        editor.parseData("Отменить");
        Assert.assertEquals(editor.state, Editor.State.MENU);
    }
}