package pfiev.lgsplus1.thegioicongnghe.activity;

import android.os.Bundle;
import android.view.View;

import pfiev.lgsplus1.thegioicongnghe.activity.viewmodel.LoginActivityViewModel;
import pfiev.lgsplus1.thegioicongnghe.base.BaseActivity;
import pfiev.lgsplus1.thegioicongnghe.databinding.ActivityLoginBinding;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding loginBinding;
    private LoginActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View viewRoot = loginBinding.getRoot();
        setContentView(viewRoot);

        mViewModel = new LoginActivityViewModel(this);
        setResult(RESULT_OK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.initView();
    }

    public ActivityLoginBinding getLoginBinding() {
        return loginBinding;
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }
}