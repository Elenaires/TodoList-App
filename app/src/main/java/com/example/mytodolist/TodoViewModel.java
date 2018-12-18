package com.example.mytodolist;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by core on 16/12/18.
 */

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository mRepository;
    private LiveData<List<Todo>> mAllTodos;

    public TodoViewModel (Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }

    LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(Todo todo) {
        mRepository.insert(todo);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteTodo(Todo todo) {
        mRepository.deleteTodo(todo);
    }

    // update todo
    public void update(Todo todo) {
        mRepository.update(todo);
    }
}
