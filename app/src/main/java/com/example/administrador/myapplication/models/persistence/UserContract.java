package com.example.administrador.myapplication.models.persistence;

import android.content.ContentValues;

import com.example.administrador.myapplication.models.entities.User;

/**
 * Created by erick.barbosa on 28/05/2015.
 */
public class UserContract {

    public static final String TABLE = "user";
    public static final String ID = "id";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String STORED = "stored";

    public static final String[] COLUMNS = {ID, LOGIN, PASSWORD, NAME, STORED};

    public static String createTable() {
        final StringBuilder sql = new StringBuilder();
        sql.append(" CREATE TABLE ");
        sql.append(TABLE);
        sql.append(" ( ");
        sql.append(ID + " INTEGER PRIMARY KEY, ");
        sql.append(LOGIN + " TEXT, ");
        sql.append(PASSWORD + " TEXT, ");
        sql.append(NAME + " TEXT, ");
        sql.append(STORED + " INTEGER ");
        sql.append(" ); ");
        return sql.toString();
    }

    public static ContentValues getContentValues(User user) {
        ContentValues content = new ContentValues();
        content.put(ID, user.getId());
        content.put(LOGIN, user.getLogin());
        content.put(PASSWORD, user.getPassword());
        content.put(NAME, user.getName());
        content.put(STORED, user.isStored() ? 1 : 0);
        return content;
    }

}
