package minh.project.multishop.network;

import java.util.List;

import minh.project.multishop.models.CartItem;
import minh.project.multishop.models.Category;
import minh.project.multishop.models.Product;
import minh.project.multishop.models.Rating;
import minh.project.multishop.models.UserProfile;
import minh.project.multishop.network.dtos.DTORequest.CreateOrderRequest;
import minh.project.multishop.network.dtos.DTORequest.RateProductRequest;
import minh.project.multishop.network.dtos.DTORequest.RegisterRequest;
import minh.project.multishop.network.dtos.DTOResponse.GetListOrderResponse;
import minh.project.multishop.network.dtos.DTOResponse.GetProductNameResponse;
import minh.project.multishop.network.dtos.DTOResponse.OrderDetailResponse;
import minh.project.multishop.network.dtos.DTOResponse.EditCartResponse;
import minh.project.multishop.network.dtos.DTORequest.LoginRequest;
import minh.project.multishop.network.dtos.DTORequest.RefreshAccessTokenRequest;
import minh.project.multishop.network.dtos.DTORequest.EditCartRequest;
import minh.project.multishop.network.dtos.DTOResponse.GetListProductResponse;
import minh.project.multishop.network.dtos.DTOResponse.LoginResponse;
import minh.project.multishop.network.dtos.DTOResponse.RefreshAccessTokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAppAPI {

    //Product API
    @GET("products?page=1&page_size=220")
    Call<GetListProductResponse> getAllProduct();

    @GET("products?page=1&page_size=20")
    Call<GetListProductResponse> getHomeListProduct();

    @GET("products/{id}")
    Call<Product> getProductByID(@Path("id") int id);

    @GET("categories")
    Call<List<Category>> getAllCategory();

    @GET("products")
    Call<GetListProductResponse> getProductByCategory(@Query("category") int cateID, @Query("page_size") int size);

    @GET("products/lite?page_size=220")
    Call<GetProductNameResponse> getAllProductName();

    @GET("products?page=1&page_size=220")
    Call<GetListProductResponse> getListProductByName(@Query("search_name") String searchName);

    //UserAPI
    @POST("refresh")
    Call<RefreshAccessTokenResponse> refreshToken(@Body RefreshAccessTokenRequest param);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest params);

    @POST("register")
    Call<UserProfile> register(@Body RegisterRequest request);

    @GET("user/me")
    Call<UserProfile> getProfile(@Header("Authorization") String token);

    @PUT("user/me")
    Call<UserProfile> updateProfile(@Header("Authorization") String token, @Body UserProfile profile);

    //Cart API
    @GET("user/carts")
    Call<List<CartItem>> getCartList(@Header("Authorization") String token);

    @PUT("user/carts/add")
    Call<EditCartResponse> addProductToCart(@Header("Authorization") String token, @Body EditCartRequest params);

    @PUT("user/carts/remove")
    Call<EditCartResponse> removeProductFromCart(@Header("Authorization") String token, @Body EditCartRequest params);

    @DELETE("user/carts/{cartID}")
    Call<String> deleteCartItem(@Header("Authorization") String token, @Path("cartID") int cartID);

    //Order API
    @POST("user/orders")
    Call<OrderDetailResponse> createOrder(@Header("Authorization") String token, @Body CreateOrderRequest params);

    @GET("user/orders/{orderID}")
    Call<OrderDetailResponse> getOrderDetail(@Header("Authorization") String token, @Path("orderID") int orderID);

    @GET("user/orders?page=1&page_size=1")
    Call<GetListOrderResponse> getTotalFirst(@Header("Authorization") String token);

    @GET("user/orders?page=1")
    Call<GetListOrderResponse> getOrderByStatus(@Header("Authorization") String token, @Query("page_size") int size, @Query("status") String status);

    //Review API
    @GET("ratings/{product_id}")
    Call<List<Rating>> getProductRating(@Path("product_id") int productID);

    @POST("user/ratings")
    Call<Rating> rateProduct(@Header("Authorization") String token, @Body RateProductRequest request);

    @GET("user/ratings")
    Call<List<Rating>> getMyRating(@Header("Authorization") String token, @Query("product") int productID);
}