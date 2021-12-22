package minh.project.multishop.network.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import minh.project.multishop.models.CartItem;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import minh.project.multishop.network.dtos.DTORequest.EditCartRequest;
import minh.project.multishop.network.dtos.DTOResponse.EditCartResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private final IAppAPI api;
    private static CartRepository instance;

    private MutableLiveData<List<CartItem>> listCartData;
    private MutableLiveData<EditCartResponse> editCartData;
    private MutableLiveData<String> deleteCartItemResult;

    private CartRepository() {
        this.api = RetroInstance.getAppAPI();
    }

    public static CartRepository getInstance(){
        if(instance==null) instance = new CartRepository();
        return instance;
    }

    public LiveData<List<CartItem>> getListCartData(String token){
        listCartData = new MutableLiveData<>();
        loadListCartData(token);
        return listCartData;
    }

    public LiveData<EditCartResponse> getAddCartData(String token, EditCartRequest request){
        editCartData = new MutableLiveData<>();
        loadAddCartData(token, request);
        return editCartData;
    }

    public LiveData<EditCartResponse> getRemoveCartData(String token, EditCartRequest request){
        editCartData = new MutableLiveData<>();
        loadRemoveCartData(token, request);
        return editCartData;
    }

    public LiveData<String> deleteCartItem(String token, int cartID){
        deleteCartItemResult = new MutableLiveData<>();
        deleteCartItemCall(token,cartID);
        return deleteCartItemResult;
    }

    private void deleteCartItemCall(String token, int cartID) {
        Call<String> call = api.deleteCartItem("Bearer "+token,cartID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    deleteCartItemResult.postValue(response.body());
                } else deleteCartItemResult.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                deleteCartItemResult.postValue(null);
            }
        });
    }

    private void loadRemoveCartData(String token, EditCartRequest request) {
        Call<EditCartResponse> call = api.removeProductFromCart("Bearer "+token,request);
        call.enqueue(new Callback<EditCartResponse>() {
            @Override
            public void onResponse(@NonNull Call<EditCartResponse> call, @NonNull Response<EditCartResponse> response) {
                if(response.isSuccessful()){
                    editCartData.postValue(response.body());
                } else editCartData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<EditCartResponse> call, @NonNull Throwable t) {
                editCartData.postValue(null);
            }
        });

    }

    private void loadAddCartData(String token, EditCartRequest request) {
        Call<EditCartResponse> call = api.addProductToCart("Bearer "+token,request);
        call.enqueue(new Callback<EditCartResponse>() {
            @Override
            public void onResponse(@NonNull Call<EditCartResponse> call, @NonNull Response<EditCartResponse> response) {
                if(response.isSuccessful()){
                    editCartData.postValue(response.body());
                } else editCartData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<EditCartResponse> call, @NonNull Throwable t) {
                editCartData.postValue(null);
            }
        });
    }


    private void loadListCartData(String token) {
        Call<List<CartItem>> call = api.getCartList("Bearer "+token);
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<CartItem>> call, @NonNull Response<List<CartItem>> response) {
                if(response.isSuccessful()){
                    listCartData.postValue(response.body());
                } else listCartData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<List<CartItem>> call, @NonNull Throwable t) {
                listCartData.postValue(null);
            }
        });
    }


}
