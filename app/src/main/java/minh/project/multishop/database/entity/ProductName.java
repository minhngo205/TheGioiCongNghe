package minh.project.multishop.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity
public class ProductName {
    @PrimaryKey
    @SerializedName("id")
    private int productID;

    @ColumnInfo
    @SerializedName("name")
    private String productName;

    @ColumnInfo
    @SerializedName("name_latin")
    private String productNameLatin;

    public ProductName(int productID, String productName, String productNameLatin) {
        this.productID = productID;
        this.productName = productName;
        this.productNameLatin = productNameLatin;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameLatin() {
        return productNameLatin;
    }

    public void setProductNameLatin(String productNameLatin) {
        this.productNameLatin = productNameLatin;
    }
}
