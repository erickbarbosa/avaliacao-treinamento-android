package com.example.administrador.myapplication.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrador.myapplication.models.entities.User;
import com.example.administrador.myapplication.util.AppUtil;

/**
 * Created by erick.barbosa on 28/05/2015.
 */
public class UserRepository {

    private static class Singleton {
        private static UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() { }

    public static UserRepository getInstance() { return Singleton.INSTANCE; }

    public void save(User user) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase bd = helper.getWritableDatabase();
        if(user.getId() == null) {
            bd.insert(UserContract.TABLE, null, UserContract.getContentValues(user));
        } else {
            String where = UserContract.ID + "= ?";
            String[] args = {user.getId().toString()};
            bd.update(UserContract.TABLE, UserContract.getContentValues(user), where, args);
        }
    }

    public Boolean authenticate(String login, String pass) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase bd = helper.getReadableDatabase();
        String sql = "SELECT " +UserContract.LOGIN + " FROM " + UserContract.TABLE +
                " WHERE " + UserContract.LOGIN + " = ?" +
                " AND " + UserContract.PASSWORD + " = ?";
        String[] args = {login, pass};

        Cursor cursor = bd.rawQuery(sql, args);
        return cursor.moveToFirst();
    }

}
