package com.self.achyut.maintenant.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.self.achyut.maintenant.activity.ElectricBill;
import com.self.achyut.maintenant.domain.ElectricityCharge;
import com.self.achyut.maintenant.domain.Tenant;
import com.self.achyut.maintenant.utils.DateHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by boonkui on 07-09-2016.
 */
public class TenantDataSource {

    private SQLiteDatabase database;
    private TenantDBHandler dbHandler;
    String[] allColumns = {TenantDBHandler.COLUMN_NAME,TenantDBHandler.COLUMN_MOBILE,TenantDBHandler.COLUMN_RENT,TenantDBHandler.COLUMN_ADVANCE,
            TenantDBHandler.COLUMN_MAINTENANCE,TenantDBHandler.COLUMN_PER_UNIT_CHARGE,TenantDBHandler.COLUMN_DATE_OCCUPIED};

    public TenantDataSource(Context context){
        dbHandler = new TenantDBHandler(context);
    }

    public void open() throws SQLException {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    public boolean createTenant(Tenant tenant){

        ContentValues values = new ContentValues();

        values.put(TenantDBHandler.COLUMN_NAME,tenant.getName());
        values.put(TenantDBHandler.COLUMN_MOBILE,tenant.getMobile());
        values.put(TenantDBHandler.COLUMN_ADVANCE,tenant.getAdvance());
        values.put(TenantDBHandler.COLUMN_MAINTENANCE,tenant.getMaintenance());
        values.put(TenantDBHandler.COLUMN_RENT,tenant.getRent());
        values.put(TenantDBHandler.COLUMN_PER_UNIT_CHARGE,tenant.getPerUnitCharge());
        values.put(TenantDBHandler.COLUMN_DATE_OCCUPIED, DateHandler.dateToString(tenant.getDateOccupied()));

        long insertId = database.insert(TenantDBHandler.TABLE_TENANTS, null,
                values);
        Cursor cursor = database.query(TenantDBHandler.TABLE_TENANTS,
                allColumns, TenantDBHandler.COLUMN_MOBILE + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        cursor.close();
        return true;
    }

    public void deleteTenant(Tenant tenant) {
        String id = tenant.getMobile();
        System.out.println("Tenant deleted with mobile: " + id);
        database.delete(TenantDBHandler.TABLE_TENANTS, TenantDBHandler.COLUMN_MOBILE
                + " = " + id, null);
    }

    public List<Tenant> getAllTenants() {
        List<Tenant> tenants = new ArrayList<Tenant>();

        Cursor cursor = database.rawQuery("select * from "+TenantDBHandler.TABLE_TENANTS,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tenant tenant = cursorToTenant(cursor);
            tenants.add(tenant);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tenants;
    }

    public Tenant getTenantById(String id){

        Cursor cursor = database.rawQuery("select * from "+TenantDBHandler.TABLE_TENANTS+" where "+TenantDBHandler.COLUMN_MOBILE+"="+id,null);
        cursor.moveToFirst();

        Tenant tenant = cursorToTenant(cursor);


        // make sure to close the cursor
        cursor.close();
        return tenant;
    }

    public void updateTenant(Tenant tenant, String tenantId){

        ContentValues values = new ContentValues();

        values.put(TenantDBHandler.COLUMN_NAME,tenant.getName());
        values.put(TenantDBHandler.COLUMN_MOBILE,tenant.getMobile());
        values.put(TenantDBHandler.COLUMN_ADVANCE,tenant.getAdvance());
        values.put(TenantDBHandler.COLUMN_MAINTENANCE,tenant.getMaintenance());
        values.put(TenantDBHandler.COLUMN_RENT,tenant.getRent());
        values.put(TenantDBHandler.COLUMN_PER_UNIT_CHARGE,tenant.getPerUnitCharge());
        values.put(TenantDBHandler.COLUMN_DATE_OCCUPIED, DateHandler.dateToString(tenant.getDateOccupied()));

        database.update(TenantDBHandler.TABLE_TENANTS,values,TenantDBHandler.COLUMN_MOBILE+"="+tenantId,null);

    }

    public void createEmptyRows(int count){
        for(int i=1;i<=count;i++){
            Tenant newTenant = new Tenant();
            newTenant.setName("Tenant "+i);
            newTenant.setMobile(""+i);
            newTenant.setRent(0.0);
            newTenant.setMaintenance(0.0);
            newTenant.setAdvance(0.0);
            newTenant.setPerUnitCharge(0.0);
            newTenant.setDateOccupied(new Date());
            createTenant(newTenant);
        }
    }

    public void deleteTable(){
        database.execSQL("DROP TABLE IF EXISTS " + TenantDBHandler.TABLE_TENANTS );
        database.execSQL(TenantDBHandler.CREATE_TENANT_TABLE);
    }

    private Tenant cursorToTenant(Cursor cursor) {

        Tenant tenant = new Tenant();
        tenant.setName(cursor.getString(1));
        tenant.setMobile(cursor.getString(0));
        tenant.setAdvance(cursor.getLong(4));
        tenant.setMaintenance(cursor.getLong(3));
        tenant.setRent(cursor.getLong(2));
        tenant.setPerUnitCharge(cursor.getLong(5));
        tenant.setDateOccupied(DateHandler.stringToDate(cursor.getString(6)));

        return tenant;
    }

    public boolean createBill(ElectricityCharge charge,String id){

        boolean bool = false;
        ContentValues values = new ContentValues();

        values.put(TenantDBHandler.COLUMN_TENANT_ID,id);
        values.put(TenantDBHandler.COLUMN_PREVIOUS,charge.getPreviousReading());
        values.put(TenantDBHandler.COLUMN_CURRENT,charge.getCurrentReading());
        values.put(TenantDBHandler.COLUMN_UNITS,charge.getUnitsConsumed());
        values.put(TenantDBHandler.COLUMN_TOTAL,charge.getTotal());
        values.put(TenantDBHandler.COLUMN_DATE, DateHandler.dateToString(charge.getDateNoted()));

        try {
            database.insertOrThrow(TenantDBHandler.TABLE_EB, null,values);
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    public List<ElectricityCharge> getBillForTenant(String id){
        List<ElectricityCharge> bills= new ArrayList<ElectricityCharge>();

        Cursor cursor = database.rawQuery("select * from "+TenantDBHandler.TABLE_EB+" where "+TenantDBHandler.COLUMN_TENANT_ID+
                "="+id,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ElectricityCharge bill = cursorToBill(cursor);
            bills.add(bill);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return bills;
    }

    private ElectricityCharge cursorToBill(Cursor cursor) {
        ElectricityCharge bill = new ElectricityCharge();
        bill.setPreviousReading(cursor.getDouble(1));
        bill.setCurrentReading(cursor.getDouble(2));
        bill.setDateNoted(DateHandler.stringToDate(cursor.getString(3)));
        bill.setUnitsConsumed(cursor.getDouble(4));
        bill.setTotal(cursor.getDouble(5));

        return bill;
    }
}
