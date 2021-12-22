package minh.project.multishop.models;

import android.os.Parcel;
import android.os.Parcelable;

import minh.project.multishop.network.dtos.DTOmodels.DTOOrderItemRequest;

public class OrderItem implements Parcelable {
    private int productID;
    private String imageURL;
    private String productName;
    private int salePrice;
    private int count;

    public OrderItem(int productID, String imageURL, String productName, int salePrice, int count) {
        this.productID = productID;
        this.imageURL = imageURL;
        this.productName = productName;
        this.salePrice = salePrice;
        this.count = count;
    }

    protected OrderItem(Parcel in) {
        productID = in.readInt();
        imageURL = in.readString();
        productName = in.readString();
        salePrice = in.readInt();
        count = in.readInt();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getItemTotalPrice() {
        return salePrice*count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(productID);
        parcel.writeString(imageURL);
        parcel.writeString(productName);
        parcel.writeInt(salePrice);
        parcel.writeInt(count);
    }

    public DTOOrderItemRequest castToDTO(){
        return new DTOOrderItemRequest(
                productID,
                count
        );
    }
}
