package com.zhuorui.securities.openaccount.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.ZRTitleEditText;
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
        itemHeight = (int) (getResources().getDisplayMetrics().density *40);
        maxHeight = itemHeight * showMaxItem;
        listView = new RecyclerView(context);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setAdapter(adapter = new ListAdapter(context));
        listView.setBackgroundResource(R.color.app_bule);
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
            adapter.setDatas(datas);
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

    public class ListAdapter extends RecyclerView.Adapter<Holder> {
        List<String> datas = new ArrayList<>();
        Context context;

        public ListAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_email_tips, parent, false);
            Holder holder = new Holder(v);
            holder.text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String txt = (String) view.getTag();
                    anchor.setText(txt);
                }
            });
            return new Holder(v);
        }

        public void onBindViewHolder(@NonNull Holder holder, int position) {
            String title = datas.get(position).toString();
            holder.text.setTag(title);
            holder.text.setText(title);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return datas.size();
        }


        public void setDatas(List<String> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }


    }


    class Holder extends RecyclerView.ViewHolder {
        TextView text;

        public Holder(View v) {
            super(v);
            text = v.findViewById(R.id.text);
        }

    }


}
