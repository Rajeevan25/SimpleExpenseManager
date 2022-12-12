package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBhelper;

public class PersistentExpenseManager extends ExpenseManager{
    private DBhelper databaseHelper;
    public PersistentExpenseManager(Context c) {
        setup(c);
    }


    @Override
    public void setup(Context c) {

        databaseHelper = new DBhelper(c);

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(databaseHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(databaseHelper);
        setAccountsDAO(persistentAccountDAO);

    }
}
