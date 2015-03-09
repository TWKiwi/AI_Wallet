package kiwi.ai_wallet;

import static kiwi.ai_wallet.DbConstants.TABLE_NAME;
import static android.provider.BaseColumns._ID;
import static kiwi.ai_wallet.DbConstants.PHNAME;
import static kiwi.ai_wallet.DbConstants.NAME;
import static kiwi.ai_wallet.DbConstants.TYPE;
import static kiwi.ai_wallet.DbConstants.PRICE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kiwi on 15/2/25.
 */
public class DBHelper extends SQLiteOpenHelper{

    private final static String DATABASE_NAME = "demo.db";
    private final static int DATABASE_VERSION = 5;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME +
                                  " (" +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                  PHNAME + " CHAR, " +
                                  NAME + " CHAR, " +
                                  TYPE + " CHAR, " +
                                  PRICE + " CHAR);";
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }
}
