package minh.project.multishop.base;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(!isConnected(this.requireActivity())){
//            showInternetDialog();
//        }
//    }
//
//    private void showInternetDialog() {
//        InternetConnection dialog = new InternetConnection(this.getActivity());
//        dialog.setCancelListener(view -> {
//            this.requireActivity().finish();
//            dialog.dismiss();
//        });
//        dialog.setConfirmListener(view -> {
//            if (!isConnected(this.requireActivity())) {
//                dialog.show();
//            } else {
////                final FragmentTransaction ft = this.requireActivity().getSupportFragmentManager().beginTransaction();
////                ft.detach(this);
////                ft.attach(this);
////                ft.commit();
//                this.requireActivity().recreate();
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    private boolean isConnected(FragmentActivity activity) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
//    }
}
