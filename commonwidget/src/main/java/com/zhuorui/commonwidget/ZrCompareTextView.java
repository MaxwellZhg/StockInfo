package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/10
 * Desc:
 */
public class ZrCompareTextView extends AppCompatTextView {

    private int color;
    private String keyword;
    public ZrCompareTextView(Context context) {
        this(context,null);
    }

    public ZrCompareTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZrCompareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZrCompareTextView);
        color = a.getColor(R.styleable.ZrCompareTextView_zr_spancolor,-1);

    }
    public void setText(CharSequence text,String keyword){
         setkeyword(keyword);
        try {
            super.setText(matcherSearchText(color,text.toString(),keyword));
        } catch (Exception e) {
            Log.e("tttttt", e.toString());
            super.setText(text);
        }
    }
    public void setkeyword(String keyword){
      this.keyword = keyword;
    }
    public  SpannableString matcherSearchText(int color, String text, String keywords) {
        SpannableString ss = new SpannableString(text);
        Pattern p =Pattern.compile(keywords);
        Matcher matcher = p.matcher(ss);
        while(matcher.find()){
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;

    }
}
