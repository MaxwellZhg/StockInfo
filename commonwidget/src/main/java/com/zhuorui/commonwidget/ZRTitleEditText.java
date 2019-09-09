package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.zhuorui.commonwidget.impl.IZRTitleView;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-23 10:09
 * desc   :
 */
public class ZRTitleEditText extends FrameLayout implements View.OnFocusChangeListener, TextWatcher, IZRTitleView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private TextView vTitle;
    public EditText vEt;
    public ImageView vRightIcon;
    private int mOrientation = VERTICAL;
    private boolean mTitleBaseline = false;
    private boolean mShowDivider = false;
    private boolean mRightIconVisible = false;
    private int mRightIconSrc = 0;
    private int mRightIconWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mRightIconHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mEditType = 0;

    public ZRTitleEditText(Context context) {
        this(context, null);
    }

    public ZRTitleEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRTitleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRTitleEditText);
        mOrientation = a.getInt(R.styleable.ZRTitleEditText_zr_teditOrientation, mOrientation);
        mEditType = a.getInt(R.styleable.ZRTitleEditText_zr_teditType, mEditType);
        mTitleBaseline = a.getBoolean(R.styleable.ZRTitleEditText_zr_titleWidthBaseline, mTitleBaseline);
        mShowDivider = a.getBoolean(R.styleable.ZRTitleEditText_zr_dividerVisible, mShowDivider);
        mRightIconVisible = a.getBoolean(R.styleable.ZRTitleEditText_zr_iconVisible, mRightIconVisible);
        mRightIconWidth = a.getDimensionPixelOffset(R.styleable.ZRTitleEditText_zr_iconWidth, mRightIconWidth);
        mRightIconHeight = a.getDimensionPixelOffset(R.styleable.ZRTitleEditText_zr_iconHight, mRightIconHeight);
        mRightIconSrc = a.getResourceId(R.styleable.ZRTitleEditText_zr_iconSrc, mRightIconSrc);
        String title = a.getString(R.styleable.ZRTitleEditText_zr_teditTitle);
        String text = a.getString(R.styleable.ZRTitleEditText_zr_teditText);
        String hiht = a.getString(R.styleable.ZRTitleEditText_zr_teditHint);
        a.recycle();
        initView();
        orientationChange(title, text, TextUtils.isEmpty(hiht) ? "请输入" + title : hiht);
        if (mTitleBaseline) {
            post(new Runnable() {
                @Override
                public void run() {
                    titleBasel();
                }
            });
        }
    }

    private void initView() {
        removeAllViews();
        inflate(getContext(), mOrientation == VERTICAL ? R.layout.layout_title_edittext_vertical : R.layout.layout_title_edittext_horizontal, this);
        vTitle = findViewById(R.id.tv_title);
        vEt = findViewById(R.id.et_edittext);
        vEt.setOnFocusChangeListener(this);
        vEt.addTextChangedListener(this);
    }

    private void titleBasel() {
        ViewParent parent = getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            for (int i = 0, l = group.getChildCount(); i < l; i++) {
                View v = group.getChildAt(i);
                if (v != ZRTitleEditText.this && v instanceof IZRTitleView) {
                    ((IZRTitleView) v).setTitleWidth(vTitle.getWidth());
                }
            }

        }
    }

    public void setOrientation(int orientation) {
        if (orientation == mOrientation) return;
        mOrientation = orientation;
        String title = vTitle != null ? vTitle.getText().toString() : "";
        String text = vEt != null ? vEt.getText().toString() : "";
        String hint = vEt != null ? vEt.getHint().toString() : "";
        initView();
        orientationChange(title, text, hint);
    }

    private void orientationChange(String title, String text, String hint) {
        setEditTextType(mEditType);
        setTitle(title);
        setText(text);
        setHint(hint);
        setRightIcon();
        setDivider();
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }

    public void setHint(String hiht) {
        vEt.setHint(hiht);
    }

    public void setText(String text) {
        vEt.setText(text);
    }

    private void setDivider() {
        if (!mShowDivider) return;
        ConstraintLayout rootView = findViewById(R.id.root_view);
        View v = getDividerView(rootView.getChildCount());
        rootView.addView(v);
        ConstraintSet mConstraintSet = new ConstraintSet();
        mConstraintSet.clone(rootView);
        mConstraintSet.connect(v.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        mConstraintSet.connect(v.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        mConstraintSet.connect(v.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        mConstraintSet.applyTo(rootView);
    }

    private View getDividerView(int parentChildCount) {
        View v = new View(getContext());
        v.setId(parentChildCount + 1);
        v.setBackgroundColor(Color.parseColor("#CCCCCC"));
        int h = (int) (getResources().getDisplayMetrics().density * 0.6);
        ViewGroup.LayoutParams lp = new LayoutParams(0, h);
        v.setLayoutParams(lp);
        return v;
    }

    public void setTitleWidth(int width) {
        if (width > 0) {
            ViewGroup.LayoutParams params = vTitle.getLayoutParams();
            params.width = width;
            vTitle.setLayoutParams(params);
        }
    }

    private void setRightIcon() {
        vRightIcon = findViewById(R.id.iv_right_icon);
        if (vRightIcon == null) return;
        if (mRightIconVisible) {
            vRightIcon.setVisibility(VISIBLE);
            ViewGroup.LayoutParams params = vRightIcon.getLayoutParams();
            params.width = mRightIconWidth;
            params.height = mRightIconHeight;
            vRightIcon.setImageResource(mRightIconSrc);
        } else {
            vRightIcon.setVisibility(GONE);
        }
    }

    public String getText() {
        String str = vEt.getText().toString();
        if (mEditType != 0 && !TextUtils.isEmpty(str)) {
            str = str.replace(" ", "");
        }
        return str;
    }


    private void setEditTextType(int type) {
        mEditType = type;
        switch (mEditType) {
            case 1:
                vEt.setMaxLines(1);
                vEt.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                vEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(22)});
                CardTextWatcher.bind(vEt, 22);
                break;
            case 2:
                vEt.setMaxLines(1);
                vEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                vEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26)});
                CardTextWatcher.bind(vEt, 26);
                break;
            case 3:
                vEt.setMaxLines(1);
                vEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 4:
                CardTextWatcher.bind(vEt);
                vEt.setMaxLines(1);
                break;

        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
