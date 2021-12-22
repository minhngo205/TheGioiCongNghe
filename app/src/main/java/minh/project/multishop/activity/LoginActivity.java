package minh.project.multishop.activity;

import android.os.Bundle;
import android.view.View;

import minh.project.multishop.activity.viewmodel.LoginActivityViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityLoginBinding;


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