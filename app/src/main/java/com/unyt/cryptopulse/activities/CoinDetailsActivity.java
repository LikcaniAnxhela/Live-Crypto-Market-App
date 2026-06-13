package com.unyt.cryptopulse.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.database.AppDatabase;
import com.unyt.cryptopulse.database.FavoriteCoin;
import com.unyt.cryptopulse.models.Coin;
import com.unyt.cryptopulse.views.MiniChartView;

import java.util.Locale;

public class CoinDetailsActivity extends AppCompatActivity {

    private Coin coin;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_coin_details);

        coin = (Coin) getIntent().getSerializableExtra("coin");

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailName = findViewById(R.id.detailName);
        TextView detailPrice = findViewById(R.id.detailPrice);
        TextView detailInfo = findViewById(R.id.detailInfo);

        MiniChartView detailChart = findViewById(R.id.detailChart);

        Button saveButton = findViewById(R.id.saveButton);
        Button shareButton = findViewById(R.id.shareButton);
        Button browserButton = findViewById(R.id.browserButton);
        Button backButton = findViewById(R.id.backButton);

        if (coin == null) {
            finish();
            return;
        }

        backButton.setOnClickListener(v -> finish());

        Glide.with(this)
                .load(coin.image)
                .into(detailImage);

        detailName.setText(coin.name + " (" + coin.symbol.toUpperCase() + ")");

        detailPrice.setText("$" + String.format(Locale.US, "%.2f", coin.currentPrice));

        detailChart.setDemoValues(coin.currentPrice + Math.abs(coin.priceChange24h));

        detailInfo.setText(
                "Market cap rank: #" + coin.marketCapRank +
                        "\n24h movement: " + String.format(Locale.US, "%.2f%%", coin.priceChange24h) +
                        "\nMarket cap: $" + coin.marketCap +
                        "\n\nThe chart is a visual demo trend based on the selected coin value, while the price and market values come from the external CoinGecko API."
        );

        saveButton.setOnClickListener(v -> {
            FavoriteCoin favoriteCoin = new FavoriteCoin(
                    coin.id,
                    coin.name,
                    coin.symbol,
                    coin.image,
                    coin.currentPrice,
                    coin.priceChange24h
            );

            AppDatabase.getInstance(this)
                    .favoriteDao()
                    .insert(favoriteCoin);

            Toast.makeText(this, "Saved to favorites", Toast.LENGTH_SHORT).show();
        });

        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Check " + coin.name +
                            " on CryptoPulse. Price: $" + coin.currentPrice +
                            " | 24h: " + coin.priceChange24h + "%"
            );

            startActivity(Intent.createChooser(shareIntent, "Share coin"));
        });

        browserButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.coingecko.com/en/coins/" + coin.id)
            );

            startActivity(browserIntent);
        });
    }
}