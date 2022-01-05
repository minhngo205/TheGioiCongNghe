package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import minh.project.multishop.R;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.utils.CurrencyFormat;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {

    private final Context mContext;
    private List<OrderItem> orderItemList;

    public OrderItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderItem item = orderItemList.get(position);

        Glide.with(mContext)
                .load(item.getImageURL())
                .placeholder(R.drawable.progress_bar_loading)
                .into(holder.ivThumbnail);

        holder.tvProductName.setText(item.getProductName());
        holder.tvPrice.setText(CurrencyFormat.currencyFormat(item.getSalePrice()));
        holder.tvCount.setText(String.valueOf(item.getCount()));
        holder.tvTotal.setText(CurrencyFormat.currencyFormat(item.getItemTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return orderItemList == null || orderItemList.isEmpty() ? 0 : orderItemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView ivThumbnail;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvCount;
        TextView tvTotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.iv_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvCount = itemView.findViewById(R.id.tv_discount);
            tvTotal = itemView.findViewById(R.id.tv_product_evaluate);
        }
    }
}
