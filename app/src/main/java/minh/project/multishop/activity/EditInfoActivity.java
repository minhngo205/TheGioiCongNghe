package minh.project.multishop.activity;

import android.os.Bundle;
import android.view.View;
import minh.project.multishop.activity.viewmodel.EditInfoViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityEditInfoBinding;

public class EditInfoActivity extends BaseActivity implements View.OnClickListener {

    private EditInfoViewModel viewModel;
    private ActivityEditInfoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEditInfoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        viewModel = new EditInfoViewModel(this);
        viewModel.initView();
    }

    public ActivityEditInfoBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onClick(View view) {
        viewModel.onClickEvent(view.getId());
    }
}