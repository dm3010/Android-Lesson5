package com.edu0988.lesson5;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu0988.lesson5.database.UserDBHelper;
import com.edu0988.lesson5.database.UserDBSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Users {

    public static final List<User> LIST = loadFromDatabase();

    public static User get(String uuid) {
        for (User user : LIST) {
            if (user.getUuid().equals(UUID.fromString(uuid))) return user;
        }
        return null;
    }

    public static void add(User user) {
        SQLiteDatabase db = UserDBHelper.get().getWritableDatabase();
        db.insert(UserDBSchema.UserTable.NAME, null, getContentValues(user));
        db.close();
        LIST.add(user);
    }

    public static void update(User updateUser) {
        User user = get(updateUser.getUuid().toString());
        if (user == null) {
            add(updateUser);
        } else {
            SQLiteDatabase db = UserDBHelper.get().getWritableDatabase();
            db.update(
                    UserDBSchema.UserTable.NAME,
                    getContentValues(user),
                    "uuid = ?",
                    new String[]{user.getUuid().toString()}
            );
            db.close();
            LIST.set(LIST.indexOf(user), updateUser);
        }
    }

    public static void delete(String uuid) {
        User user = get(uuid);
        if (user != null) {
            SQLiteDatabase db = UserDBHelper.get().getWritableDatabase();
            db.delete(
                    UserDBSchema.UserTable.NAME,
                    "uuid = ?",
                    new String[]{uuid}
            );
            db.close();
            LIST.remove(user);
        }
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserDBSchema.Cols.UUID, user.getUuid().toString());
        values.put(UserDBSchema.Cols.USERNAME, user.getName());
        values.put(UserDBSchema.Cols.USERLASTNAME, user.getLastname());
        values.put(UserDBSchema.Cols.PHONE, user.getPhone());
        return values;
    }

    private static List<User> loadFromDatabase() {
        List<User> userList = new ArrayList<>();
        try (SQLiteDatabase db = UserDBHelper.get().getWritableDatabase();
             Cursor cursor = queryAll(db)) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                userList.add(get(cursor));
                cursor.moveToNext();
            }
        }
        return userList;
    }

    private static Cursor queryAll(SQLiteDatabase db) {
        return db.query(
                UserDBSchema.UserTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static User get(Cursor cursor) {
        String uuidString = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.UUID));
        String userName = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.USERNAME));
        String userLastName = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.USERLASTNAME));
        String phone = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.PHONE));
        User user = new User(UUID.fromString(uuidString));
        user.setName(userName);
        user.setLastname(userLastName);
        user.setPhone(phone);
        return user;
    }
}
