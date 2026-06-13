package com.unyt.cryptopulse.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    public String email;

    public String name;
    public String password;

    public User(@NonNull String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}