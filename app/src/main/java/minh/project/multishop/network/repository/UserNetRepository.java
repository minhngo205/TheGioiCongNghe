package minh.project.multishop.network.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import minh.project.multishop.models.UserProfile;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import minh.project.multishop.network.dtos.DTORequest.LoginRequest;
import minh.project.multishop.network.dtos.DTORequest.RefreshAccessTokenRequest;
import minh.project.multishop.network.dtos.DTORequest.RegisterRequest;
import minh.project.multishop.network.dtos.DTOResponse.LoginResponse;
import minh.project.multishop.network.dtos.DTOResponse.RefreshAccessTokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNetRepository {

    private final IAppAPI api;
    private MutableLiveData<LoginResponse> loginData;
    private MutableLiveData<UserProfile> profileData;
    private MutableLiveData<RefreshAccessTokenResponse> tokenData;

    private static UserNetRepository instance;

    private UserNetRepository() {
        this.api = RetroInstance.getAppAPI();
    }

    public static UserNetRepository getInstance(){
        if(instance==null){
            instance = new UserNetRepository();
        }
        return instance;
    }

    public LiveData<LoginResponse> getLoginData(LoginRequest loginRequest){
        loginData = new MutableLiveData<>();
        login(loginRequest);
        return loginData;
    }

    public LiveData<UserProfile> getUserProfile(String accessToken){
        profileData = new MutableLiveData<>();
        loadProfileData(accessToken);
        return profileData;
    }

    public LiveData<RefreshAccessTokenResponse> getTokenData(RefreshAccessTokenRequest request){
        tokenData = new MutableLiveData<>();
        loadTokenData(request);
        return tokenData;
    }

    public LiveData<UserProfile> getRegisterData(RegisterRequest request){
        profileData = new MutableLiveData<>();
        registerUser(request);
        return profileData;
    }

    public LiveData<UserProfile> updateInfoData(String token, UserProfile request){
        profileData = new MutableLiveData<>();
        loadUpdateProfile(token,request);
        return profileData;
    }

    private void loadUpdateProfile(String token, UserProfile request) {
        Call<UserProfile> call = api.updateProfile("Bearer "+token,request);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if(response.isSuccessful()){
                    profileData.postValue(response.body());
                } else profileData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                profileData.postValue(null);
            }
        });
    }

    private void registerUser(RegisterRequest request) {
        Call<UserProfile> call = api.register(request);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if(response.isSuccessful()){
                    profileData.postValue(response.body());
                } else profileData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                profileData.postValue(null);
            }
        });
    }

    private void loadTokenData(RefreshAccessTokenRequest request) {
        Call<RefreshAccessTokenResponse> call = api.refreshToken(request);
        call.enqueue(new Callback<RefreshAccessTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<RefreshAccessTokenResponse> call, @NonNull Response<RefreshAccessTokenResponse> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    tokenData.postValue(response.body());
                } else tokenData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<RefreshAccessTokenResponse> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void loadProfileData(String accessToken) {
        Call<UserProfile> call = api.getProfile("Bearer "+accessToken);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    profileData.postValue(response.body());
                } else profileData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                profileData.postValue(null);
            }
        });
    }

    private void login(LoginRequest loginRequest) {
        Call<LoginResponse> call = api.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    loginData.postValue(response.body());
                } else loginData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginData.postValue(null);
            }
        });
    }


}
