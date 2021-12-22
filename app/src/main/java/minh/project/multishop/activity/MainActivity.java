package minh.project.multishop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import minh.project.multishop.R;
import minh.project.multishop.activity.viewmodel.MainActivityViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ActivityMainBinding mainBinding;
    private MainActivityViewModel mViewModel;

    private boolean isInit = false;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = mainBinding.getRoot();
        setContentView(viewRoot);

        mViewModel = new MainActivityViewModel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInit) {
            mViewModel.initView();
            mViewModel.initFragment();
            isInit = true;
        }
    }

    @Override
    public void recreate() {
        super.recreate();
        mViewModel.removeAllFragment();
    }

    public ActivityMainBinding getMainBinding() {
        return mainBinding;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (secondTime - firstTime < 2000) {
                finish();
            } else {
                Toast.makeText(MainActivity.this, R.string.first_press_back, Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }
}