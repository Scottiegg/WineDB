package com.example.scottie.winedb;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * MainActivity
 * <p>
 * Activity seen when app is first opened.
 * <p>
 * Has 3 fragments: main, details and search
 *
 * @author Scott Garton
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add main fragment to activity
        Fragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * onOptionsItemSelected
     *
     * routes user requests for menu items
     *
     * @param item the menu item pressed
     * @return return of super (if successful)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),
                        com.example.scottie.winedb.AddActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
