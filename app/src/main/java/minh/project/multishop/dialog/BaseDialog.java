package minh.project.multishop.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import minh.project.multishop.R;

public class BaseDialog {
    /**
     * CONFIRM_BUTTON
     */
    public static final String CONFIRM_BUTTON = "ConfirmButton";

    /**
     * CONTENT
     */
    public static final String CONTENT = "Content";

    /**
     * CANCEL_BUTTON
     */
    public static final String CANCEL_BUTTON = "CancelButton";

    private final AlertDialog dialog;

    private final TextView btnConfirm;

    private final TextView btnCancel;

    private final boolean cancelFlag;

    public BaseDialog(@NonNull Context context, Bundle mData, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.diag_base, null);
        builder.setView(view);
        builder.setCancelable(cancelable);
        cancelFlag = cancelable;

        TextView inputEdit = view.findViewById(R.id.diag_content);
        inputEdit.setText(R.string.confirm_log_out);
        setTextContent(inputEdit, mData.getString(CONTENT), R.string.welcome);

        btnConfirm = view.findViewById(R.id.confirm_view);
        btnConfirm.setText(R.string.confirm);
        btnConfirm.setOnClickListener(v -> dismiss());
        setTextContent(btnConfirm, mData.getString(CONFIRM_BUTTON), R.string.confirm);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancel = view.findViewById(R.id.cancel_view);
        if (cancelable) {
            btnCancel.setOnClickListener(v -> dismiss());
            setTextContent(btnCancel, mData.getString(CANCEL_BUTTON), R.string.cancel);
        } else {
            view.findViewById(R.id.split_line).setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
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

    private void setTextContent(TextView textview, String inputdata, @StringRes int resId) {
        if (inputdata == null) {
            textview.setText(resId);
        } else {
            textview.setText(inputdata);
        }
    }

    /**
     * set the listener for the confirm button
     *
     * @param listener set the listener for the confirm button
     */
    public void setConfirmListener(TextView.OnClickListener listener) {
        btnConfirm.setOnClickListener(listener);
    }

    /**
     * set the listener for the Cancel button
     *
     * @param listener set the listener for the Cancel button
     */
    public void setCancelListener(TextView.OnClickListener listener) {
        if (cancelFlag) {
            btnCancel.setOnClickListener(listener);
        }
    }
}
