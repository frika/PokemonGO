package com.nianticlabs.nia.account;

import android.content.Context;
import android.content.SharedPreferences;
import spacemadness.com.lunarconsole.BuildConfig;

public class AccountPreferences {
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static AccountPreferences instance;
    private static final Object staticMutex;
    private final SharedPreferences prefs;

    static {
        staticMutex = new Object();
        instance = null;
    }

    public static AccountPreferences getInstance(Context context) {
        AccountPreferences accountPreferences;
        synchronized (staticMutex) {
            if (instance == null) {
                instance = new AccountPreferences(context);
            }
            accountPreferences = instance;
        }
        return accountPreferences;
    }

    private AccountPreferences(Context context) {
        this.prefs = context.getSharedPreferences(context.getPackageName() + ".PREFS", 0);
    }

    public synchronized void clearAccount() {
        this.prefs.edit().remove(KEY_ACCOUNT_NAME).apply();
    }

    public synchronized String getAccountName() {
        return this.prefs.getString(KEY_ACCOUNT_NAME, BuildConfig.FLAVOR);
    }

    public synchronized void setAccountName(String accountName) {
        this.prefs.edit().putString(KEY_ACCOUNT_NAME, accountName).apply();
    }
}
