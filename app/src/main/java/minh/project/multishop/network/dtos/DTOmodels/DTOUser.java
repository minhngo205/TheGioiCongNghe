package minh.project.multishop.network.dtos.DTOmodels;

import com.google.gson.annotations.SerializedName;

public class DTOUser {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("role")
    public String role;
}
