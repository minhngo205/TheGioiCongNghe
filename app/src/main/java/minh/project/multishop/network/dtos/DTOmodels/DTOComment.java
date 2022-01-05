package minh.project.multishop.network.dtos.DTOmodels;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DTOComment {
    @SerializedName("user")
    public DTOUser user;
    @SerializedName("comment")
    public String comment;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;
}
