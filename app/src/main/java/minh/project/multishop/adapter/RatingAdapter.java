package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import minh.project.multishop.R;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.dialog.EditReviewDialog;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.models.Rating;
import minh.project.multishop.network.repository.RatingRepository;
import minh.project.multishop.utils.DateConverter;

import java.util.List;

import static minh.project.multishop.dialog.EditReviewDialog.editConfig;
import static minh.project.multishop.dialog.EditReviewDialog.replyConfig;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Rating> ratingList;
    private OrderItem productInfo;

    public void setProductInfo(OrderItem productInfo) {
        this.productInfo = productInfo;
    }

    public RatingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_review,parent,false));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Rating rate = ratingList.get(position);

        holder.tvRate.setText(String.valueOf(rate.getRate()));
        holder.tvName.setText(rate.getUser().name);
        holder.tvDate.setText(DateConverter.DateTimeFormat(rate.getCreated_at()));
        holder.tvComment.setText(rate.getComment());
        if(!rate.getResponses().isEmpty()){
            holder.layoutResponse.setVisibility(View.VISIBLE);
            holder.lvRep.setLayoutManager(new LinearLayoutManager(mContext));
            holder.lvRep.setAdapter(new ReviewReplyAdapter(rate.getResponses()));
        }
        if(null != UserDBRepository.getInstance().getUserInfo()
                && isCurrentUser(rate)){
            holder.ivMenu.setVisibility(View.VISIBLE);
            holder.ivMenu.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(mContext,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_review, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()){
                        case R.id.nav_update:
                            editReview(rate);
                            return true;
                        case R.id.nav_reply:
                            replyReview(rate);
                            return true;
                        default: return false;
                    }
                });
                popupMenu.show();
            });
        }
    }

    private void editReview(Rating rate) {
        Bundle data = new Bundle();
        data.putString("CONFIG",editConfig);

        data.putString("COMMENT", rate.getComment());
        data.putInt("RATE", rate.getRate());
        data.putLong("CREATE_AT",DateConverter.fromDate(rate.getCreated_at()));
        data.putLong("UPDATE_AT",DateConverter.fromDate(rate.getCreated_at()));

        data.putParcelable("PRODUCT_DATA",productInfo);

        EditReviewDialog editDialog = new EditReviewDialog(mContext,data,true);
        editDialog.setConfirmListener(v -> {
            User mUser = UserDBRepository.getInstance().getCurrentUser();
            RatingRepository.getInstance().rateProduct(mUser.getAccToken(),editDialog.getRatingRequest()).observe(ProcessLifecycleOwner.get(), rating -> {
                if(null == rating){
                    Toast.makeText(mContext, "Bạn không thể đánh giá lúc này", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Bạn đã cập nhật đánh giá thành công", Toast.LENGTH_SHORT).show();
                ((Activity) mContext).recreate();
                editDialog.dismiss();
            });
        });
        editDialog.setCancelListener(v -> editDialog.dismiss());
        editDialog.show();
    }

    private void replyReview(Rating rate) {
        Bundle data = new Bundle();

        data.putString("CONFIG",replyConfig);
        data.putString("REVIEW_OWNER",rate.getUser().name);

        EditReviewDialog replyDialog = new EditReviewDialog(mContext,data,true);
        replyDialog.setConfirmListener(v -> {
            User mUser = UserDBRepository.getInstance().getCurrentUser();
            if (null == replyDialog.getReplyRequest()){
                Toast.makeText(mContext, "Bạn phải nhập gì đó", Toast.LENGTH_SHORT).show();
                return;
            }
            RatingRepository.getInstance().ReplyReview(mUser.getAccToken(), rate.getId(), replyDialog.getReplyRequest()).observe(ProcessLifecycleOwner.get(), replyReviewResponse -> {
                if(null == replyReviewResponse){
                    Toast.makeText(mContext, "Bạn không thể trả lời đánh giá lúc này", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(mContext, "Bạn đã trả lời đánh giá thành công", Toast.LENGTH_SHORT).show();
                ((Activity) mContext).recreate();
                replyDialog.dismiss();
            });
        });
        replyDialog.setCancelListener(v -> replyDialog.dismiss());
        replyDialog.show();
    }

    private boolean isCurrentUser(Rating rate){
        return rate.getUser().id == UserDBRepository.getInstance().getUserInfo().getId();
    }

    @Override
    public int getItemCount() {
        return null == ratingList || ratingList.isEmpty() ? 0 : ratingList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRate;
        TextView tvName;
        TextView tvDate;
        TextView tvComment;
        RecyclerView lvRep;
        LinearLayout layoutResponse;
        ImageView ivMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRate = itemView.findViewById(R.id.tvProductReviewRating);
            tvName = itemView.findViewById(R.id.tvProductReviewCmt);
            tvDate = itemView.findViewById(R.id.tvProductReviewDuration);
            tvComment = itemView.findViewById(R.id.tvProductReviewSubHeading);
            layoutResponse = itemView.findViewById(R.id.layout_response);
            lvRep = itemView.findViewById(R.id.list_response);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }
    }
}