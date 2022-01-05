package minh.project.multishop.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.models.Payment;
import minh.project.multishop.network.dtos.DTOmodels.DTOOrderItemResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailResponse {
    @SerializedName("id")
    public int orderID;
    @SerializedName("status")
    public String orderStatus;
    @SerializedName("user")
    public int userID;
    @SerializedName("is_paid")
    public boolean isPaid;
    @SerializedName("payment")
    public Payment payment;
    @SerializedName("name")
    public String Name;
    @SerializedName("address")
    public String userAddress;
    @SerializedName("phone_number")
    public String userPhone;
    @SerializedName("sum_price")
    public int sumPrice;
    @SerializedName("shipping_fee")
    public int shipFee;
    @SerializedName("total_cost")
    public int totalCost;
    @SerializedName("created_at")
    public Date orderDay;
    @SerializedName("updated_at")
    public Date updateDay;
    @SerializedName("items")
    public List<DTOOrderItemResponse> orderItemResponses;

    public ArrayList<OrderItem> castToModelList(){
        ArrayList<OrderItem> result = new ArrayList<>();
        for (DTOOrderItemResponse itemResponse : orderItemResponses){
            result.add(itemResponse.castToModel());
        }
        return result;
    }
}