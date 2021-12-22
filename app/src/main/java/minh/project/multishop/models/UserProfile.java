package minh.project.multishop.models;

import java.util.Date;

import minh.project.multishop.database.entity.UserInfo;
import minh.project.multishop.utils.DateConverter;


public class UserProfile {
    private int id;
    private String username;
    private String email;
    private String role;
    private String name;
    private String address;
    private String phone_number;
    private String dob;
    private Date created_at;
    private Date updated_at;

    public UserProfile(String email, String name, String address, String phone_number, String dob) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getDob() {
        return dob;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public UserInfo castToInfo(){
        return new UserInfo(
                id,
                username,
                email,
                name,
                address,
                phone_number,
                dob,
                DateConverter.fromDate(updated_at)
        );
    }
}
