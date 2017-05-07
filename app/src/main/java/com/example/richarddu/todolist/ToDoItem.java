package com.example.richarddu.todolist;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Richard Du on 5/6/2017.
 */

public class ToDoItem implements Serializable, Comparator<ToDoItem> {
    public String text;
    public String id;
    public int priority = 0;

    @Override
    public String toString()
    {
        return text;
    }

    @Override
    public int compare(ToDoItem o1, ToDoItem o2) {
        if (o1.priority > o2.priority)
            return 1;
        else
            return 0;
    }
}
