package com.example.richarddu.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ToDoItem> todoItems;
    //private ArrayAdapter<ToDoItem> aToDoAdapter;
    private ItemsAdapter aToDoAdapter;
    private ListView lvItems;
    private EditText etEditText;

    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                i.putExtra("todoListItem", todoItems.get(position));
                i.putExtra("position", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(MainActivity.this);

                databaseHelper.deleteItem(todoItems.get(position));
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // Time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract updated list value from result extras
            ToDoItem updatedListItem = (ToDoItem)data.getSerializableExtra("updatedListItem");
            int position = data.getIntExtra("position", -1);

            Log.d("Spinner", ""+updatedListItem.priority);
            todoItems.set(position, updatedListItem);
            aToDoAdapter.notifyDataSetChanged();
            // Get singleton instance of database
            ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(this);
            Collections.sort(todoItems, new ToDoItem());
            databaseHelper.updateToDoItem(updatedListItem);

        }
    }

    public void populateArrayItems() {
        readItems();

        aToDoAdapter= new ItemsAdapter(this, todoItems);
        //aToDoAdapter = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {

        // Get singleton instance of database
        ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(this);

        // Get all posts from database
        todoItems = new ArrayList<ToDoItem>(databaseHelper.getAllItems());

        Collections.sort(todoItems, new ToDoItem());
    }

    public void onAddItem(View view) {
        // Create sample data
        ToDoItem newItem = new ToDoItem();
        newItem.text = etEditText.getText().toString();
        newItem.id = "" + Math.random()*1000000;

        // Get singleton instance of database
        ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(this);
        // Add sample post to the database
        databaseHelper.addItem(newItem);

        aToDoAdapter.add(newItem);
        etEditText.setText("");

    }
}
