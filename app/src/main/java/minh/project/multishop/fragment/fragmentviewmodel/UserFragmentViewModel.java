package minh.project.multishop.fragment.fragmentviewmodel;


import static minh.project.multishop.base.BaseDialog.CANCEL_BUTTON;
import static minh.project.multishop.base.BaseDialog.CONFIRM_BUTTON;
import static minh.project.multishop.base.BaseDialog.CONTENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import minh.project.multishop.R;
import minh.project.multishop.activity.CartActivity;
import minh.project.multishop.activity.LoginActivity;
import minh.project.multishop.activity.OrderCentreActivity;
import minh.project.multishop.activity.EditInfoActivity;
import minh.project.multishop.base.BaseDialog;
import minh.project.multishop.base.BaseFragmentViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.FragmentUserBinding;
import minh.project.multishop.fragment.UserFragment;

public class UserFragmentViewModel extends BaseFragmentViewModel<UserFragment> {

    private User mUser;
    private final UserInfo mUserInfo;
    private final UserDBRepository mUserDBRepository;
    private FragmentUserBinding binding;

    /**
     * constructor
     *
     * @param userFragment Fragment object
     */
    public UserFragmentViewModel(UserFragment userFragment) {
        super(userFragment);
        mUserDBRepository = UserDBRepository.getInstance();
        mUserInfo = mUserDBRepository.getUserInfo();
    }

    @Override
    public void initView(View view) {
        binding = mFragment.getBinding();

        binding.tvSignIn.setOnClickListener(mFragment);
        binding.lvScan.setOnClickListener(mFragment);
        binding.lvAccount.setOnClickListener(mFragment);
        binding.lvBag.setOnClickListener(mFragment);
        binding.lvOrder.setOnClickListener(mFragment);
        binding.lvSave.setOnClickListener(mFragment);
        binding.lvSet.setOnClickListener(mFragment);
        binding.lvOffline.setOnClickListener(mFragment);
        binding.lvContact.setOnClickListener(mFragment);
        binding.lvOut.setOnClickListener(mFragment);
    }

    public void checkSignIn() {
        mUser = mUserDBRepository.getCurrentUser();
        if (mUser == null) { // no sign
            binding.layoutWelcome.setVisibility(View.GONE);
            binding.tvSignIn.setVisibility(View.VISIBLE);
            return;
        }

        // sign in
        binding.layoutWelcome.setVisibility(View.VISIBLE);
        binding.tvSignIn.setVisibility(View.GONE);
        Log.d("TAG", "checkSignIn: "+mUserInfo);
        if(null == mUserInfo || mUserInfo.getName().isEmpty()){
            binding.tvUserName.setText(mFragment.requireContext().getString(R.string.welcome_user,mUser.getUsername()));
            return;
        }
        binding.tvUserName.setText(mFragment.requireContext().getString(R.string.welcome_user,mUserInfo.getName()));
    }

    ActivityResultLauncher<Intent> activityResultLauncher = mFragment.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    checkSignIn();
                    Intent data = result.getData();
                    if(null != data){
                        String name = data.getStringExtra("NAME");
                        setUserName(name);
                    }
                }
            }
    );

    private void setUserName(String name) {
        if(null == name || name.isEmpty()){
            binding.tvUserName.setText(mFragment.requireContext().getString(R.string.welcome_user,mUser.getUsername()));
            return;
        }

        binding.tvUserName.setText(mFragment.requireContext().getString(R.string.welcome_user,name));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId) {
            case R.id.tv_sign_in: // Sign in
                Intent loginIntent = new Intent(mFragment.getActivity(), LoginActivity.class);
                activityResultLauncher.launch(loginIntent);
                break;
            case R.id.lv_scan: // Scan to pay

                break;
            case R.id.lv_account: // My Account
                if(null == mUser){
                    Toast.makeText(mFragment.getContext(), "Bạn cần phải đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent editInfoIntent = new Intent(mFragment.getActivity(), EditInfoActivity.class);
                activityResultLauncher.launch(editInfoIntent);
                break;
            case R.id.lv_bag: // Bag
                Intent cartIntent = new Intent(mFragment.getActivity(), CartActivity.class);
                mFragment.requireActivity().startActivity(cartIntent);
                break;
            case R.id.lv_order: // Order Center
                if(null == mUser){
                    Toast.makeText(mFragment.getContext(), "Bạn cần phải đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFragment.requireActivity().startActivity(new Intent(mFragment.getActivity(), OrderCentreActivity.class));
                break;
            case R.id.lv_save: // Favourite

                break;
            case R.id.lv_set: // Setting

                break;
            case R.id.lv_offline: // Offline Shop

                break;
            case R.id.lv_contact: // Contact Us

                break;
            case R.id.lv_out: // Log out
                checkSignOut();
                break;
            case R.id.lv_left: // Default
                break;
            default:
                break;
        }
    }

    private void checkSignOut() {
        if (mUser == null) {
            Toast.makeText(mFragment.getActivity(), R.string.please_sign_first, Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle data = new Bundle();
        data.putString(CONFIRM_BUTTON, mFragment.getString(R.string.confirm));
        data.putString(CANCEL_BUTTON, mFragment.getString(R.string.cancel));
        data.putString(CONTENT, mFragment.getString(R.string.confirm_log_out));

        BaseDialog dialog = new BaseDialog(mFragment.requireActivity(), data, true);
        dialog.setConfirmListener(v -> {
            signOut();
            dialog.dismiss();
        });
        dialog.setCancelListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void signOut() {
        mUserDBRepository.deleteUser();
        mUserDBRepository.clearInfoData();
        checkSignIn();
    }
}
