package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOmodels;

import com.google.gson.annotations.SerializedName;

public class DTOOrderItemRequest {
    @SerializedName("product")
    private int productID;
    @SerializedName("count")
    private int count;

    public DTOOrderItemRequest(int productID, int count) {
        this.productID = productID;
        this.count = count;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
