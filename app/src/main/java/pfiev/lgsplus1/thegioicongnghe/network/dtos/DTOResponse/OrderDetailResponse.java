package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pfiev.lgsplus1.thegioicongnghe.models.OrderItem;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOmodels.DTOOrderItemResponse;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOmodels.Payment;


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

    public List<OrderItem> castToModelList(){
        List<OrderItem> result = new ArrayList<>();
        for (DTOOrderItemResponse itemResponse : orderItemResponses){
            result.add(itemResponse.castToModel());
        }
        return result;
    }
}