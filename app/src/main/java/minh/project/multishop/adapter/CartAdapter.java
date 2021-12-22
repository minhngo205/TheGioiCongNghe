package minh.project.multishop.adapter;

import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import minh.project.multishop.R;
import minh.project.multishop.activity.ProductDetailActivity;
import minh.project.multishop.models.CartItem;
import minh.project.multishop.utils.OnItemModifyListener;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private final Context mContext;

    private List<CartItem> cartItems;

    public CartAdapter(Context context) {
        this.mContext = context;
    }

    private OnItemModifyListener onItemModifyListener;

    public void setOnItemModifyListener(OnItemModifyListener onItemModifyListener) {
        this.onItemModifyListener = onItemModifyListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shoppingcart_list,parent,false));
    }

    @Override
    public int getItemCount() {
        return cartItems == null || cartItems.isEmpty() ? 0 : cartItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        Glide.with(mContext)
                .load(cartItem.getProduct().getImageThumbnail())
                .placeholder(R.drawable.progress_bar_loading)
                .into(holder.imgThumbnail);

        holder.tvName.setText(cartItem.getProduct().getProductName());
//        holder.tvDescription.setText(cartItem.getProduct().getShortDes());
        if(cartItem.getProduct().isDiscount!=0){
            holder.tvProductPrice.setText(currencyFormat(cartItem.getProduct().getProductPrice()));
            holder.tvProductPrice.setPaintFlags(holder.tvProductPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.tvSalePrice.setText(currencyFormat(cartItem.getProduct().getSalePrice()));
        holder.textQuantity.setText(String.valueOf(cartItem.getCount()));
        holder.checkItemChoose.setChecked(cartItem.isChoose());

        holder.textAdd.setOnClickListener(v -> onItemModifyListener.onItemQuantityAdd(position, holder.textQuantity));
        holder.textReduce.setOnClickListener(v -> onItemModifyListener.onItemQuantityReduce(position, holder.textQuantity));
        holder.checkItemChoose.setOnClickListener(view -> {
            cartItem.setChoose(((CheckBox) view).isChecked());
            onItemModifyListener.onItemChoose(position, ((CheckBox) view).isChecked());
        });

        View.OnClickListener redirectToProductDetail = view -> {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("productID",cartItem.getProduct().getID());
            mContext.startActivity(intent);
        };

        holder.tvName.setOnClickListener(redirectToProductDetail);
        holder.imgThumbnail.setOnClickListener(redirectToProductDetail);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imgThumbnail;
        TextView tvName;
//        TextView tvDescription;
        TextView tvProductPrice;
        TextView tvSalePrice;
        TextView textQuantity;
//        TextView textReduce;
        ImageView textReduce;
//        TextView textAdd;
        ImageView textAdd;
        CheckBox checkItemChoose;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.iv_show_pic);
//            imgThumbnail = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tv_product_name);
//            tvName = itemView.findViewById(R.id.tvProductName);
//            tvDescription = itemView.findViewById(R.id.tv_configuration);
            tvProductPrice = itemView.findViewById(R.id.tv_display_price);
//            tvProductPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvSalePrice = itemView.findViewById(R.id.tv_price);
//            tvSalePrice = itemView.findViewById(R.id.tvPrice);

//            textQuantity = itemView.findViewById(R.id.tv_show_quantity);
            textQuantity = itemView.findViewById(R.id.edtQty);

//            textReduce = itemView.findViewById(R.id.iv_sub);
            textReduce = itemView.findViewById(R.id.ivDecreaseQuantity);
//            textAdd = itemView.findViewById(R.id.iv_add);
            textAdd = itemView.findViewById(R.id.ivIncreaseQuantity);
            checkItemChoose = itemView.findViewById(R.id.ck_chose);
        }
    }
}
