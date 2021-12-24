package minh.project.multishop.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import minh.project.multishop.database.dao.ProductNameDAO;
import minh.project.multishop.database.dao.SearchHistoryDAO;
import minh.project.multishop.database.dao.UserDAO;
import minh.project.multishop.database.entity.ProductName;
import minh.project.multishop.database.entity.SearchHistory;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;

@Database(entities = {User.class, UserInfo.class, ProductName.class, SearchHistory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract ProductNameDAO productNameDAO();
    public abstract SearchHistoryDAO searchHistoryDAO();
}