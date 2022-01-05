package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import minh.project.multishop.R;
import minh.project.multishop.activity.LoginActivity;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityLoginBinding;
import minh.project.multishop.network.dtos.DTORequest.LoginRequest;
import minh.project.multishop.network.dtos.DTORequest.RegisterRequest;
import minh.project.multishop.network.repository.UserNetRepository;

public class LoginActivityViewModel extends BaseActivityViewModel<LoginActivity> {

    private final UserNetRepository netRepository;
    private final ActivityLoginBinding mBinding;
    private final UserDBRepository dbRepository;

    private final TextWatcher loginWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkLoginForEmptyValues();
        }
    };

    private final TextWatcher registerWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkRegisterForEmptyValues();
        }
    };

    void checkLoginForEmptyValues(){

        String username = String.valueOf(mBinding.email.getText());
        String password = String.valueOf(mBinding.password.getText());

        mBinding.continueButton.setEnabled(!username.equals("") && !password.equals(""));
    }

    void checkRegisterForEmptyValues(){

        String name = String.valueOf(mBinding.name.getText());
        String username = String.valueOf(mBinding.user2.getText());
        String password = String.valueOf(mBinding.password2.getText());
        String rePassword = String.valueOf(mBinding.reenterPassword.getText());

        mBinding.signupButton.setEnabled(
                !username.equals("") &&
                !password.equals("") &&
                !name.equals("") &&
                !rePassword.equals("")
        );
    }


    /**
     * constructor
     *
     * @param loginActivity Activity object
     */
    public LoginActivityViewModel(LoginActivity loginActivity) {
        super(loginActivity);
        netRepository = UserNetRepository.getInstance();
        mBinding = mActivity.getLoginBinding();
        dbRepository = new UserDBRepository();
    }

    @Override
    public void initView() {
        mBinding.signin.setOnClickListener(mActivity);
        mBinding.signup.setOnClickListener(mActivity);

        checkLoginForEmptyValues();
        checkRegisterForEmptyValues();

        mBinding.continueButton.setOnClickListener(mActivity);
        mBinding.signupButton.setOnClickListener(mActivity);

        mBinding.email.addTextChangedListener(loginWatcher);
        mBinding.password.addTextChangedListener(loginWatcher);

        mBinding.name.addTextChangedListener(registerWatcher);
        mBinding.user2.addTextChangedListener(registerWatcher);
        mBinding.password2.addTextChangedListener(registerWatcher);
        mBinding.reenterPassword.addTextChangedListener(registerWatcher);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.signin:{
                mBinding.signupPage.setVisibility(View.GONE);
                mBinding.signinPage.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.signup:{
                mBinding.signupPage.setVisibility(View.VISIBLE);
                mBinding.signinPage.setVisibility(View.GONE);
                break;
            }
            case R.id.continue_button:{
                String username = String.valueOf(mBinding.email.getText());
                String password = String.valueOf(mBinding.password.getText());

                login(username.trim(),password.trim());
                break;
            }
            case R.id.signup_button:{
                String name = String.valueOf(mBinding.name.getText());
                String username = String.valueOf(mBinding.user2.getText());
                String password = String.valueOf(mBinding.password2.getText());
                String rePassword = String.valueOf(mBinding.reenterPassword.getText());

                if(!rePassword.equals(password)){
                    mBinding.reenterPassword.setError("Cần nhập lại mật khẩu đúng với mật khẩu cần đăng ký");
                    mBinding.reenterPassword.requestFocus();
                    return;
                }

                register(name.trim(),username.trim(),password.trim());
            }
            default: break;
        }
    }

    private void register(String name, String username, String password) {
        RegisterRequest registerRequest = new RegisterRequest(name,username,password);

        netRepository.getRegisterData(registerRequest).observe(mActivity, userProfile -> {
            if(null == userProfile){
                Toast.makeText(mActivity, "Không thể đăng ký", Toast.LENGTH_SHORT).show();
                return;
            }

            dbRepository.insertUserInfo(userProfile.castToInfo());
            login(userProfile.getUsername(),password);
        });
    }

    private void login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username,password);

        netRepository.getLoginData(loginRequest).observe(mActivity, loginResponse -> {
            if (loginResponse==null){
                Toast.makeText(mActivity, "Đăng nhập thất bại. Kiểm tra tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(username,loginResponse.getRefreshToken(),loginResponse.getAccessToken());
            dbRepository.setCurrentUser(user);
            CacheUserInfo(user);
        });
    }

    private void CacheUserInfo(User user) {
        if(null != dbRepository.getUserInfo()){
            onQuit(dbRepository.getUserInfo().getName());
            return;
        }
        netRepository.getUserProfile(user.getAccToken()).observe(mActivity, userProfile -> {
            if(null == userProfile){
                Toast.makeText(mActivity, "Không thể lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            UserInfo userInfo = userProfile.castToInfo();
            Log.d("TAG", "CacheUserInfo: "+userInfo.getUsername());
            dbRepository.insertUserInfo(userInfo);
            onQuit(userInfo.getName());
//
//            Toast.makeText(mActivity, "Username from DB" + dbRepository.getUserInfo().getUsername(), Toast.LENGTH_SHORT).show();
        });
    }

    private void onQuit(String username){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("NAME",username);
        mActivity.setResult(Activity.RESULT_OK,returnIntent);
        mActivity.finish();
    }
}
