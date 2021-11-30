package pfiev.lgsplus1.thegioicongnghe.network;

import java.util.List;

import pfiev.lgsplus1.thegioicongnghe.models.CartItem;
import pfiev.lgsplus1.thegioicongnghe.models.Category;
import pfiev.lgsplus1.thegioicongnghe.models.Product;
import pfiev.lgsplus1.thegioicongnghe.models.Rating;
import pfiev.lgsplus1.thegioicongnghe.models.UserProfile;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTORequest.CreateOrderRequest;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTORequest.EditCartRequest;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTORequest.LoginRequest;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTORequest.RefreshAccessTokenRequest;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTORequest.RegisterRequest;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse.EditCartResponse;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse.GetListOrderResponse;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse.GetListProductResponse;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse.LoginResponse;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse.OrderDetailResponse;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse.RefreshAccessTokenResponse;
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

    //UserAPI
    @POST("refresh")
    Call<RefreshAccessTokenResponse> refreshToken(@Body RefreshAccessTokenRequest param);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest params);

    @POST("register")
    Call<UserProfile> register(@Body RegisterRequest request);

    @GET("user/me")
    Call<UserProfile> getProfile(@Header("Authorization") String value);

    @PUT("user/me")
    Call<UserProfile> updateProfile(@Header("Authorization") String value, @Body UserProfile profile);

    //Cart API
    @GET("user/carts")
    Call<List<CartItem>> getCartList(@Header("Authorization") String value);

    @PUT("user/carts/add")
    Call<EditCartResponse> addProductToCart(@Header("Authorization") String value, @Body EditCartRequest params);

    @PUT("user/carts/remove")
    Call<EditCartResponse> removeProductFromCart(@Header("Authorization") String value, @Body EditCartRequest params);

    @DELETE("user/carts/{cartID}")
    Call<String> deleteCartItem(@Header("Authorization") String value, @Path("cartID") int cartID);

    //Order API
    @POST("user/orders")
    Call<OrderDetailResponse> createOrder(@Header("Authorization") String value, @Body CreateOrderRequest params);

    @GET("user/orders/{orderID}")
    Call<OrderDetailResponse> getOrderDetail(@Header("Authorization") String value, @Path("orderID") int orderID);

    @GET("user/orders?page=1&page_size=1")
    Call<GetListOrderResponse> getTotalFirst(@Header("Authorization") String value);

    @GET("user/orders?page=1")
    Call<GetListOrderResponse> getOrderByStatus(@Header("Authorization") String value, @Query("page_size") int size, @Query("status") String status);

    //Review API
    @GET("ratings/{product_id}")
    Call<List<Rating>> getProductRating(@Path("product_id") int productID);
}