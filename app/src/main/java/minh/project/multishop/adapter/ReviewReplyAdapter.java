package minh.project.multishop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import minh.project.multishop.R;
import minh.project.multishop.databinding.ItemReviewResponseBinding;
import minh.project.multishop.network.dtos.DTOmodels.DTOComment;
import minh.project.multishop.utils.DateConverter;

import java.util.List;

public class ReviewReplyAdapter extends RecyclerView.Adapter<ReviewReplyAdapter.MyViewHolder> {

    private final List<DTOComment> responseList;

    public ReviewReplyAdapter(List<DTOComment> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewResponseBinding mBinding = ItemReviewResponseBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DTOComment comment = responseList.get(position);

        if("admin".equals(comment.user.role)){
            holder.mBinding.imageUser.setImageResource(R.mipmap.ava_admin);
            holder.mBinding.textName.setText("Quản trị viên");
        } else {
            holder.mBinding.imageUser.setImageResource(R.mipmap.head_load);
            holder.mBinding.textName.setText(comment.user.name);
        }

        holder.mBinding.textTime.setText(DateConverter.DateTimeFormat(comment.updatedAt));
        holder.mBinding.tvContent.setText(comment.comment);
    }

    @Override
    public int getItemCount() {
        return null == responseList ? 0 : responseList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemReviewResponseBinding mBinding;
        public MyViewHolder(@NonNull ItemReviewResponseBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
