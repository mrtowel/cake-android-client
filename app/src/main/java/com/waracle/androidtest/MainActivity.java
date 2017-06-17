package com.waracle.androidtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.waracle.androidtest.net.HttpCache;
import com.waracle.androidtest.ui.ItemListFragment;


/**
 * Launcher activity.
 */
public final class MainActivity extends AppCompatActivity {

    private static final String ITEM_LIST_FRAGMENT_TAG = "ItemListFragment";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpCache.install(this.getApplicationContext());

        if (getSupportFragmentManager().findFragmentByTag(ITEM_LIST_FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ItemListFragment(), ITEM_LIST_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
