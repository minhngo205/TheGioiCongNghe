package minh.project.multishop.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import minh.project.multishop.dialog.InternetConnection;

public abstract class BaseActivityViewModel<T extends BaseActivity> {
    // Activity object
    protected T mActivity;

    /**
     * constructor
     *
     * @param t Activity object
     */
    public BaseActivityViewModel(T t) {
        this.mActivity = t;
        if(!isConnected(t)){
            showInternetDialog();
        }
    }

    /**
     * Used to initialize the layout
     */
    public abstract void initView();

    /**
     * Set the click event.
     *
     * @param viewId Control ID
     */
    public abstract void onClickEvent(int viewId);

    private void showInternetDialog() {
        InternetConnection dialog = new InternetConnection(mActivity);
        dialog.setCancelListener(view -> {
            mActivity.finish();
            dialog.dismiss();
        });
        dialog.setConfirmListener(view -> {
            if (!isConnected(mActivity)) {
                dialog.show();
            } else {
                mActivity.recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isConnected(T activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }
}
