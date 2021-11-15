package com.example.myapplication;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id =:id")
    List<Task> loadAllByIds(Long[] id);


    @Insert
    void insertAll(Task... tasks);

}
