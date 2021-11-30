package pfiev.lgsplus1.thegioicongnghe.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CartItem {
    @SerializedName("id")
    private int ID;
    @SerializedName("product")
    private Product product;
    @SerializedName("count")
    private int count;
    @SerializedName("create_at")
    private Date created_at;
    @SerializedName("updated_at")
    private Date updated_at;

    public CartItem(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    private boolean Choose;

    public int getID() {
        return ID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setChoose(boolean choose) {
        Choose = choose;
    }

    public boolean isChoose() {
        return Choose;
    }

    public OrderItem toOrderItem() {
        return new OrderItem(
                product.getID(),
                product.getImageThumbnail(),
                product.getProductName(),
                product.getSalePrice(),
                count
        );
    }
}
