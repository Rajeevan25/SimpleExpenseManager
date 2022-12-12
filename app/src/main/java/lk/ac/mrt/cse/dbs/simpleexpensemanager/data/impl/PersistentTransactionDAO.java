package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper dataBaseHelper;

    public PersistentTransactionDAO(DBHelper db) {
        this.dataBaseHelper = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String Date = new SimpleDateFormat("dd-mm-yyyy").format(date);
        dataBaseHelper.insertTransaction(Date,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList= new ArrayList<>();
        Cursor c = dataBaseHelper.getAllTransactions();

        if(c.getCount()==0){
            return transactionList;
        }
        while(c.moveToNext()){
            try {
                String accNo              = c.getString(1);;
                Date date                  =new SimpleDateFormat("dd-mm-yyyy").parse(c.getString(2));
                Double amount              = c.getDouble(3);
                String typeT               = c.getString(4);
                Log.d("myTag", typeT);
                ExpenseType type= ExpenseType.valueOf(typeT);
                Transaction t = new Transaction(date,accNo,type,amount);
                transactionList.add(t);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit)  {
        List<Transaction> transactionList = new ArrayList<>();
        Cursor cur = dataBaseHelper.getLimitedTransactions(limit);

        if(cur.getCount()==0){
            return transactionList;
        }
        while(cur.moveToNext()){
            try {
                String accNo              =cur.getString(1);;
                Date date                  =new SimpleDateFormat("dd-mm-yyyy").parse(cur.getString(2));
                Double amount              = cur.getDouble(3);
                String typeT               = cur.getString(4);
                ExpenseType type        =  ExpenseType.valueOf(typeT);
                Transaction t           = new Transaction(date,accNo,type,amount);
                transactionList.add(t);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return transactionList;

    }
}
