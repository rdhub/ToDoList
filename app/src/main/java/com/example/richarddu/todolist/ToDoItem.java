package com.example.richarddu.todolist;

import java.io.Serializable;

/**
 * Created by Richard Du on 5/6/2017.
 */

public class ToDoItem implements Serializable {
    public String text;
    public int priority;

    @Override
    public String toString()
    {
        return text;
    }
}
