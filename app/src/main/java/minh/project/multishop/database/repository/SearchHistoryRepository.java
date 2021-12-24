package minh.project.multishop.database.repository;

import minh.project.multishop.database.AppDatabase;
import minh.project.multishop.database.DatabaseUtil;
import minh.project.multishop.database.dao.SearchHistoryDAO;
import minh.project.multishop.database.entity.SearchHistory;

import java.util.List;

public class SearchHistoryRepository {
    private static SearchHistoryRepository instance;
    private final SearchHistoryDAO searchHistoryDAO;

    private SearchHistoryRepository(){
        AppDatabase database = DatabaseUtil.getDatabase();
        this.searchHistoryDAO = database.searchHistoryDAO();
    }

    public static SearchHistoryRepository getInstance(){
        if(null == instance) instance = new SearchHistoryRepository();
        return instance;
    }

    public List<String> getAllSearchHistory(){
        return searchHistoryDAO.getAllSearchHistory();
    }

    public void addSearchHistory(SearchHistory history){
//        if(null != searchHistoryDAO.isExist(history.getSearchContent())){
//            List<SearchHistory> records = searchHistoryDAO.isExist(history.getSearchContent());
//            for(SearchHistory searchHistory : records){
//                searchHistoryDAO.deleteRecord(searchHistory);
//            }
//        }
        searchHistoryDAO.deleteRecord(history);
        searchHistoryDAO.addSearchHistory(history);
    }

    public void clearHistory(){
        searchHistoryDAO.deleteAllSearchHistory();
    }
}
