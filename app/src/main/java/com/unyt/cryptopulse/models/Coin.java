package com.unyt.cryptopulse.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coin implements Serializable {

    public String id;
    public String symbol;
    public String name;
    public String image;

    @SerializedName("current_price")
    public double currentPrice;

    @SerializedName("market_cap_rank")
    public int marketCapRank;

    @SerializedName("price_change_percentage_24h")
    public double priceChange24h;

    @SerializedName("market_cap")
    public long marketCap;
}