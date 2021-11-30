package pfiev.lgsplus1.thegioicongnghe.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseUtil {
    private static final String DATABASE_NAME = "multishop.db";

    private static volatile DatabaseUtil instance;

    private final AppDatabase database;

    private DatabaseUtil(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
    }

    private static void getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseUtil.class) {
                if (instance == null) {
                    instance = new DatabaseUtil(context);
                }
            }
        }
    }

    public static void init(Context context) {
        getInstance(context);
    }

    public static AppDatabase getDatabase() {
        return instance.database;
    }
}