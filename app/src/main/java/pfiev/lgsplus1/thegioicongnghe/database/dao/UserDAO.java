package pfiev.lgsplus1.thegioicongnghe.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import pfiev.lgsplus1.thegioicongnghe.database.entity.User;
import pfiev.lgsplus1.thegioicongnghe.database.entity.UserInfo;


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
