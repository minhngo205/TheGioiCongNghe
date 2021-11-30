package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;

public class RefreshAccessTokenResponse {

    @SerializedName("access_token")
    private String newAccessToken;

    public String getNewAccessToken() {
        return newAccessToken;
    }
}
