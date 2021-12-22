package minh.project.multishop.adapter;

import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import minh.project.multishop.R;
import minh.project.multishop.activity.ProductDetailActivity;
import minh.project.multishop.models.Product;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.MyViewHolder> {
    private final List<Product> productList;
    private final Context mContext;

    public HomeProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_product,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
//        setAnimation(holder.itemView,position);
        Glide.with(mContext)
                .load(product.getImageThumbnail())
                .placeholder(R.drawable.progress_bar_loading)
                .into(holder.imageviewProduct);

        holder.tvName.setText(product.getProductName());
        holder.tvRate.setText(String.valueOf(product.getAvgRate()));
        holder.barRate.setRating((float) product.getAvgRate());

        if(product.getIsDiscount()==0){
            holder.tvPrice.setText("");
            holder.tvSale.setText(currencyFormat(product.getSalePrice()));
            holder.tvDiscount.setVisibility(View.GONE);
        } else {
            holder.tvPrice.setText(currencyFormat(product.getProductPrice()));
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvSale.setText(currencyFormat(product.getSalePrice()));
            holder.tvDiscount.setText("-"+product.getIsDiscount()+"%");
        }

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
        ImageView imageviewProduct;
        TextView tvRate;
        TextView tvName;
        TextView tvPrice;
        TextView tvSale;
        TextView tvDiscount;
        RatingBar barRate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageviewProduct = itemView.findViewById(R.id.image_product);
            tvRate = itemView.findViewById(R.id.text_rate);
            tvName = itemView.findViewById(R.id.text_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvSale = itemView.findViewById(R.id.tv_discount);
            tvDiscount = itemView.findViewById(R.id.tv_countdown);
            barRate = itemView.findViewById(R.id.rating_bar);
        }
    }
}
