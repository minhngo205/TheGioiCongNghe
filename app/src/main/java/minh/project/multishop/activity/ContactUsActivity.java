package minh.project.multishop.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import minh.project.multishop.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContactUsBinding mBinding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.title.tvTitle.setText("Về chúng tôi");
    }
}