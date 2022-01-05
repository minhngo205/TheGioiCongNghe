package minh.project.multishop.models;

import com.google.gson.annotations.SerializedName;
import minh.project.multishop.network.dtos.DTOmodels.DTOComment;
import minh.project.multishop.network.dtos.DTOmodels.DTOUser;

import java.util.Date;
import java.util.List;

public class Rating {
    @SerializedName("id")
    private int id;
    @SerializedName("rate")
    private int rate;
    @SerializedName("comment")
    private String comment;
    @SerializedName("created_at")
    private Date created_at;
    @SerializedName("updated_at")
    private Date updated_at;
    @SerializedName("product")
    private int productID;
    @SerializedName("user")
    private DTOUser user;
    @SerializedName("is_solved")
    private boolean isSolved;
    @SerializedName("responses")
    private List<DTOComment> responses;

    public int getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public int getProductID() {
        return productID;
    }

    public DTOUser getUser() {
        return user;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public List<DTOComment> getResponses() {
        return responses;
    }
}
