package com.example.scottie.winedb;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * AddActivity
 * <p>
 * Used in in-app form (via AddFragmentScreen[1-3])
 * to allow user to enter in a new wine into app.
 * <p>
 * Implements 3 interfaces, for each fragment to store data from each screen in the form.
 *
 * @author Scott Garton
 * @version 1.0
 */
public class AddActivity extends AppCompatActivity
        implements AddFragmentScreen1.OnNextClickListenerScreen1,
        AddFragmentScreen2.OnNextClickListenerScreen2,
        AddFragmentScreen3.OnNextClickListenerScreen3 {
    /**
     * Stores each screen's information
     */
    Bundle screen1Data = null;
    Bundle screen2Data = null;
    Bundle screen3Data = null;

    /**
     * removeFocus
     * <p>
     * Removes focus from currently focused UI element
     */
    private void removeFocus() {
        // remove focus from UI elements
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * onNextButtonCLickScreen1
     * <p>
     * Saves data from first screen, when user clicks next.
     *
     * @param savedData data produced from screen 1
     */
    public void onNextButtonClickScreen1(Bundle savedData) {
        removeFocus();
        screen1Data = savedData;

        // Load new fragment for next screen
        Fragment newFragment = new AddFragmentScreen2();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_layout, newFragment)
                .commit();
    }

    /**
     * onNextButtonCLickScreen2
     * <p>
     * Saves data from second screen, when user clicks next.
     *
     * @param savedData data produced from screen 2
     */
    public void onNextButtonClickScreen2(Bundle savedData) {
        removeFocus();
        screen2Data = savedData;

        // Load new fragment for next screen
        Fragment newFragment = new AddFragmentScreen3();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_layout, newFragment)
                .commit();
    }

    /**
     * onNextButtonCLickScreen3
     * <p>
     * Saves data from last screen, when user clicks next.
     * Collates data from all screens, then saves to SQLite database
     *
     * @param savedData data produced from screen 3
     */
    public void onNextButtonClickScreen3(Bundle savedData) {
        //save incoming data
        screen3Data = savedData;

        DBHelper db = DBHelper.getInstance(this);
        ContentValues cv = new ContentValues();

        // insert screen 1 content
        if (screen1Data != null) {
            cv.put(DBHelper.WINE_NAME, screen1Data.getString("WINE_NAME"));
            cv.put(DBHelper.VARIETY, screen1Data.getString("VARIETY"));
            cv.put(DBHelper.VINEYARD, screen1Data.getString("VINEYARD"));
            cv.put(DBHelper.VINTAGE, screen1Data.getInt("VINTAGE"));

            if (screen1Data.containsKey("REGION")) {
                cv.put(DBHelper.REGION, screen1Data.getString("REGION"));
            }
        }

        // insert screen 2 content
        if (screen2Data != null) {
            int tasted = (screen2Data.getBoolean("TASTED")) ? 1 : 0;
            cv.put(DBHelper.TASTED, tasted);

            boolean inCellarBool = screen2Data.getBoolean("IN_CELLAR");
            int inCellarInt = inCellarBool ? 1 : 0;
            cv.put(DBHelper.IN_CELLAR, inCellarInt);

            if (inCellarBool) {
                cv.put(DBHelper.NO_BOTTLES, screen2Data.getInt("NO_BOTTLES"));
                cv.put(DBHelper.BOX_NO, screen2Data.getInt("BOX_NO"));
            }
        }

        // insert screen 3 content
        if (screen3Data != null) {
            //TODO:get bitmap

            if (screen3Data.containsKey("NOTES"))
                cv.put(DBHelper.NOTES, screen3Data.getString("NOTES"));

            if (screen3Data.containsKey("RATING"))
                cv.put(DBHelper.RATING, screen3Data.getFloat("RATING"));

            if (screen3Data.containsKey("PRICE_PAID"))
                cv.put(DBHelper.PRICE_PAID, screen3Data.getString("PRICE_PAID"));

            if (screen3Data.containsKey("IMAGE")) {
                byte[] imageBytes = (byte[]) screen3Data.getSerializable("IMAGE");
                cv.put(DBHelper.IMAGE, imageBytes);
            }

            if (screen3Data.containsKey("DRINK_FROM"))
                cv.put(DBHelper.DRINK_FROM, screen3Data.getString("DRINK_FROM"));

            if (screen3Data.containsKey("DRINK_TO"))
                cv.put(DBHelper.DRINK_TO, screen3Data.getString("DRINK_TO"));

            if (screen3Data.containsKey("YEAR_BOUGHT"))
                cv.put(DBHelper.YEAR_BOUGHT, screen3Data.getString("YEAR_BOUGHT"));

            if (screen3Data.containsKey("PERCENT_ALCOHOL"))
                cv.put(DBHelper.PERCENT_ALCOHOL, screen3Data.getString("PERCENT_ALCOHOL"));
        }

        // insert entry into database
        db.getWritableDatabase().insert(DBHelper.WINE_TBL, null, cv);
        db.close();

        //restart main activity
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),
                com.example.scottie.winedb.MainActivity.class);
        startActivity(intent);
    }

    /**
     * onCreate
     * Loads in Add Fragment for Screen 1.
     *
     * @param savedInstanceState not used currently.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Fragment fragment = new AddFragmentScreen1();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
