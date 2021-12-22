package minh.project.multishop.network.dtos.DTOmodels;

import com.google.gson.annotations.SerializedName;

import minh.project.multishop.models.OrderItem;
import minh.project.multishop.models.Product;


public class DTOOrderItemResponse {
    @SerializedName("count")
    private int count;
    @SerializedName("order_price")
    private int order_price;
    @SerializedName("product")
    private Product product;

    public int getCount() {
        return count;
    }

    public int getOrder_price() {
        return order_price;
    }

    public Product getProduct() {
        return product;
    }

    public OrderItem castToModel(){
        return new OrderItem(
                product.getID(),
                product.getImageThumbnail(),
                product.getProductName(),
                order_price,
                count
        );
    }
}
