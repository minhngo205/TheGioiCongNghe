package minh.project.multishop.database.dao;

import androidx.room.*;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    User getCurrentUser();

    @Query("SELECT * FROM user WHERE (username=:username)")
    User getUserByUSN(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("DELETE FROM user")
    void deleteAll();

    @Update()
    void updateAccessToken(User user);

    @Query("SELECT * FROM userinfo")
    UserInfo getUserInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(UserInfo userInfo);

    @Update
    void updateInfo(UserInfo userInfo);

    @Query("DELETE FROM userInfo")
    void clearUserData();
}
