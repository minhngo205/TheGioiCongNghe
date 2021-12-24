package minh.project.multishop.network.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import minh.project.multishop.database.entity.ProductName;
import minh.project.multishop.models.Product;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import minh.project.multishop.network.dtos.DTOResponse.GetListProductResponse;
import minh.project.multishop.network.dtos.DTOResponse.GetProductNameResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductNetRepository {
    private final IAppAPI api;

    private static final String TAG = "ProductNetRepository";

    private MutableLiveData<List<Product>> allProduct;
    private MutableLiveData<List<Product>> listProductData;
    private MutableLiveData<Product> product;
    private MutableLiveData<List<ProductName>> productNameData;

    private static ProductNetRepository instance;

    ProductNetRepository() {
        api = RetroInstance.getAppAPI();
    }

    public static ProductNetRepository getInstance(){
        if(instance==null) instance = new ProductNetRepository();
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

    public LiveData<List<ProductName>> getAllProductName(){
        if(null == productNameData){
            productNameData = new MutableLiveData<>();
            loadProductNameData();
        }
        return productNameData;
    }

    public LiveData<List<Product>> getListProductByName(String name){
        listProductData = new MutableLiveData<>();
        loadProductByName(name);
        return listProductData;
    }

    private void loadProductByName(String name) {
        Call<GetListProductResponse> call = api.getListProductByName(name);
        call.enqueue(new Callback<GetListProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetListProductResponse> call, @NonNull Response<GetListProductResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listProductData.postValue(response.body().productList);
                } else listProductData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<GetListProductResponse> call, @NonNull Throwable t) {
                listProductData.postValue(null);
            }
        });
    }

    private void loadProductNameData() {
        Call<GetProductNameResponse> call = api.getAllProductName();
        call.enqueue(new Callback<GetProductNameResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetProductNameResponse> call, @NonNull Response<GetProductNameResponse> response) {
                if(response.isSuccessful() && null != response.body()){
                    productNameData.postValue(response.body().getProductNameList());
                } else productNameData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<GetProductNameResponse> call, @NonNull Throwable t) {
                productNameData.postValue(null);
            }
        });
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
                if(response.isSuccessful() && response.body()!=null){
                    allProduct.postValue(response.body().productList);
                } else allProduct.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<GetListProductResponse> call, @NonNull Throwable t) {
                allProduct.postValue(null);
            }
        });
    }
}