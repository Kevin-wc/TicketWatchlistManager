package com.example.ticketwatchlistmanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements TickerListFragment.OnTickerSelectedListener {

    private FragmentManager fm;
    private TickerListFragment listFragment;
    private InfoWebFragment webFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // create fragments
            listFragment = new TickerListFragment();
            webFragment  = new InfoWebFragment();

            FragmentTransaction trans = fm.beginTransaction();
            trans.add(R.id.ListFragment, listFragment, "listFragment");
            trans.add(R.id.infoFragment, webFragment, "webFragment");
            trans.commit();
        } else {
            // recover references by tag
            listFragment = (TickerListFragment) fm.findFragmentByTag("listFragment");
            webFragment  = (InfoWebFragment)  fm.findFragmentByTag("webFragment");
        }
    }


    @Override
    public void onTickerSelected(String ticker) {
        if (webFragment == null) {
            webFragment = (InfoWebFragment) fm.findFragmentByTag("webFragment");
        }
        if (webFragment != null) {
            webFragment.loadTicker(ticker);
        }
    }
}
