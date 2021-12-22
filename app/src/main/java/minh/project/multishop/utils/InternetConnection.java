package minh.project.multishop.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
    private final Context mContext;

    private static InternetConnection instance;

    public InternetConnection(Context mContext) {
        this.mContext = mContext;
    }

    public static InternetConnection getInstance(Context context){
        if(instance==null) instance = new InternetConnection(context);
        return instance;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
