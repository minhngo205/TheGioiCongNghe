package pfiev.lgsplus1.thegioicongnghe.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pfiev.lgsplus1.thegioicongnghe.database.dao.UserDAO;
import pfiev.lgsplus1.thegioicongnghe.database.entity.User;
import pfiev.lgsplus1.thegioicongnghe.database.entity.UserInfo;


@Database(entities = {User.class, UserInfo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
}