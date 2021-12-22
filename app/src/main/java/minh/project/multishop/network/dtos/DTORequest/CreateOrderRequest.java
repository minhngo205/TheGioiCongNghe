package minh.project.multishop.network.dtos.DTORequest;

import java.util.List;

import minh.project.multishop.network.dtos.DTOmodels.DTOOrderItemRequest;

public class CreateOrderRequest {
    public String name;
    public String address;
    public String phone_number;
    public int payment;
    public List<DTOOrderItemRequest> items;

    public CreateOrderRequest(String name, String address, String phone_number, int payment, List<DTOOrderItemRequest> items) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.payment = payment;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public List<DTOOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<DTOOrderItemRequest> items) {
        this.items = items;
    }
}
