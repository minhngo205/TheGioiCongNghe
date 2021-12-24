package minh.project.multishop.activity.customview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import minh.project.multishop.R;

public class SearchView extends LinearLayout {

    private AutoCompleteTextView editText;

    private ImageView ivClear;

    private ImageView ivSearch;

    private ImageView ivBack;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_search_box, this);
        editText = view.findViewById(R.id.et_search);
        ivClear = view.findViewById(R.id.iv_clear);
        ivSearch = view.findViewById(R.id.iv_search);
        ivBack = view.findViewById(R.id.iv_back);
        ivSearch.setOnClickListener(v -> requestKeyBoard(context, 0));
        ivClear.setOnClickListener(v -> editText.setText(""));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setThreshold(3);
        requestKeyBoard(context, 200);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener editorActionListener) {
        editText.setOnEditorActionListener(editorActionListener);
    }

    public void setIvBackListener(OnClickListener listener) {
        this.ivBack.setOnClickListener(listener);
    }

    public void setEditTextSelectedListener(AdapterView.OnItemClickListener listener){
        editText.setOnItemClickListener(listener);
    }

    private void requestKeyBoard(Context context, int delay) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, delay);
    }

    public void setAdapter(ArrayAdapter<String> adapter){
        editText.setAdapter(adapter);
    }

    public AutoCompleteTextView getEditText() {
        return editText;
    }
}
