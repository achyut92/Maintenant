package com.self.achyut.maintenant.dbHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.self.achyut.maintenant.utils.Constants;

/**
 * Created by boonkui on 07-09-2016.
 */
public class TenantDBHandler extends SQLiteOpenHelper {

    public static final String TABLE_TENANTS = "TENANTS";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_MOBILE = "MOBILE";
    public static final String COLUMN_RENT = "RENT";
    public static final String COLUMN_MAINTENANCE = "MAINTENANCE";
    public static final String COLUMN_ADVANCE = "ADVANCE";
    public static final String COLUMN_PER_UNIT_CHARGE = "PER_UNIT_CHARGE";
    public static final String COLUMN_DATE_OCCUPIED= "DATE_OCCUPIED";

    public static final String TABLE_EB = "BILL";
    public static final String COLUMN_TENANT_ID = "TENANT_ID";
    public static final String COLUMN_PREVIOUS = "PRV_READING";
    public static final String COLUMN_CURRENT = "CURR_READING";
    public static final String COLUMN_DATE= "DATE_NOTED";
    public static final String COLUMN_UNITS = "UNITS";
    public static final String COLUMN_TOTAL = "TOTAL";

    public static final String CREATE_TENANT_TABLE = "CREATE TABLE " + TABLE_TENANTS + " ( "
            + COLUMN_MOBILE + " INTEGER PRIMARY KEY, " + COLUMN_NAME
            + " TEXT, " + COLUMN_RENT + " INTEGER, " + COLUMN_MAINTENANCE + " INTEGER, "
            + COLUMN_ADVANCE + " INTEGER, " +COLUMN_PER_UNIT_CHARGE + " INTEGER, "
            + COLUMN_DATE_OCCUPIED + " DATETIME)";

    public static final String CREATE_EB_TABLE = "CREATE TABLE " + TABLE_EB + " ( "
            + COLUMN_TENANT_ID + " TEXT," + COLUMN_PREVIOUS + " INTEGER, " + COLUMN_CURRENT
            + " INTEGER, " + COLUMN_DATE + " DATETIME, " + COLUMN_UNITS + " INTEGER, "
            + COLUMN_TOTAL + " INTEGER, PRIMARY KEY("+COLUMN_TENANT_ID+","+COLUMN_DATE+"))";


    public TenantDBHandler(Context context) {
        super(context, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TENANT_TABLE);
        db.execSQL(CREATE_EB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TENANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EB);
        onCreate(db);
    }
}
