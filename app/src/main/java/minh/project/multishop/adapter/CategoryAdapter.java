package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import minh.project.multishop.R;
import minh.project.multishop.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private final List<Category> categoryList;
    private final Context mContext;
    private OnItemClickListener onItemClickListener;
    private final int showPosition;
    private int selectedItemPosition = 0;
    Integer[] images = new Integer[]{
            R.mipmap.laptop,
            R.mipmap.pc_parts,
            R.mipmap.television,
            R.mipmap.screen,
            R.mipmap.smart_devices,
            R.mipmap.smart_phone,
            R.mipmap.mouse,
            R.mipmap.key_board
    };

    public CategoryAdapter(List<Category> categoryList, Context context, int showPosition) {
        this.categoryList = categoryList;
        this.mContext = context;
        this.showPosition = showPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category_list,parent,false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = categoryList.get(position);

        holder.ivCategory.setImageResource(images[position]);
        holder.cateName.setText(category.getName());
        holder.cateName.setTextColor(ContextCompat.getColor(mContext, R.color.item_catalogue_no_select));

        holder.itemView.setOnClickListener(v -> {
            selectedItemPosition = position;
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
            notifyDataSetChanged();
        });

        if (selectedItemPosition == position){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red_type_1));
            holder.cateName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_bdg_gray));
            holder.cateName.setTextColor(ContextCompat.getColor(mContext, R.color.item_catalogue_no_select));
        }
    }

    @Override
    public int getItemCount() {
        return null == categoryList || categoryList.isEmpty() ? 0 : categoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cateName;
        LinearLayout cardView;
        ImageView ivCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cateName = itemView.findViewById(R.id.catalogue_type);
            ivCategory = itemView.findViewById(R.id.iv_category);
            cardView = itemView.findViewById(R.id.item_category_list);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        /**
         * This function is used to make action when item is clicked.
         *
         * @param position item position
         */
        void onItemClick(int position);
    }
}
