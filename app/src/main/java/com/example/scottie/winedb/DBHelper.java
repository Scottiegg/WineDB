package com.example.scottie.winedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper
 * <p>
 * Used by rest of app as a helper class to manage database creation, and general management
 *
 * @author Scott Garton
 * @version 1.0
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sInstance;

    private static final String DATABASE_NAME = "winedb";
    static final String _ID = "_id";
    static final String WINE_TBL = "winelist";

    static final String WINE_NAME = "winename";
    static final String VARIETY = "variety";
    static final String VINEYARD = "vineyard";
    static final String REGION = "region";
    static final String VINTAGE = "vintage";

    static final String TASTED = "tasted";
    static final String IN_CELLAR = "incellar";
    static final String NO_BOTTLES = "nobottles";
    static final String BOX_NO = "boxno";

    //TODO: bitmap storage (onCreate and alias)

    static final String NOTES = "notes";
    static final String RATING = "rating";
    static final String PRICE_PAID = "pricepaid";
    static final String IMAGE = "image";
    static final String DRINK_FROM = "drinkfrom";
    static final String DRINK_TO = "drinkto";
    static final String YEAR_BOUGHT = "yearbought";
    static final String PERCENT_ALCOHOL = "percentalcohol";

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * getInstance
     *
     * @param context Information about application environment
     * @return instance of DBHelper
     */
    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * onCreate
     * <p>
     * A method that uses an SQL statement to create the table in the database.
     *
     * @param db the SQLite database to create the table in
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStmt = "CREATE TABLE " + WINE_TBL +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WINE_NAME + " TEXT, " +
                VARIETY + " TEXT, " +
                VINEYARD + " TEXT, " +
                REGION + " TEXT, " +
                VINTAGE + " TEXT, " +
                TASTED + " INTEGER, " +
                IN_CELLAR + " INTEGER, " +
                NO_BOTTLES + " INTEGER, " +
                BOX_NO + " INTEGER, " +
                NOTES + " TEXT, " +
                RATING + " NUMERIC, " +
                PRICE_PAID + " NUMERIC, " +
                IMAGE + " BLOB, " +
                DRINK_FROM + " INTEGER, " +
                DRINK_TO + " INTEGER, " +
                YEAR_BOUGHT + " INTEGER, " +
                PERCENT_ALCOHOL + " NUMERIC);";

        db.execSQL(createStmt);
    }

    /**
     * onUpgrade
     * <p>
     * Needs to be rewritten when database schema changes
     * Currently DROPs old table and creates a new one (inappropriate)
     *
     * @param db         the SQLite database
     * @param oldVersion former version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.w("WINE_DB_HELPER", "Evil method to upgrade db, will destroy old data");
        db.execSQL("DROP TABLE IF EXISTS " + WINE_TBL);
        onCreate(db);
    }
}	
	