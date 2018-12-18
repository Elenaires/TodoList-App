package com.example.mytodolist;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by core on 16/12/18.
 */

@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Todo todo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("SELECT * from todo_table ORDER BY id ASC")
    LiveData<List<Todo>> getAllTodos();

    // Room issues the database query when getAnyWord is called
    // returns an array containing one word
    @Query("SELECT * from todo_table LIMIT 1")
    Todo[] getAnyTodo();

    @Delete
    void deleteTodo(Todo todo);

    // to edit a todo
    @Update
    void update(Todo... todo);
}
