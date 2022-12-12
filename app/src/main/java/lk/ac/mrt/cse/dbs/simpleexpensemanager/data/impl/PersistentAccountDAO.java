package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBhelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private final DBhelper databaseHelper;
    public PersistentAccountDAO(DBhelper db) {
        this.databaseHelper = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> AccountNumList = new ArrayList<>();
        Cursor c = databaseHelper.getAccountNum();

        if(c.getCount()==0){
            return AccountNumList;
        }
        if (c.moveToFirst()) {
            do {
                AccountNumList.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return AccountNumList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> AccountList = new ArrayList<>();
        Cursor c = databaseHelper.getAccounts();

        if(c.getCount()==0){
            return AccountList;
        }
        while(c.moveToNext()){
            String accountNO          = c.getString(0);
            String bankName           = c.getString(1);
            String accountHolderName  = c.getString(2);
            Double balance            = c.getDouble(3);
            Account a = new Account(accountNO,bankName,accountHolderName,balance);
            AccountList.add(a);
        }
        return AccountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor c = databaseHelper.getAccount(accountNo);

        if (c.getCount()>0) {
            String AccountNO         = c.getString(0);
            String bankName          = c.getString(1);
            String accountHolderName = c.getString(2);
            Double balance           = c.getDouble(3);
            Account a = new Account(AccountNO,bankName,accountHolderName,balance);
            return a;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }



    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
            databaseHelper.deleteAccount(accountNo);
    }
    @Override
    public void addAccount(Account account) {
        String bank      = account.getBankName();
        String accountNo = account.getAccountNo();
        String holder    = account.getAccountHolderName();
        Double balance   = account.getBalance();
        databaseHelper.insertAccount(accountNo,bank,holder,balance);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        if(!(account instanceof Account)){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        double b = account.getBalance();
        switch (expenseType) {
            case INCOME:
                b = account.getBalance() + amount;
                account.setBalance(b);
                break;
            case EXPENSE:
                b = account.getBalance() - amount;
                account.setBalance(b);
                break;
        }
        databaseHelper.updateBalance(accountNo,b);
    }
}
