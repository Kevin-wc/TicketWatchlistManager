package com.example.ticketwatchlistmanager;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class TickerListFragment extends Fragment {

    private static final int maxSize = 6;
    //Pattern is a java class that will check for patterns. In this case, it will check that
    // it only contains capitalized letters A-Z and that they repeat 1 to 5 times
    private static final Pattern tickerPattern = Pattern.compile("^[A-Z]{1,5}$");


    private ArrayList<String> tickers;
    private ArrayAdapter<String> adapter;

    public interface OnTickerSelectedListener {
        void onTickerSelected(String ticker);
    }

    private OnTickerSelectedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnTickerSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTickerSelectedListener");
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            tickers = new ArrayList<>();
            tickers.add("NEE");
            tickers.add("AAPL");
            tickers.add("DIS");
        } else {
            tickers = savedInstanceState.getStringArrayList("tickers");
            if (tickers == null){
                tickers = new ArrayList<>();
                tickers.add("NEE");
                tickers.add("AAPL");
                tickers.add("DIS");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_ticker_list, container, false);

        ListView listView = view.findViewById(R.id.tickerLV);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
                tickers);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            String ticker = tickers.get(position);
            if (listener != null) listener.onTickerSelected(ticker);
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("tickers", tickers);
    }
    public void addTicker(String s){
        if (s == null || s.trim().isEmpty()) {
            return;
        }
        String ticker = s.trim();

        if (!tickerPattern.matcher(ticker).matches()){
            return;
        }
        if (tickers.contains(ticker)){
            tickers.remove(ticker);
            tickers.add(0, ticker);
        } else if (tickers.size() < maxSize){
            tickers.add(ticker);
        } else {
            tickers.set(maxSize - 1, ticker);
        }
        adapter.notifyDataSetChanged();
    }
}