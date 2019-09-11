package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-09-11 15:52
 * desc   :
 */
public class ZRSearchView extends FrameLayout implements TextWatcher, View.OnClickListener {

    private EditText vEt;
    private ImageView vClear;
    private OnKeyChangeListener ml;
    private String lastKey;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ml.onKeyChange(vEt.getText().toString());
        }
    };

    public ZRSearchView(Context context) {
        this(context, null);
    }

    public ZRSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZRSearchView);
        int leftIcon = a.getResourceId(R.styleable.ZRSearchView_zr_left_icon, 0);
        int clearIcon = a.getResourceId(R.styleable.ZRSearchView_zr_clear_icon, 0);
        int background = a.getResourceId(R.styleable.ZRSearchView_zr_background, 0);
        String hint = a.getString(R.styleable.ZRSearchView_zr_hint);
        inflate(context, R.layout.layout_search_view, this);
        vEt = findViewById(R.id.search_view_et);
        vClear = findViewById(R.id.search_view_clear);
        vEt.setHint(hint);
        vEt.setBackgroundResource(background);
        vEt.addTextChangedListener(this);
        vEt.setCompoundDrawablesWithIntrinsicBounds(leftIcon, 0, 0, 0);
        vClear.setOnClickListener(this);
        vClear.setImageResource(clearIcon);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (TextUtils.isEmpty(editable)) {
            vClear.setVisibility(GONE);
        } else {
            vClear.setVisibility(VISIBLE);
        }
        if (ml != null) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, TextUtils.isEmpty(lastKey) || TextUtils.isEmpty(editable) ? 0 : 350);
            lastKey = editable.toString();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == vClear) {
            vEt.setText("");
        }
    }

    public void setKey(String key) {
        vEt.setText(key);
        if (ml != null)
            ml.onKeyChange(key);
    }

    public void setOnKeyChangeListener(OnKeyChangeListener l) {
        ml = l;
    }

    public interface OnKeyChangeListener {
        void onKeyChange(String key);
    }
}
