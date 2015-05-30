package com.example.administrador.myapplication.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ServiceOrdersRepository {



    private static class Singleton {
        public static final ServiceOrdersRepository INSTANCE = new ServiceOrdersRepository();
    }

    private ServiceOrdersRepository() {
        super();
    }

    public static ServiceOrdersRepository getInstance() {
        return Singleton.INSTANCE;
    }

    public void save(ServiceOrder serviceOrder) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (serviceOrder.getId() == null) {
            db.insert(ServiceOrderContract.TABLE, null, ServiceOrderContract.getContentValues(serviceOrder));
        } else {
            String where = ServiceOrderContract.ID + " = ?";
            String[] args = {serviceOrder.getId().toString()};
            db.update(ServiceOrderContract.TABLE, ServiceOrderContract.getContentValues(serviceOrder), where, args);
        }
        db.close();
        helper.close();
    }

    public void delete(ServiceOrder serviceOrder) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = ServiceOrderContract.ID + " = ?";
        String[] args = {serviceOrder.getId().toString()};
        db.delete(ServiceOrderContract.TABLE, where, args);
        db.close();
        helper.close();
    }

    public List<ServiceOrder> getAll() {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(ServiceOrderContract.TABLE, ServiceOrderContract.COLUNS, null, null, null, null, ServiceOrderContract.DATE);
        List<ServiceOrder> serviceOrders = ServiceOrderContract.bindList(cursor);
        db.close();
        helper.close();
        return serviceOrders;
    }

    public List<ServiceOrder> filterBy(Map<String, String[]> filters) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();
        StringBuilder where = new StringBuilder();

        List<String> values = new ArrayList<>();
        for(String column : filters.keySet()) {
            String[] entryValues = filters.get(column);
            if(entryValues != null) {
                if (!where.toString().isEmpty()) {
                    where.append(" AND ");
                }
                where.append(column);
                if(entryValues.length > 1) {
                    where.append(" in (");
                    StringBuilder in = new StringBuilder();
                    for(String value : entryValues) {
                        if(!in.toString().isEmpty()) {
                            in.append(", ");
                        }
                        in.append("?");
                        values.add(value);
                    }
                    where.append(in).append(")");
                } else {
                    where.append(" = ?");
                    values.add(entryValues[0]);
                }
            }
        }
        String[] arrayValues = new String[values.size()];
        Cursor cursor = db.query(ServiceOrderContract.TABLE, ServiceOrderContract.COLUNS, where.toString(), values.toArray(arrayValues), null, null, ServiceOrderContract.DATE);
        List<ServiceOrder> serviceOrders = ServiceOrderContract.bindList(cursor);
        db.close();
        helper.close();
        return serviceOrders;
    }

}