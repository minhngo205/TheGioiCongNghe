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
import minh.project.multishop.databinding.FragmentCancelBinding;
import minh.project.multishop.dialog.CustomProgress;
import minh.project.multishop.network.repository.OrderRepository;

import java.util.ArrayList;

import static minh.project.multishop.utils.Statistics.CANCEL_ORDER;

public class CancelFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentCancelBinding mBinding;
    private OrderCenterListAdapter mAdapter;

    private final User mUser;
    private final OrderCentreActivity mActivity;

    public CancelFragment(OrderCentreActivity orderCentreActivity, User user) {
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
        mBinding = FragmentCancelBinding.inflate(inflater,container,false);
        initRecycleView();
        loadOrder();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.layoutRefreshCancel.setOnRefreshListener(this);
        mBinding.layoutRefreshCancel.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initRecycleView() {
        mBinding.cancelOrders.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.cancelOrders.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OrderCenterListAdapter(mActivity);
        mBinding.cancelOrders.setAdapter(mAdapter);
    }

    private void loadOrder() {
        CustomProgress dialog = CustomProgress.getInstance();
        dialog.showProgress(getContext(),"Đang tải...",false);
        OrderRepository.getInstance().getListOrderByStatus(mUser.getAccToken(),CANCEL_ORDER).observe(mActivity, getListOrderResponse -> {
            if(null == getListOrderResponse || getListOrderResponse.results.isEmpty()){
                mBinding.noItem.setVisibility(View.VISIBLE);
                dialog.hideProgress();
                mBinding.layoutRefreshCancel.setRefreshing(false);
                return;
            }

            mBinding.noItem.setVisibility(View.GONE);
            mAdapter.setListOrder(getListOrderResponse.results);
            dialog.hideProgress();
            mBinding.layoutRefreshCancel.setRefreshing(false);
        });
    }

    @Override
    public void onRefresh() {
        mAdapter.setListOrder(new ArrayList<>());
        loadOrder();
    }
}