package minh.project.multishop.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import minh.project.multishop.R;
import minh.project.multishop.databinding.DialogReviewResponseBinding;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.network.dtos.DTORequest.RateProductRequest;
import minh.project.multishop.network.dtos.DTORequest.ReplyReviewRequest;

import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;
import static minh.project.multishop.utils.DateConverter.DateTimeFormat;
import static minh.project.multishop.utils.DateConverter.toDate;

public class EditReviewDialog {
    public static final String replyConfig = "REPLY_REVIEW";
    public static final String editConfig = "EDIT_REVIEW";

    private final DialogReviewResponseBinding mBinding;
    private final AlertDialog dialog;
    private OrderItem item;

    @SuppressLint("SetTextI18n")
    public EditReviewDialog(Context context, Bundle mData, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        mBinding = DialogReviewResponseBinding.inflate(LayoutInflater.from(context));

        if(replyConfig.equals(mData.getString("CONFIG"))){
            String ownerName = mData.getString("REVIEW_OWNER", "Chính bạn");
            mBinding.ratingBar.setVisibility(View.GONE);
            mBinding.layoutProductView.itemProduct.setVisibility(View.GONE);
            mBinding.header.setText("Trà lời đánh giá của: "+ownerName);
        }

        if(editConfig.equals(mData.getString("CONFIG"))){
            mBinding.btnSubmit.setEnabled(false);
            mBinding.btnSubmit.setClickable(false);

            item = mData.getParcelable("PRODUCT_DATA");
            setProductInfo(item, context);

            String comment = mData.getString("COMMENT");
            mBinding.edtReview.setText(comment);
            mBinding.edtReview.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mBinding.btnSubmit.setEnabled(s.toString().trim().equals(comment) || s.toString().trim().length() != 0);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            int rate = mData.getInt("RATE");
            mBinding.ratingBar.setRating((float)rate);

            long create_at = mData.getLong("CREAT_AT");
            long update_at = mData.getLong("UPDATE_AT");

            if(update_at > create_at){
                mBinding.header.setText("Đã cập nhật: "+ DateTimeFormat(toDate(update_at)));
            } else {
                mBinding.header.setText("Đã đánh giá: "+ DateTimeFormat(toDate(create_at)));
            }
        }

        builder.setView(mBinding.getRoot());
        builder.setCancelable(cancelable);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setProductInfo(OrderItem item, Context context) {
        mBinding.layoutProductView.tvTitle.setText(item.getProductName());
        mBinding.layoutProductView.tvSalePrice.setText(currencyFormat(item.getSalePrice()));
        Glide.with(context)
                .load(item.getImageURL())
                .placeholder(R.drawable.progress_bar_loading)
                .into(mBinding.layoutProductView.ivProduct);
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
        mBinding.btnSubmit.setOnClickListener(listener);
    }

    /**
     * set the listener for the Cancel button
     *
     * @param listener set the listener for the Cancel button
     */
    public void setCancelListener(TextView.OnClickListener listener) {
            mBinding.btnCancel.setOnClickListener(listener);
    }

    public RateProductRequest getRatingRequest() {
        String comment = mBinding.edtReview.getText().toString().trim();
        int rate = (int) mBinding.ratingBar.getRating();
        return new RateProductRequest(item.getProductID(), comment, rate);
    }


    public ReplyReviewRequest getReplyRequest() {
        String comment = mBinding.edtReview.getText().toString().trim();
        if(0 == comment.length()){
            return null;
        }

        return new ReplyReviewRequest(comment);
    }
}
