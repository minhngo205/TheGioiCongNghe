package minh.project.multishop.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import minh.project.multishop.database.entity.SearchHistory;

@Dao
public interface SearchHistoryDAO {
//    @Query("SELECT * FROM SearchHistory WHERE searchContent = :searchString")
//    List<SearchHistory> isExist(String searchString);

    @Query("SELECT searchContent FROM SearchHistory")
    List<String> getAllSearchHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSearchHistory(SearchHistory... histories);

    @Query("DELETE FROM SearchHistory")
    void deleteAllSearchHistory();

    @Delete
    void deleteRecord(SearchHistory... histories);
}
