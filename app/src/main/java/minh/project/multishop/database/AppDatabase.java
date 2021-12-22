package minh.project.multishop.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import minh.project.multishop.database.dao.UserDAO;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;


@Database(entities = {User.class, UserInfo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
}