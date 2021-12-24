package minh.project.multishop.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SearchHistory {
    @NonNull
    @PrimaryKey
    private String searchContent;

    public SearchHistory(@NonNull String searchContent) {
        this.searchContent = searchContent;
    }

    @NonNull
    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(@NonNull String searchContent) {
        this.searchContent = searchContent;
    }
}