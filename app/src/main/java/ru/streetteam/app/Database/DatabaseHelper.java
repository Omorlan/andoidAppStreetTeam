package ru.streetteam.app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "placesss.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "places"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_LATITUDE = "latitude"; //широта
    public static final String COLUMN_LONGITUDE = "longotude"; //долгота
    public static final String COLUMN_INFO = "info"; //описание
    public static final String COLUMN_CHANNEL_ID = "channelId"; //id
    public static final String COLUMN_ROOM_NAME = "roomName"; //имя команты

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LABEL + " TEXT, "
                + COLUMN_INFO + " TEXT, "
                + COLUMN_LATITUDE + " INTEGER, "
                + COLUMN_LONGITUDE + " INTEGER,"
                + COLUMN_CHANNEL_ID + " TEXT, "
                + COLUMN_ROOM_NAME + " TEXT)");
        // добавление начальных данных
        db.execSQL("INSERT INTO " + TABLE +
                " (" + COLUMN_LABEL + ", "
                + COLUMN_INFO + ", "
                + COLUMN_LATITUDE + ", "
                + COLUMN_LONGITUDE + ", "
                + COLUMN_CHANNEL_ID + ", "
                + COLUMN_ROOM_NAME + " ) " +
                "VALUES ('Pluzhnik fitness'," +
                " 'Фитнес зал'," +
                " 55.588545, " +
                "37.600649," +
                " 'w6wSvI0YGYWlqQuF', " +
                "'observable-room') ;");
        // добавление начальных данных
        db.execSQL("INSERT INTO " + TABLE +
                " (" + COLUMN_LABEL + ", "
                + COLUMN_INFO + ", "
                + COLUMN_LATITUDE + ", "
                + COLUMN_LONGITUDE + ", "
                + COLUMN_CHANNEL_ID + ", "
                + COLUMN_ROOM_NAME + " ) " +
                "VALUES ('Paris-life'," +
                " 'Фитнес зал', " +
                "55.600389, " +
                "37.598995," +
                "'OA6Q7ngCFVeHh8Mx'," +
                "'observable-channel') ;");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}