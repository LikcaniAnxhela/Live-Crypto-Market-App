package com.unyt.cryptopulse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.database.AppDatabase;
import com.unyt.cryptopulse.database.FavoriteCoin;

import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    private List<FavoriteCoin> list;

    public FavoriteAdapter(List<FavoriteCoin> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_favorite_coin, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FavoriteCoin coin = list.get(position);

        holder.name.setText(coin.name);
        holder.symbol.setText(coin.symbol.toUpperCase());

        holder.price.setText(
                "$" + String.format(Locale.US, "%.2f", coin.price)
        );

        holder.change.setText(
                String.format(Locale.US, "%.2f%%", coin.change24h)
        );

        holder.change.setTextColor(
                coin.change24h >= 0 ? 0xff27E8A7 : 0xffff5c7a
        );

        holder.bar.setProgress(
                (int) Math.min(100, Math.abs(coin.change24h) * 8 + 8)
        );

        Glide.with(holder.itemView.getContext())
                .load(coin.image)
                .into(holder.img);

        holder.removeButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            FavoriteCoin selectedCoin = list.get(currentPosition);

            AppDatabase
                    .getInstance(holder.itemView.getContext())
                    .favoriteDao()
                    .delete(selectedCoin);

            list.remove(currentPosition);
            notifyItemRemoved(currentPosition);

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Removed from favorites",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        TextView symbol;
        TextView price;
        TextView change;
        ProgressBar bar;
        Button removeButton;

        Holder(View view) {
            super(view);

            img = view.findViewById(R.id.coinImage);
            name = view.findViewById(R.id.coinName);
            symbol = view.findViewById(R.id.coinSymbol);
            price = view.findViewById(R.id.coinPrice);
            change = view.findViewById(R.id.coinChange);
            bar = view.findViewById(R.id.coinBar);
            removeButton = view.findViewById(R.id.removeFavoriteButton);
        }
    }
}