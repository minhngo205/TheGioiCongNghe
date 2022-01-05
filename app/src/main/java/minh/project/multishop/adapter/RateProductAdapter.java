package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import minh.project.multishop.R;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.models.Rating;
import minh.project.multishop.utils.CurrencyFormat;
import minh.project.multishop.utils.DateConverter;
import minh.project.multishop.utils.OnProductRateListener;

import java.util.List;

public class RateProductAdapter extends RecyclerView.Adapter<RateProductAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<OrderItem> itemList;
    private List<Rating> ratingList;

    private OnProductRateListener onProductRateListener;

    public void setOnProductRateListener(OnProductRateListener onProductRateListener) {
        this.onProductRateListener = onProductRateListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public synchronized void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
        notifyDataSetChanged();
    }

    public RateProductAdapter(Context context, List<OrderItem> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rate_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderItem item = itemList.get(position);

        Glide.with(mContext)
                .load(item.getImageURL())
                .placeholder(R.drawable.progress_bar_loading)
                .into(holder.ivThumbnail);

        holder.tvProductName.setText(item.getProductName());
        holder.tvPrice.setText(CurrencyFormat.currencyFormat(item.getSalePrice()));
        holder.tvCount.setText(String.valueOf(item.getCount()));
        holder.tvTotal.setText(CurrencyFormat.currencyFormat(item.getItemTotalPrice()));

        holder.btnSubmit.setOnClickListener(view -> {
            String comment = String.valueOf(holder.edtComment.getText());
            int rateIndex = (int) holder.ratingBar.getRating();
            Log.i("TAO Ở ĐÂY", "onClick: "+comment+" "+rateIndex);
            if(comment.trim().isEmpty()){
                holder.edtComment.setError("Bạn cần phải nhận xét gì đó");
                holder.edtComment.requestFocus();
                return;
            }
            onProductRateListener.onRateProduct(item.getProductID(),comment.trim(),rateIndex,holder.btnSubmit, holder.edtComment, holder.ratingBar);
        });

        if(null == ratingList){
            return;
        }

        Rating rating = ratingList.get(position);
        if(null!=rating){
            holder.edtComment.setText(rating.getComment());
            holder.ratingBar.setRating((float) rating.getRate());
            holder.btnSubmit.setText(mContext.getString(R.string.already_rate, DateConverter.DateTimeFormat(rating.getUpdated_at())));

            holder.edtComment.setClickable(false);
            holder.edtComment.setCursorVisible(false);
            holder.edtComment.setFocusable(false);
            holder.edtComment.setFocusableInTouchMode(false);
            holder.btnSubmit.setClickable(false);
            holder.ratingBar.setIsIndicator(true);
        }
    }

    @Override
    public int getItemCount() {
        return null == itemList || itemList.isEmpty() ? 0 : itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText edtComment;
        MaterialButton btnSubmit;
        RatingBar ratingBar;
        RelativeLayout layoutProductInfo;

        ImageView ivThumbnail;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvCount;
        TextView tvTotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            edtComment = itemView.findViewById(R.id.edtReview);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            layoutProductInfo = itemView.findViewById(R.id.layout_product_view);

            ivThumbnail = layoutProductInfo.findViewById(R.id.iv_product);
            tvProductName = layoutProductInfo.findViewById(R.id.tv_product_name);
            tvPrice = layoutProductInfo.findViewById(R.id.tv_product_price);
            tvCount = layoutProductInfo.findViewById(R.id.tv_discount);
            tvTotal = layoutProductInfo.findViewById(R.id.tv_product_evaluate);
        }
    }
}
