package minh.project.multishop.network.dtos.DTORequest;

import com.google.gson.annotations.SerializedName;

public class RateProductRequest {
    @SerializedName("product")
    private int productID;
    @SerializedName("comment")
    private String rateComment;
    @SerializedName("rate")
    private int rateIndex;

    public RateProductRequest(int productID, String rateComment, int rateIndex) {
        this.productID = productID;
        this.rateComment = rateComment;
        this.rateIndex = rateIndex;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getRateComment() {
        return rateComment;
    }

    public void setRateComment(String rateComment) {
        this.rateComment = rateComment;
    }

    public int getRateIndex() {
        return rateIndex;
    }

    public void setRateIndex(int rateIndex) {
        this.rateIndex = rateIndex;
    }
}
