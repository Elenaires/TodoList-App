package com.example.mytodolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * The adapter caches data and populates the RecyclerView with it.
 * The inner class WordViewHolder holds and manages a view for one list item.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {
    private final LayoutInflater mInflater;
    private List<Todo> mTodos; // Cached copy of words
    private static ClickListener clickListener;

    TodoListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    // holds and manages a view for one list item
    class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TextView todoItemView;

        private TodoViewHolder(View itemView) {
            super(itemView);
            todoItemView = itemView.findViewById(R.id.textView);

            // this part is needed for upadating todo
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        if(mTodos != null) {
            Todo current = mTodos.get(position);
            holder.todoItemView.setText(current.getTodo());
        }
        else {
            // covers the case of data not being ready yet
            holder.todoItemView.setText("No Word");
        }
    }

    void setTodos(List<Todo> todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    // getItemCount is called many times, and when it is first called,
    // mTodos has not been updated (initially it's null and cant return null)
    @Override
    public int getItemCount() {
        if(mTodos != null) {
            return mTodos.size();
        }
        else {
            return 0;
        }
    }

    public Todo getTodoAtPosition(int position) {
        return mTodos.get(position);
    }

    // for editing todo
    public void setOnItemClickListener(ClickListener clickListener) {
        TodoListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
