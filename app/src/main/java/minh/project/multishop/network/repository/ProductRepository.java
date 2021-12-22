package minh.project.multishop.network.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import minh.project.multishop.models.Product;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import minh.project.multishop.network.dtos.DTOResponse.GetListProductResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private final IAppAPI api;
    private static final String TAG = "ProductRepository";
    private MutableLiveData<List<Product>> allProduct;
    private MutableLiveData<Product> product;
    private static ProductRepository instance;

    ProductRepository() {
        api = RetroInstance.getAppAPI();
    }

    public static ProductRepository getInstance(){
        if(instance==null) instance = new ProductRepository();
        return instance;
    }

    public LiveData<List<Product>> getAllProduct(){
        if(allProduct==null){
            allProduct = new MutableLiveData<>();
            loadAllProduct();
        }
        return allProduct;
    }

    public LiveData<Product> getProductByID(int ID){
        product = new MutableLiveData<>();
        loadProductData(ID);
        return product;
    }

    private void loadProductData(int id) {
        Call<Product> call = api.getProductByID(id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if(response.isSuccessful()){
                    product.postValue(response.body());
                } else product.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                product.postValue(null);
            }
        });
    }

    private void loadAllProduct() {
        Call<GetListProductResponse> call = api.getAllProduct();
        call.enqueue(new Callback<GetListProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetListProductResponse> call, @NonNull Response<GetListProductResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<GetListProductResponse> call, @NonNull Throwable t) {
                allProduct.postValue(null);
            }
        });
    }
}