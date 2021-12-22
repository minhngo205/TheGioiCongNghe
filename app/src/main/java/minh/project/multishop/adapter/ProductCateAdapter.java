package minh.project.multishop.adapter;

import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import minh.project.multishop.R;
import minh.project.multishop.models.Product;

public class ProductCateAdapter extends RecyclerView.Adapter<ProductCateAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Product> productList;

    public ProductCateAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        Glide.with(mContext)
                .load(product.getImageThumbnail())
                .placeholder(R.drawable.progress_bar_loading)
                .into(holder.imageView);

        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText(currencyFormat(product.getSalePrice()));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("productID",product.getID());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_product);
            tvName = itemView.findViewById(R.id.text_name);
            tvPrice = itemView.findViewById(R.id.text_price);
        }
    }
}
