package minh.project.multishop.database.repository;

import minh.project.multishop.database.AppDatabase;
import minh.project.multishop.database.DatabaseUtil;
import minh.project.multishop.database.dao.UserDAO;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;

public class UserDBRepository {
    private final UserDAO userDao;

    private static UserDBRepository instance;

    public UserDBRepository() {
        AppDatabase database = DatabaseUtil.getDatabase();
        this.userDao = database.userDAO();
    }

    public static UserDBRepository getInstance(){
        if(instance==null){
            instance = new UserDBRepository();
        }
        return instance;
    }

    public User getCurrentUser(){
        return userDao.getCurrentUser();
    }

    public void setCurrentUser(User user){
        userDao.insert(user);
    }

    public void deleteUser(){
        userDao.deleteAll();
    }

    public void updateToken(User user){
        userDao.updateAccessToken(user);
    }

    public User getUserByUsername(String username){
        return userDao.getUserByUSN(username);
    }

    public UserInfo getUserInfo(){
        return userDao.getUserInfo();
    }

    public void insertUserInfo(UserInfo userInfo){
        userDao.insertUserInfo(userInfo);
    }

    public void updateInfo(UserInfo userInfo){
        userDao.updateInfo(userInfo);
    }

    public void clearInfoData(){
        userDao.clearUserData();
    }
}
