package pfiev.lgsplus1.thegioicongnghe.network;

import static pfiev.lgsplus1.thegioicongnghe.utils.Statistics.BASE_URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {

    private static Retrofit retrofit;
    private static IAppAPI AppAPI;

    public static Retrofit getRetroInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if(retrofit == null) {
            retrofit =  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static IAppAPI getAppAPI(){
        if(AppAPI==null){
            AppAPI = getRetroInstance().create(IAppAPI.class);
        }
        return AppAPI;
    }
}
