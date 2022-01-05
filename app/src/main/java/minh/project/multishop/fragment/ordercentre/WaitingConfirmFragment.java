package minh.project.multishop.fragment.ordercentre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import minh.project.multishop.activity.OrderCentreActivity;
import minh.project.multishop.adapter.OrderCenterListAdapter;
import minh.project.multishop.base.BaseFragment;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.databinding.FragmentWaitingConfirmBinding;
import minh.project.multishop.dialog.CustomProgress;
import minh.project.multishop.network.repository.OrderRepository;
import minh.project.multishop.utils.OnOrderListRefresh;

import java.util.ArrayList;

import static minh.project.multishop.utils.Statistics.WAITING_CONFIRM;

public class WaitingConfirmFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnOrderListRefresh {

    private FragmentWaitingConfirmBinding mBinding;
    private OrderCenterListAdapter mAdapter;

    private final User mUser;
    private final OrderCentreActivity mActivity;

    public WaitingConfirmFragment(OrderCentreActivity orderCentreActivity, User user) {
        this.mUser = user;
        this.mActivity = orderCentreActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWaitingConfirmBinding.inflate(inflater,container,false);
        initRecycleView();
        loadOrder();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.layoutWaitingConfirm.setOnRefreshListener(this);
        mBinding.layoutWaitingConfirm.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initRecycleView() {
        mBinding.waitingOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.waitingOrders.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OrderCenterListAdapter(mActivity);
        mBinding.waitingOrders.setAdapter(mAdapter);
    }

    private void loadOrder() {
        CustomProgress dialog = CustomProgress.getInstance();
        dialog.showProgress(getContext(),"Đang tải...",false);
        OrderRepository.getInstance().getListOrderByStatus(mUser.getAccToken(),WAITING_CONFIRM).observe(mActivity, getListOrderResponse -> {
            if(null == getListOrderResponse || getListOrderResponse.results.isEmpty()){
                mBinding.noItem.setVisibility(View.VISIBLE);
                dialog.hideProgress();
                mBinding.layoutWaitingConfirm.setRefreshing(false);
                return;
            }

            mBinding.noItem.setVisibility(View.GONE);
            mAdapter.setListOrder(getListOrderResponse.results);
            dialog.hideProgress();
            mBinding.layoutWaitingConfirm.setRefreshing(false);
        });
    }

    @Override
    public void onRefresh() {
        mAdapter.setListOrder(new ArrayList<>());
        loadOrder();
    }

    @Override
    public void onDataRefresh() {
        onRefresh();
    }
}