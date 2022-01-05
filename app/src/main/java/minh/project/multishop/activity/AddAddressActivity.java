package minh.project.multishop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import minh.project.multishop.database.entity.UserInfo;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityAddAddressBinding;

public class AddAddressActivity extends AppCompatActivity {

    private ActivityAddAddressBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        View viewRoot = mBinding.getRoot();
        setContentView(viewRoot);

        mBinding.rlUseCurrentLocation.setOnClickListener(view -> initDataFromDB());
        mBinding.btnSaveAddress.setOnClickListener(view -> changeAddress());
        mBinding.toolbar.tvTitle.setText("Thay đổi thông tin");
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if(null != intent){
            UserInfo userInfo = intent.getParcelableExtra("CURRENT_INFO");
            mBinding.setUserInfo(userInfo);
            return;
        }
        initDataFromDB();
    }

    private void initDataFromDB() {
        UserInfo userInfo = UserDBRepository.getInstance().getUserInfo();
        if(null == userInfo){
            Toast.makeText(this, "Không có thông tin hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }
        mBinding.setUserInfo(userInfo);
    }

    private void changeAddress() {
        String newName = mBinding.edtName.getText().toString();
        String newAdd = mBinding.edtAddress1.getText().toString();
        String newPhone = mBinding.edtMobileNo.getText().toString();

        if(newName.trim().isEmpty()){
            mBinding.edtName.setError("Cần có tên khách hàng");
            mBinding.edtName.requestFocus();
            return;
        }
        if(newAdd.trim().isEmpty()){
            mBinding.edtAddress1.setError("Cần có địa chỉ giao hàng");
            mBinding.edtAddress1.requestFocus();
            return;
        }
        if(newPhone.trim().isEmpty()){
            mBinding.edtMobileNo.setError("Cần có số điện thoại");
            mBinding.edtMobileNo.requestFocus();
            return;
        }

        UserInfo userInfo = new UserInfo(newName,newAdd,newPhone);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("ADDRESS_INFO",userInfo);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}