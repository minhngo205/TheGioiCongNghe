package minh.project.multishop.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import minh.project.multishop.R;

public class InternetConnection {
    private final AlertDialog dialog;

    private final MaterialButton btnConfirm;

    private final MaterialButton btnCancel;


    public InternetConnection(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_no_internet, null);

        builder.setView(view);
        builder.setCancelable(true);

        btnConfirm = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }

    public void setConfirmListener(TextView.OnClickListener listener) {
        btnConfirm.setOnClickListener(listener);
    }

    public void setCancelListener(TextView.OnClickListener listener) {
            btnCancel.setOnClickListener(listener);
    }
}
