package com.example.richarddu.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;
    private ToDoItem todoListItem;
    private Spinner sPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        todoListItem = (ToDoItem)getIntent().getSerializableExtra("todoListItem");
        etEditItem.setText("");
        etEditItem.append(todoListItem.text);
        etEditItem.requestFocus();

        sPriority = (Spinner) findViewById(R.id.sPriority);
        String[] items = new String[]{"Low", "Medium", "High"};

        sPriority.setSelection(todoListItem.priority);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sPriority.setAdapter(adapter);
        sPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                todoListItem.priority = position;
                Log.d("HELLO", ""+todoListItem.priority);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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
