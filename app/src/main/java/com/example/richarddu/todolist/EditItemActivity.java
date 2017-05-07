package com.example.richarddu.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String todoListItem = getIntent().getStringExtra("todoListItem");
        etEditItem.setText("");
        etEditItem.append(todoListItem);
        etEditItem.requestFocus();
    }

    public void onSave(View view) {
        // Closes activity and returns to first screen
        Intent data = new Intent();
        data.putExtra("updatedListItem", etEditItem.getText().toString());
        data.putExtra("position", getIntent().getIntExtra("position", -1));
        setResult(RESULT_OK, data);
        this.finish();
    }
}
