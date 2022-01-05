package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import minh.project.multishop.R;
import minh.project.multishop.activity.OrderCentreActivity;
import minh.project.multishop.activity.OrderDetailActivity;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.dialog.BaseDialog;
import minh.project.multishop.dialog.CancelInfoDialog;
import minh.project.multishop.network.dtos.DTOResponse.OrderDetailResponse;
import minh.project.multishop.network.dtos.DTOmodels.DTOOrderItemResponse;
import minh.project.multishop.network.repository.OrderRepository;
import minh.project.multishop.utils.CurrencyFormat;
import minh.project.multishop.utils.DateConverter;
import minh.project.multishop.utils.Statistics;

import java.util.List;

import static minh.project.multishop.dialog.BaseDialog.*;
import static minh.project.multishop.utils.Statistics.*;

public class OrderCenterListAdapter extends RecyclerView.Adapter<OrderCenterListAdapter.MyViewHolder> {

    private final Context mContext;
    private List<OrderDetailResponse> listOrder;

    public OrderCenterListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListOrder(List<OrderDetailResponse> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordercenter_list, parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetailResponse detail = listOrder.get(position);

        int totalCount = 0;
        for(DTOOrderItemResponse item : detail.orderItemResponses){
            totalCount += item.getCount();
        }

        switch (detail.orderStatus){
            case WAITING_CONFIRM:
                holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.orange_type_1));
                break;
            case CONFIRMED_ORDER:
                holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.purple_200));
                break;
            case SHIPPING_ORDER:
                holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.gold));
                break;
            case SUCCESS_ORDER:
                holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.light_green_1));
                break;
            case CANCEL_ORDER:
                holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                break;
            default: break;
        }

        holder.orderID.setText(mContext.getString(R.string.order_center_order_number,detail.orderID));
        holder.status.setText(Statistics.getStatusText(detail.orderStatus));

        OrderItemAdapter adapter = new OrderItemAdapter(mContext);
        adapter.setOrderItemList(detail.castToModelList());
        LinearLayoutManager layoutManager = new MyLinearLayoutManager(mContext);
        holder.orderItemList.setLayoutManager(layoutManager);
        holder.orderItemList.setAdapter(adapter);

        holder.productTotalInfo.setText(mContext.getString(R.string.order_center_total,totalCount) + CurrencyFormat.currencyFormat(detail.totalCost));
        View.OnClickListener redirectToOrderDetail = view -> {
            Intent intent = new Intent(mContext, OrderDetailActivity.class);
            intent.putExtra("ORDER_ID",detail.orderID);
            mContext.startActivity(intent);
        };

        if(WAITING_CONFIRM.equals(detail.orderStatus)){
            holder.layoutCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setOnClickListener(v -> cancelOrder(detail));
        }
        holder.itemView.setOnClickListener(redirectToOrderDetail);
        holder.orderItemList.setOnClickListener(redirectToOrderDetail);
    }

    private void cancelOrder(OrderDetailResponse detail) {
        Bundle data = new Bundle();

        data.putString(CONFIRM_BUTTON, mContext.getString(R.string.confirm));
        data.putString(CONTENT, mContext.getString(R.string.order_center_confirm_cancel_order));
        data.putString(CANCEL_BUTTON, mContext.getString(R.string.cancel));

        BaseDialog dialog = new BaseDialog(mContext, data, true);
        dialog.setConfirmListener(v -> {

            User mUser = UserDBRepository.getInstance().getCurrentUser();

            OrderRepository.getInstance().cancelOrder(mUser.getAccToken(),detail.orderID).observe(ProcessLifecycleOwner.get(), orderDetailResponse -> {
                if(null == orderDetailResponse){
                    Toast.makeText(mContext, "Không thể huỷ đơn hàng lúc này.\nHãy liên lạc với Quản trị viên", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle orderData = new Bundle();

                orderData.putInt("ORDER_ID",orderDetailResponse.orderID);
                orderData.putString("CANCEL_DAY", DateConverter.DateTimeFormat(orderDetailResponse.updateDay));
                orderData.putParcelableArrayList("LIST_ORDER", orderDetailResponse.castToModelList());

                CancelInfoDialog infoDialog = new CancelInfoDialog(mContext,orderData,true);
                infoDialog.setConfirmListener(v1 -> {
                    Intent intent = new Intent(mContext,OrderDetailActivity.class);
                    intent.putExtra("ORDER_ID",orderDetailResponse.orderID);
                    mContext.startActivity(intent);
                    infoDialog.dismiss();
                });

                dialog.dismiss();
                infoDialog.show();

                Toast.makeText(mContext, "Đã huỷ đơn hàng " + orderDetailResponse.orderID + " thành công.\nXem trong mục \"Đơn đã huỷ\"", Toast.LENGTH_SHORT).show();
                ((OrderCentreActivity)mContext).onDataRefresh();
            });
        });

        dialog.setCancelListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return null == listOrder || listOrder.isEmpty() ? 0 : listOrder.size();
    }

    private static class MyLinearLayoutManager extends LinearLayoutManager {
        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderID;

        TextView status;

        TextView productTotalInfo;

        RecyclerView orderItemList;

        RelativeLayout layoutCancel;

        TextView btnCancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.text_order_number);
            status = itemView.findViewById(R.id.text_pending_payment_time);
            productTotalInfo = itemView.findViewById(R.id.text_product_total_info);
            orderItemList = itemView.findViewById(R.id.item_list_order);
            layoutCancel = itemView.findViewById(R.id.order_to_be_cancel);
            btnCancel = itemView.findViewById(R.id.tv_cancel_order);

        }
    }
}
