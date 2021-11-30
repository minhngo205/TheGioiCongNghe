package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("access_token")
    private String accessToken;


    @SerializedName("detail")
    private String errorDetail;
    @SerializedName("code")
    private String errorCode;


    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
