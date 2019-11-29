package com.zhuorui.securities.openaccount.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.ZRTitleEditText;
import com.zhuorui.securities.base2app.adapter.BaseListAdapter;
import com.zhuorui.securities.openaccount.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-30 16:10
 * desc   :
 */
public class EmailTipsView extends FrameLayout implements TextWatcher, View.OnFocusChangeListener {


    private ListAdapter adapter;
    private RecyclerView listView;
    private int itemHeight = 0;
    private int maxHeight = 0;
    private int showMaxItem = 4;

    private boolean mFocus;
    private ZRTitleEditText anchor;
    private String[] emails = {"qq.com", "163.com", "126.com", "sina.com", "yahoo.com", "mall.google.com", "hotmail.com", "outlook.com", "shou.com", "vip.sina.com",};

    public EmailTipsView(Context context) {
        this(context, null);
    }

    public EmailTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmailTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemHeight = (int) (getResources().getDisplayMetrics().density * 40);
        maxHeight = itemHeight * showMaxItem;
        listView = new RecyclerView(context);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setAdapter(adapter = new ListAdapter());
        listView.setBackgroundResource(R.color.color_1A6ED2);
        addView(listView);
        setListH();
    }


    public void bindEditText(ZRTitleEditText tv) {
        anchor = tv;
        tv.vEt.addTextChangedListener(this);
        tv.vEt.setOnFocusChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!TextUtils.isEmpty(editable) && editable.toString().endsWith("@") && mFocus) {
            List<String> datas = new ArrayList<>();
            for (String e : emails) {
                datas.add(editable + e);
            }
            adapter.setItems(datas);
            setListH();
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        mFocus = b;
        if (!b) {
            setVisibility(GONE);
        }
    }

    private void setListH() {
        if (adapter.getItemCount() >= showMaxItem) {
            listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, maxHeight));
        } else {
            listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, itemHeight * adapter.getItemCount()));
        }
        adapter.notifyDataSetChanged();
    }

    class ListAdapter extends BaseListAdapter<String> {

        public ListAdapter() {
        }

        @Override
        protected int getLayout(int viewType) {
            return R.layout.item_email_tips;
        }

        @Override
        protected RecyclerView.ViewHolder createViewHolder(View v, int viewType) {
            Holder holder = new Holder(v);
            holder.text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String txt = (String) view.getTag();
                    anchor.setText(txt);
                }
            });
            return holder;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class Holder extends ListItemViewHolder<String> {
            TextView text;

            public Holder(View v) {
                super(v, false, false);
                text = v.findViewById(R.id.text);
            }

            @Override
            protected void bind(String item, int position) {
                String title = getItems().get(position);
                text.setTag(title);
                text.setText(title);
            }
        }

    }


}
