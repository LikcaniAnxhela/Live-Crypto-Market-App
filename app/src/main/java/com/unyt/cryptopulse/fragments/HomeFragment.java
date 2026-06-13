package com.unyt.cryptopulse.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.activities.CoinDetailsActivity;
import com.unyt.cryptopulse.adapters.CoinAdapter;
import com.unyt.cryptopulse.api.RetrofitClient;
import com.unyt.cryptopulse.models.Coin;
import com.unyt.cryptopulse.views.MiniChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recycler;
    private ProgressBar progress;
    private RadioButton usd;
    private RadioButton eur;
    private CheckBox topTen;
    private SeekBar seek;
    private TextView summary;
    private TextView filterLabel;
    private MiniChartView chart;
    private EditText searchInput;

    private List<Coin> all = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycler = view.findViewById(R.id.coinRecycler);
        progress = view.findViewById(R.id.progress);
        usd = view.findViewById(R.id.usdRadio);
        eur = view.findViewById(R.id.eurRadio);
        topTen = view.findViewById(R.id.topTenCheck);
        seek = view.findViewById(R.id.changeSeek);
        summary = view.findViewById(R.id.marketSummary);
        filterLabel = view.findViewById(R.id.filterLabel);
        chart = view.findViewById(R.id.marketChart);
        searchInput = view.findViewById(R.id.searchInput);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        usd.setOnClickListener(v -> loadCoins());
        eur.setOnClickListener(v -> loadCoins());

        topTen.setOnClickListener(v -> applyFilter());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                filterLabel.setText("Minimum 24h movement: " + progressValue + "%");
                applyFilter();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadCoins();

        return view;
    }

    private void loadCoins() {
        progress.setVisibility(View.VISIBLE);

        String currency = eur.isChecked() ? "eur" : "usd";

        RetrofitClient
                .getApi()
                .getMarkets(currency, "market_cap_desc", 60, 1, false)
                .enqueue(new Callback<List<Coin>>() {
                    @Override
                    public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                        progress.setVisibility(View.GONE);

                        if (response.body() != null) {
                            all = response.body();
                            updateSummary(currency);
                            applyFilter();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Coin>> call, Throwable t) {
                        progress.setVisibility(View.GONE);

                        Toast.makeText(
                                getContext(),
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void updateSummary(String currency) {
        if (all.isEmpty()) {
            return;
        }

        Coin bitcoin = all.get(0);

        double averageChange = 0;
        int gainers = 0;

        for (Coin coin : all) {
            averageChange += coin.priceChange24h;

            if (coin.priceChange24h >= 0) {
                gainers++;
            }
        }

        averageChange = averageChange / all.size();

        summary.setText(
                "Currency: " + currency.toUpperCase() +
                        "  •  Bitcoin: " + String.format(Locale.US, "%.2f", bitcoin.currentPrice) +
                        "  •  Avg 24h: " + String.format(Locale.US, "%.2f%%", averageChange) +
                        "  •  Gainers: " + gainers + "/" + all.size()
        );

        chart.setDemoValues(Math.abs(averageChange) + bitcoin.currentPrice % 100);
    }

    private void applyFilter() {
        if (recycler == null) {
            return;
        }

        int minimumMovement = seek.getProgress();

        String searchText = searchInput
                .getText()
                .toString()
                .trim()
                .toLowerCase();

        ArrayList<Coin> filtered = new ArrayList<>();

        for (Coin coin : all) {
            boolean matchesMovement = Math.abs(coin.priceChange24h) >= minimumMovement;

            boolean matchesSearch =
                    searchText.isEmpty()
                            || coin.name.toLowerCase().contains(searchText)
                            || coin.symbol.toLowerCase().contains(searchText);

            if (matchesMovement && matchesSearch) {
                filtered.add(coin);
            }

            if (topTen.isChecked() && filtered.size() == 10) {
                break;
            }
        }

        CoinAdapter adapter = new CoinAdapter(filtered, coin -> {
            Intent intent = new Intent(getContext(), CoinDetailsActivity.class);
            intent.putExtra("coin", coin);
            startActivity(intent);
        });

        recycler.setAdapter(adapter);
    }
}