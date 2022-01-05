package minh.project.multishop.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import minh.project.multishop.R;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityOrderCentreBinding;
import minh.project.multishop.fragment.ordercentre.*;
import minh.project.multishop.utils.OnOrderListRefresh;

import java.util.ArrayList;
import java.util.List;

import static minh.project.multishop.utils.Statistics.*;

public class OrderCentreActivity extends AppCompatActivity {

    private ActivityOrderCentreBinding mBinding;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOrderCentreBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        mBinding.titleOrderCenter.tvTitle.setText("Đơn hàng của tôi");
        User user = UserDBRepository.getInstance().getCurrentUser();
        mFragments = new ArrayList<>();
        mFragments.add(new WaitingConfirmFragment(this, user));
        mFragments.add(new ConfirmedFragment(this, user));
        mFragments.add(new ShippingFragment(this, user));
        mFragments.add(new SuccessFragment(this, user));
        mFragments.add(new CancelFragment(this, user));

        FragmentStateAdapter mAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getItemCount() {
                return mFragments.size();
            }
        };

        mBinding.viewpagerOrderCenter.setAdapter(mAdapter);
        mBinding.viewpagerOrderCenter.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                resetColor();
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void selectTab(int i) {
        switch (i) {
            case TAB_WAITING_CONFIRM_INDEX:
                mBinding.tabWaiting.setTextColor(ContextCompat.getColor(this,R.color.red));
                mBinding.viewWaiting.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
                break;
            case TAB_CONFIRMED_INDEX:
                mBinding.tabConfirmed.setTextColor(ContextCompat.getColor(this,R.color.red));
                mBinding.viewConfirmed.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
                break;
            case TAB_SHIPPING_INDEX:
                mBinding.tabShipping.setTextColor(ContextCompat.getColor(this,R.color.red));
                mBinding.viewShipping.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
                break;
            case TAB_SUCCESS_INDEX:
                mBinding.tabCompleteOrder.setTextColor(ContextCompat.getColor(this,R.color.red));
                mBinding.viewCompleteOrder.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
                break;
            case TAB_CANCEL_INDEX:
                mBinding.tabCancelOrder.setTextColor(ContextCompat.getColor(this,R.color.red));
                mBinding.viewCancelOrder.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
                break;
            default:
                break;
        }
        mBinding.viewpagerOrderCenter.setCurrentItem(i,true);
    }

    private void resetColor() {
        mBinding.tabWaiting.setTextColor(ContextCompat.getColor(this,R.color.black));
        mBinding.viewWaiting.setBackgroundColor(ContextCompat.getColor(this,R.color.white));

        mBinding.tabConfirmed.setTextColor(ContextCompat.getColor(this,R.color.black));
        mBinding.viewConfirmed.setBackgroundColor(ContextCompat.getColor(this,R.color.white));

        mBinding.tabShipping.setTextColor(ContextCompat.getColor(this,R.color.black));
        mBinding.viewShipping.setBackgroundColor(ContextCompat.getColor(this,R.color.white));

        mBinding.tabCompleteOrder.setTextColor(ContextCompat.getColor(this,R.color.black));
        mBinding.viewCompleteOrder.setBackgroundColor(ContextCompat.getColor(this,R.color.white));

        mBinding.tabCancelOrder.setTextColor(ContextCompat.getColor(this,R.color.black));
        mBinding.viewCancelOrder.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
    }

    public void onDataRefresh() {
        for (Fragment fragment : mFragments) {
            if(fragment.getClass() == WaitingConfirmFragment.class) ((OnOrderListRefresh) fragment).onDataRefresh();
        }
    }
}