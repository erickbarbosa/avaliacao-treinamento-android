package com.example.administrador.myapplication.models.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrador.myapplication.models.entities.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "SERVICE_ORDER_DB_P1";
    private static int VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DatabaseHelper.BANCO_DADOS, null, DatabaseHelper.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ServiceOrderContract.createTable());
        db.execSQL(UserContract.createTable());
        User user = new User();
        user.setId(1L);
        user.setLogin("administrator");
        user.setPassword("@dm1n");
        user.setName("Administrator");
        user.setStored(Boolean.FALSE);
        db.insert(UserContract.TABLE, null, UserContract.getContentValues(user));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ServiceOrderContract.updateTable());
    }

}
