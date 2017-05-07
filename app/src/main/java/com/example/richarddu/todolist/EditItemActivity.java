package com.example.richarddu.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;
    private ToDoItem todoListItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        todoListItem = (ToDoItem)getIntent().getSerializableExtra("todoListItem");
        etEditItem.setText("");
        etEditItem.append(todoListItem.text);
        etEditItem.requestFocus();
    }

    public void onSave(View view) {
        // Closes activity and returns to first screen
        Intent data = new Intent();
        todoListItem.text = etEditItem.getText().toString();
        data.putExtra("updatedListItem", todoListItem);
        data.putExtra("position", getIntent().getIntExtra("position", -1));
        setResult(RESULT_OK, data);
        this.finish();
    }
}
