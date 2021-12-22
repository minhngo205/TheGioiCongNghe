package minh.project.multishop.network.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import minh.project.multishop.models.Rating;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingRepository {
    private final IAppAPI api;
    private static RatingRepository instance;

    private MutableLiveData<List<Rating>> productRatings;

    private RatingRepository(){
        api = RetroInstance.getAppAPI();
    }

    public static RatingRepository getInstance(){
        if(null == instance) instance = new RatingRepository();
        return instance;
    }

    public LiveData<List<Rating>> getListRating(int productID){
        productRatings = new MutableLiveData<>();
        loadRatings(productID);
        return productRatings;
    }

    private void loadRatings(int productID) {
        Call<List<Rating>> call = api.getProductRating(productID);
        call.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(@NonNull Call<List<Rating>> call, @NonNull Response<List<Rating>> response) {
                if(response.isSuccessful()){
                    productRatings.postValue(response.body());
                } else productRatings.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<List<Rating>> call, @NonNull Throwable t) {
                productRatings.postValue(null);
            }
        });
    }


}
