package com.unyt.cryptopulse.api;

import com.unyt.cryptopulse.models.Coin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinGeckoApi {

    @GET("coins/markets")
    Call<List<Coin>> getMarkets(
            @Query("vs_currency") String currency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("sparkline") boolean sparkline
    );
}