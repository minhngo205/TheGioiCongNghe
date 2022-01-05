package minh.project.multishop.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import minh.project.multishop.R;
import minh.project.multishop.adapter.OrderItemAdapter;
import minh.project.multishop.databinding.DialogCanceledInforBinding;
import minh.project.multishop.models.OrderItem;

import java.util.List;

public class CancelInfoDialog {

    private final DialogCanceledInforBinding mBinding;
    private final Context mContext;
    private final AlertDialog dialog;

    public CancelInfoDialog(Context context, Bundle data, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        mBinding = DialogCanceledInforBinding.inflate(LayoutInflater.from(context));
        mContext = context;

        setupData(data);

        builder.setView(mBinding.getRoot());
        builder.setCancelable(cancelable);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setupData(Bundle data) {
        int orderID = data.getInt("ORDER_ID");
        mBinding.textOrderNumber.setText(mContext.getString(R.string.order_cancel_infor,orderID));

        String cancelDate = data.getString("CANCEL_DAY");
        mBinding.textCancelDate.setText(mContext.getString(R.string.cancel_time, cancelDate));

        List<OrderItem> items = data.getParcelableArrayList("LIST_ORDER");
        mBinding.listOrderItem.setLayoutManager(new LinearLayoutManager(mContext));
        OrderItemAdapter adapter = new OrderItemAdapter(mContext);
        adapter.setOrderItemList(items);
        mBinding.listOrderItem.setAdapter(adapter);
    }

    /**
     * close the dialog
     */
    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * Show the dialog
     */
    public void show() {
        dialog.show();
    }

    /**
     * set the listener for the confirm button
     *
     * @param listener set the listener for the confirm button
     */
    public void setConfirmListener(TextView.OnClickListener listener) {
        mBinding.textViewCancelOrder.setOnClickListener(listener);
    }
}
