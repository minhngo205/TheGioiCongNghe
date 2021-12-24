package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import minh.project.multishop.R;
import minh.project.multishop.activity.EditInfoActivity;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityEditInfoBinding;
import minh.project.multishop.models.UserProfile;
import minh.project.multishop.network.repository.UserNetRepository;
import minh.project.multishop.utils.DateConverter;

import java.util.Calendar;

public class EditInfoViewModel extends BaseActivityViewModel<EditInfoActivity> {

    private static final String TAG = EditInfoViewModel.class.getSimpleName();
    private final ActivityEditInfoBinding mBinding;
    private final UserDBRepository dbRepository;
    private final UserNetRepository netRepository;
    private final User mUser;
    private String Username;
    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
        myCalendar.set(Calendar.YEAR, i);
        myCalendar.set(Calendar.MONTH, i1);
        myCalendar.set(Calendar.DAY_OF_MONTH, i2);
        updateLabel();
    };

    private void updateLabel() {
        mBinding.edtBirthDay.setText(DateConverter.AppDateFormat(myCalendar.getTime()));
    }

    /**
     * constructor
     *
     * @param editInfoActivity Activity object
     */
    public EditInfoViewModel(EditInfoActivity editInfoActivity) {
        super(editInfoActivity);
        mBinding = editInfoActivity.getBinding();
        dbRepository = UserDBRepository.getInstance();
        netRepository = UserNetRepository.getInstance();
        mUser = dbRepository.getCurrentUser();
    }

    @Override
    public void initView() {
        mBinding.btnDeactivate.setOnClickListener(mActivity);
        mBinding.btnSaveProFile.setOnClickListener(mActivity);
        mBinding.edtBirthDay.setOnClickListener(mActivity);
        getInfo();
    }

    private void getInfo() {
        netRepository.getUserProfile(mUser.getAccToken()).observe(mActivity, this::InitInfo);
    }

    private void InitInfo(UserProfile userProfile) {
        if(null == userProfile){
            mBinding.setUserInfo(dbRepository.getUserInfo());
            Username = dbRepository.getUserInfo().getName();
            return;
        }

        Long updateFromDB = dbRepository.getUserInfo().getUpdated_at();
        Long updateFromAPI = DateConverter.fromDate(userProfile.getUpdated_at());
        Log.i(TAG, "InitInfo: From DB: "+updateFromDB+" | From API: "+updateFromAPI);
        if(!updateFromAPI.equals(updateFromDB)){
            netRepository.getUserProfile(mUser.getAccToken()).observe(mActivity, userProfile1 -> {
                if(null != userProfile1){
                    dbRepository.insertUserInfo(userProfile1.castToInfo());
                    mBinding.setUserInfo(userProfile1.castToInfo());
                    Username = userProfile.getName();
                }
            });
        } else {
            mBinding.setUserInfo(dbRepository.getUserInfo());
            Username = dbRepository.getUserInfo().getName();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.btnDeactivate:{
                Intent returnIntent = new Intent();
                returnIntent.putExtra("NAME",Username);
                mActivity.setResult(Activity.RESULT_OK,returnIntent);
                mActivity.finish();
                break;
            }
            case R.id.btnSaveProFile:{
                saveProfile();
                break;
            }
            case R.id.edtBirthDay:{
                new DatePickerDialog(mActivity, R.style.MySpinnerDatePickerStyle, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
            default: break;
        }
    }

    private void saveProfile() {
        String newName = mBinding.edtName.getText().toString();
        String newAdd = mBinding.edtAddress.getText().toString();
        String newEmail = mBinding.edtEmail.getText().toString();
        String newPhone = mBinding.edtMobileNo.getText().toString();
        String newDOB = mBinding.edtBirthDay.getText().toString();

        if(newName.trim().isEmpty()){
            mBinding.edtName.setError("Cần có tên khách hàng");
            mBinding.edtName.requestFocus();
            return;
        }
        if(newAdd.trim().isEmpty()){
            mBinding.edtAddress.setError("Cần có địa chỉ giao hàng");
            mBinding.edtAddress.requestFocus();
            return;
        }
        if(newPhone.trim().isEmpty()){
            mBinding.edtMobileNo.setError("Cần có số điện thoại");
            mBinding.edtMobileNo.requestFocus();
            return;
        }
        if(newEmail.trim().isEmpty()){
            mBinding.edtEmail.setError("Cần có thông tin email");
            mBinding.edtEmail.requestFocus();
            return;
        }

        UserProfile updateProfile = new UserProfile(newEmail,newName,newAdd,newPhone,newDOB);
        netRepository.updateInfoData(mUser.getAccToken(),updateProfile).observe(mActivity, profile -> {
            if(null == profile){
                Toast.makeText(mActivity, "Không thể cập nhật thông tin lên server", Toast.LENGTH_SHORT).show();
                return;
            }

            dbRepository.updateInfo(profile.castToInfo());
            InitInfo(profile);
            Toast.makeText(mActivity, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        });
    }
}
