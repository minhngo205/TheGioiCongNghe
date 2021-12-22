package minh.project.multishop.network.dtos.DTOmodels;

import com.google.gson.annotations.SerializedName;

public class DTOLink {
    @SerializedName("next")
    public String nextLink;
    @SerializedName("previous")
    public String previousLink;
}

