package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBHelper extends SQLiteOpenHelper {

    private static final String  DATABASE_NAME = "200501P.db";
    public DBHelper(@Nullable Context context) {
        super(context,  DATABASE_NAME, null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Accounts (accNO VARCHAR(10) PRIMARY KEY, bankName VARCHAR(30), accountHolderName VARCHAR(15), balance DECIMAL(20,2) )");
        db.execSQL("CREATE TABLE Transactions (transactionID INTEGER PRIMARY KEY AUTOINCREMENT, accNO VARCHAR(10), date VARCHAR(10), amount DECIMAL(20,2), type VARCHAR(10) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Accounts");
        db.execSQL("DROP TABLE IF EXISTS Transactions");
        onCreate(db);
    }

    public boolean insertAccount(String accNO, String bankName, String accountHolderName, double balance){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Accounts WHERE accNO = ?", new String[]{accNO});
        ContentValues cValue = new ContentValues();
        cValue.put("accNo",accNO);
        cValue.put("bankName",bankName);
        cValue.put("accountHolderName",accountHolderName);
        cValue.put("balance",balance);

        if (cursor.getCount() == 0) {
            long result = db.insert("Accounts", null, cValue);
            if(result==-1){
                return false;
            }
            return true;
        }
        return false;
    }
    public Cursor getAccount(String accNO){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Accounts WHERE accNO = ?", new String[]{accNO});
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAccounts(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Accounts", null);
        return c;
    }

    public Cursor getAccountNum(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c     = db.rawQuery("SELECT accNO FROM Accounts", null);
        return c;
    }

    public boolean updateBalance(String accNO,  double amount) {
        ContentValues cValues = new ContentValues();
        cValues.put("Balance:", amount);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Accounts WHERE accNO = ?", new String[]{accNO});

        if (c.getCount() > 0) {
            long output = db.update("Accounts", cValues, "accNO=?", new String[]{accNO});
            if ( output == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public boolean deleteAccount(String accNO) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{accNO});
        if (c.getCount() > 0) {
            long result = db.delete("Accounts", "accNO=?", new String[]{accNO});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public boolean insertTransaction(String date, String accNO, ExpenseType expenseType, double amount){
        ContentValues cValues = new ContentValues();
        cValues.put("accNO", accNO);
        cValues.put("date", date);
        cValues.put("amount", amount );
        cValues.put("type", expenseType.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        long output = db.insert("Transactions",null,cValues);
        if(output==-1){
            return false;
        }
        return true;
    }
    public Cursor getAllTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Transactions", null);
        return c;
    }
    public Cursor getLimitedTransactions(int limit){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Transactions ORDER BY transactionID DESC LIMIT " + Integer.toString(limit) , null);
        return c;
    }
}
