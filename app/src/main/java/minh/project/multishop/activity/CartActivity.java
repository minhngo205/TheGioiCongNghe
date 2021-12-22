package minh.project.multishop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import minh.project.multishop.activity.viewmodel.CartActivityViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityCartBinding;
import minh.project.multishop.utils.OnItemModifyListener;

public class CartActivity extends BaseActivity implements View.OnClickListener, OnItemModifyListener {

    private ActivityCartBinding mBinding;
    private CartActivityViewModel mViewModel;
    private boolean isInit = false;
    private Map<Integer,Boolean> cartItemSelectedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCartBinding.inflate(getLayoutInflater());
        View viewRoot = mBinding.getRoot();
        setContentView(viewRoot);

        mViewModel = new CartActivityViewModel(this);
        mViewModel.initView();
        cartItemSelectedMap = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isInit){
            mViewModel.initData();
            isInit = true;
            return;
        }
        mViewModel.reloadData(cartItemSelectedMap);
    }

    public ActivityCartBinding getBinding() {
        return mBinding;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }

    @Override
    public void onItemChoose(int position, boolean isChecked) {
        mViewModel.onItemChoose(position,isChecked);
    }

    @Override
    public void onItemQuantityAdd(int position, View quantityView) {
        mViewModel.onItemQuantityAdd(position,quantityView);
    }

    @Override
    public void onItemQuantityReduce(int position, View quantityView) {
        mViewModel.onItemQuantityReduce(position, quantityView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartItemSelectedMap = mViewModel.getCheckedMap();
    }
}