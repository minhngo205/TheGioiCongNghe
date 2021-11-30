package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTORequest;

import com.google.gson.annotations.SerializedName;

public class RefreshAccessTokenRequest {
    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshAccessTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
