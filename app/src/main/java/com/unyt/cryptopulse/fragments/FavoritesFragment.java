package com.unyt.cryptopulse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.adapters.FavoriteAdapter;
import com.unyt.cryptopulse.database.AppDatabase;
import com.unyt.cryptopulse.database.FavoriteCoin;

import java.util.List;

public class FavoritesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<FavoriteCoin> favorites = AppDatabase
                .getInstance(requireContext())
                .favoriteDao()
                .getAll();

        FavoriteAdapter adapter = new FavoriteAdapter(favorites);
        recyclerView.setAdapter(adapter);

        return view;
    }
}