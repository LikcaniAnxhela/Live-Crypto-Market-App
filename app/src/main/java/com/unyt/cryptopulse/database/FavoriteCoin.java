package com.unyt.cryptopulse.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteCoin {

    @PrimaryKey
    @NonNull
    public String id;

    public String name;
    public String symbol;
    public String image;

    public double price;
    public double change24h;

    public FavoriteCoin(
            @NonNull String id,
            String name,
            String symbol,
            String image,
            double price,
            double change24h
    ) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.image = image;
        this.price = price;
        this.change24h = change24h;
    }
}