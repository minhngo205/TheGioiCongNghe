package minh.project.multishop;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.ProcessLifecycleOwner;
import minh.project.multishop.database.DatabaseUtil;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.ProductDBRepository;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.network.dtos.DTORequest.RefreshAccessTokenRequest;
import minh.project.multishop.network.repository.ProductNetRepository;
import minh.project.multishop.network.repository.UserNetRepository;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();
    private static MainApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
        DatabaseUtil.init(this);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::fetchProductNameData);
        handler.post(this::refreshToken);
    }

    private void fetchProductNameData() {
        ProductDBRepository dbRepository = ProductDBRepository.getInstance();
        ProductNetRepository netRepository = ProductNetRepository.getInstance();
        netRepository.getAllProductName().observe(ProcessLifecycleOwner.get(), productNames -> {
            if(null == productNames){
                Log.i(TAG, "fetchProductNameData: Cannot get Data");
                return;
            }

            dbRepository.InsertProductNameData(productNames);
            Log.d(TAG, "fetchProductNameData: "+dbRepository.getAllProductName().isEmpty());
        });
    }

    private void refreshToken() {
        UserDBRepository dbRepository = UserDBRepository.getInstance();
        UserNetRepository netRepository = UserNetRepository.getInstance();
        User mUser = dbRepository.getCurrentUser();
        if(mUser==null){
            return;
        }
        RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(mUser.getRefToken());
        netRepository.getTokenData(request).observe(ProcessLifecycleOwner.get(), refreshAccessTokenResponse -> {
            if (null == refreshAccessTokenResponse){
                Toast.makeText(this, "Cannot get information", Toast.LENGTH_SHORT).show();
                return;
            }
            mUser.setAccToken(refreshAccessTokenResponse.getNewAccessToken());
            dbRepository.updateToken(mUser);

            Log.d("TAG", "Token in DB: " + dbRepository.getCurrentUser().getAccToken());
        });
    }

    private static void setContext(MainApplication application) {
        mApplication = application;
    }

    public static Context getContext() {
        return mApplication.getApplicationContext();
    }
}