package com.unyt.cryptopulse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.models.Coin;

import java.util.List;
import java.util.Locale;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.Holder> {

    public interface OnClick {
        void onCoinClick(Coin coin);
    }

    private List<Coin> coins;
    private OnClick listener;

    public CoinAdapter(List<Coin> coins, OnClick listener) {
        this.coins = coins;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_coin, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Coin coin = coins.get(position);

        holder.name.setText(coin.name);

        holder.symbol.setText(
                coin.symbol.toUpperCase() +
                        " • Rank #" + coin.marketCapRank +
                        " • Cap $" + shortNumber(coin.marketCap)
        );

        holder.price.setText(
                "$" + String.format(Locale.US, "%.2f", coin.currentPrice)
        );

        holder.change.setText(
                (coin.priceChange24h >= 0 ? "▲ " : "▼ ") +
                        String.format(Locale.US, "%.2f%%", Math.abs(coin.priceChange24h))
        );

        holder.change.setTextColor(
                coin.priceChange24h >= 0 ? 0xff27E8A7 : 0xffff5c7a
        );

        int progressValue = (int) Math.min(
                100,
                Math.abs(coin.priceChange24h) * 8 + 8
        );

        holder.bar.setProgress(progressValue);

        Glide.with(holder.itemView.getContext())
                .load(coin.image)
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> listener.onCoinClick(coin));
    }

    private String shortNumber(long number) {
        if (number > 1000000000L) {
            return String.format(Locale.US, "%.1fB", number / 1000000000.0);
        }

        if (number > 1000000L) {
            return String.format(Locale.US, "%.1fM", number / 1000000.0);
        }

        return String.valueOf(number);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        TextView symbol;
        TextView price;
        TextView change;
        ProgressBar bar;

        Holder(View view) {
            super(view);

            img = view.findViewById(R.id.coinImage);
            name = view.findViewById(R.id.coinName);
            symbol = view.findViewById(R.id.coinSymbol);
            price = view.findViewById(R.id.coinPrice);
            change = view.findViewById(R.id.coinChange);
            bar = view.findViewById(R.id.coinBar);
        }
    }
}