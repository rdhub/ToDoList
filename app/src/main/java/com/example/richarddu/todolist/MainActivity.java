package com.example.richarddu.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ToDoItem> todoItems;
    private ArrayAdapter<ToDoItem> aToDoAdapter;
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
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                //writeItems();
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

            todoItems.set(position, updatedListItem);
            aToDoAdapter.notifyDataSetChanged();
            // Get singleton instance of database
            ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(this);
            databaseHelper.updateToDoItem(updatedListItem);
            //writeItems();
        }
    }

    public void populateArrayItems() {
        readItems();
        /*ArrayList<String> stringItems = new ArrayList<String>();
        for(ToDoItem item: todoItems) {
            stringItems.add(item.text);
        }*/
        //aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringItems);
        aToDoAdapter = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {
        todoItems = new ArrayList<ToDoItem>();
        // Get singleton instance of database
        ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(this);

        // Get all posts from database
        List<ToDoItem> items = databaseHelper.getAllItems();
        for (ToDoItem item: items) {
            // do something
            todoItems.add(item);
            //aToDoAdapter.notifyDataSetChanged();
        }

        /*
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            ArrayList<String> stringItems = new ArrayList<String>(FileUtils.readLines(file));
            for(String text: stringItems) {
                ToDoItem item = new ToDoItem();
                item.text = text;
                todoItems.add(item);
            }
        } catch (IOException e) {

        }*/
    }

    /*private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {

            ArrayList<String> stringItems = new ArrayList<String>();
            for(ToDoItem item: todoItems) {
                stringItems.add(item.text);
            }
            FileUtils.writeLines(file, stringItems);
        } catch (IOException e) {

        }
    }*/
    public void onAddItem(View view) {
        // Create sample data
        ToDoItem newItem = new ToDoItem();
        newItem.text = etEditText.getText().toString();

        // Get singleton instance of database
        ToDoItemDatabase databaseHelper = ToDoItemDatabase.getInstance(this);
        // Add sample post to the database
        databaseHelper.addItem(newItem);
        //databaseHelper.deleteAllItems();


        //aToDoAdapter.add(etEditText.getText().toString());
        aToDoAdapter.add(newItem);
        etEditText.setText("");
        //writeItems();
    }
}
