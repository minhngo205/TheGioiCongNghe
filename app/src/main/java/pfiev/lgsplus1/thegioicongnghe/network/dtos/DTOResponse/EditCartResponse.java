package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EditCartResponse {
    @SerializedName("id")
    private int cartItemId;
    @SerializedName("count")
    private int count;
    @SerializedName("created_at")
    private Date created_at;
    @SerializedName("updated_at")
    private Date updated_at;
    @SerializedName("user")
    private int userID;
    @SerializedName("product")
    private int productID;

    public int getCartItemId() {
        return cartItemId;
    }

    public int getCount() {
        return count;
    }

    public int getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }
}
