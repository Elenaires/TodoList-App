package com.example.mytodolist;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * A repository manages query threads that allows us to use multiple backends
 * implements the logic for deciding whether to fetch data from a network or use results
 * cached in the local database
 * handles data operations, provides a clean API to the rest of the app for app data
 * not part of the architecture components libraries but is suggested best practice for code
 * separation and architecture
 */

public class TodoRepository {
    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllTodos;

    TodoRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodos = mTodoDao.getAllTodos();
    }

    /* insert a single todo item */
    private static class insertAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;

        insertAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /* delete all todo items */
    private static class deleteAllTodosAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoDao mAsyncTaskDao;

        deleteAllTodosAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /* delete single todo item */
    private static class deleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;

        deleteTodoAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.deleteTodo(params[0]);
            return null;
        }
    }

    /* update a todo */
    private static class updateTodoAsynTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;

        updateTodoAsynTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(Todo todo) {
        new insertAsyncTask(mTodoDao).execute(todo);
    }

    public void deleteAll() {
        new deleteAllTodosAsyncTask(mTodoDao).execute();
    }

    public void deleteTodo(Todo todo) {
        new deleteTodoAsyncTask(mTodoDao).execute(todo);
    }

    // update todo
    public void update(Todo todo) {
        new updateTodoAsynTask(mTodoDao).execute(todo);
    }
}
