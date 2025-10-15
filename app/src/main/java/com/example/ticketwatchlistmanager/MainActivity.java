package com.example.ticketwatchlistmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

        Log.i("TAG", String.valueOf(ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECEIVE_SMS)));

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED){
            String[] perm = new String[]{Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, perm, 52);
        }


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

        fm.executePendingTransactions();
        onNewIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);

        String status = intent.getStringExtra("sms_status");
        String ticker = intent.getStringExtra("sms ticker");
        if (status == null){
            return;
        }

        if ("valid".equals(status)) {
            if (listFragment != null && ticker != null){
                listFragment.addTicker(ticker);
            }
            if (webFragment != null && ticker != null){
                webFragment.loadTicker(ticker);
            } else if ("invalid_format".equals(status)){
                Toast.makeText(this,"No valid watchlist entry",Toast.LENGTH_SHORT).show();
            } else if ("invalid_ticker".equals(status)){
                Toast.makeText(this, "Ticker is not valid",Toast.LENGTH_SHORT).show();
            }
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
