package minh.project.multishop.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;

public class RefreshAccessTokenResponse {

    @SerializedName("access_token")
    private String newAccessToken;

    public String getNewAccessToken() {
        return newAccessToken;
    }
}
