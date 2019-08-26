package com.zhuorui.securities.openaccount.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.openaccount.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-26 13:44
 * desc   :
 */
public class OpenAccountStepView extends FrameLayout {
    public OpenAccountStepView(Context context) {
        this(context,null);
    }

    public OpenAccountStepView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OpenAccountStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.layout_open_account_step_view,this);
    }
}
