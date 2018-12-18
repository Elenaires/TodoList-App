package com.example.mytodolist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodoViewModel mTodoViewModel;

    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_TODO_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_TODO = "com.example.android.MyTodoList.EXTRA_DATA_UPDATE_TODO";
    public static final String EXTRA_DATA_ID = "com.example.android.MyTodoList.EXTRA_DATA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
                startActivityForResult(intent, NEW_TODO_ACTIVITY_REQUEST_CODE);
            }
        });

        // add RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TodoListAdapter adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get ViewModel
        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        mTodoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(@Nullable final List<Todo> todos) {
                //update the cached copy of the todos in the adapter
                adapter.setTodos(todos);
            }
        });

        // add functionality to swipe items in the recycler view to delete item
        ItemTouchHelper helper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
            {
                @Override
                public boolean onMove(RecyclerView recyclerView,
                                      RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {
                    return false;
                }

                // delete todo when swiped
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // gets position of the viewholder that was swiped
                    int position = viewHolder.getAdapterPosition();

                    // based on position, get the todo displayed by the view holder
                    Todo myTodo = adapter.getTodoAtPosition(position);
                    Toast.makeText(MainActivity.this, "Deleting " +
                        myTodo.getTodo(), Toast.LENGTH_LONG).show();

                    // delete the todo
                    mTodoViewModel.deleteTodo(myTodo);
                }
            });

        // attached the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TodoListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Todo todo = adapter.getTodoAtPosition(position);
                //Toast.makeText(getBaseContext(), "item clicked", Toast.LENGTH_LONG).show();
                launchUpdateTodoActivity(todo);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_data) {
            // add a toast for confirmation
            Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show();

            //Delete the existing data
            mTodoViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Todo todo = new Todo(data.getStringExtra(NewTodoActivity.EXTRA_REPLY));
            mTodoViewModel.insert(todo);
        }
        else if (requestCode == UPDATE_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String todo_data = data.getStringExtra(NewTodoActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewTodoActivity.EXTRA_REPLY_ID, -1);

            if(id != -1) {
                mTodoViewModel.update(new Todo(id, todo_data));
            }
            else {
                Toast.makeText(this, R.string.unable_to_update, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public void launchUpdateTodoActivity(Todo todo) {
        Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_TODO, todo.getTodo());
        intent.putExtra(EXTRA_DATA_ID, todo.getId());
        startActivityForResult(intent, UPDATE_TODO_ACTIVITY_REQUEST_CODE);
    }
}
