package com.unyt.cryptopulse.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteCoin coin);

    @Delete
    void delete(FavoriteCoin coin);

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    List<FavoriteCoin> getAll();

    @Query("SELECT COUNT(*) FROM favorites WHERE id = :id")
    int exists(String id);
}