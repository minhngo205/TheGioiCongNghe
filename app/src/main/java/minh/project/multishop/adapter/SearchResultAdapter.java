package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import minh.project.multishop.R;
import minh.project.multishop.activity.ProductDetailActivity;
import minh.project.multishop.models.Product;

import java.util.List;

import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Product> productList;

    public SearchResultAdapter(Context context) {
        this.mContext = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_search_res,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);

        Glide.with(mContext)
                .load(product.getImageThumbnail())
                .placeholder(R.drawable.progress_bar_loading)
                .into(holder.productThumb);

        if (product.getIsDiscount()==0){
            holder.productPrice.setText("");
        } else {
            holder.productPrice.setText(currencyFormat(product.getProductPrice()));
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.salePrice.setText(currencyFormat(product.getSalePrice()));

        holder.productName.setText(product.getProductName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("productID",product.getID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return null == productList || productList.isEmpty() ? 0 : productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView productThumb;
        TextView productName;
        TextView productPrice;
        TextView salePrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productThumb = itemView.findViewById(R.id.iv_product);
            productName = itemView.findViewById(R.id.tv_title);
            productPrice = itemView.findViewById(R.id.product_price);
            salePrice = itemView.findViewById(R.id.tv_sale_price);
        }
    }
}
