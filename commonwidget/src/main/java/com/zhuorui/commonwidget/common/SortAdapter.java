package com.zhuorui.commonwidget.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zhuorui.commonwidget.R;

import java.util.LinkedList;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/19
 * Desc:
 */
public class SortAdapter extends BaseAdapter {
    private Context context;

    private LinkedList<JsonBean> list=new LinkedList<>();

    public SortAdapter(Context context,CommonEnum commonEnum) {
        this.context = context;
    }
    public void addItems(LinkedList<JsonBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void clearItems(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public LinkedList<JsonBean> getList(){
        return  list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder hodler;
        if(convertView==null){
            hodler=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_common_contry_dicts,parent,false);
            hodler.tv_contry=(TextView) convertView.findViewById(R.id.tv_contry);
            hodler.tv_frist_tips=(TextView) convertView.findViewById(R.id.tv_frist_tips);
            hodler.tv_contry_code=(TextView) convertView.findViewById(R.id.tv_contry_code);
            hodler.rl_tips=(RelativeLayout) convertView.findViewById(R.id.rl_tips);
            hodler.checkbox=(CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(hodler);
        }else{
            hodler=(ViewHolder)convertView.getTag();
        }
        hodler.rl_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        hodler.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                list.get(position).setUsed(b);
            }
        });
        JsonBean jsonBean = list.get(position);
        hodler.tv_contry.setText(jsonBean.getCn());
        hodler.tv_contry_code.setText(jsonBean.getNumber());
        String currentWord = jsonBean.getSortLetters() + "";
        if (position > 0) {
            String lastWord = list.get(position - 1).getSortLetters() + "";
            if (currentWord.equals(lastWord)) {
                //首字母相同
                hodler.tv_frist_tips.setVisibility(View.GONE);
                hodler.rl_tips.setVisibility(View.GONE);
                hodler.checkbox.setChecked(jsonBean.isUsed());
            } else {
                //首字母不同
                //由于布局是复用的，所以需要设置可见
                hodler.tv_frist_tips.setVisibility(View.VISIBLE);
                hodler.rl_tips.setVisibility(View.VISIBLE);
                hodler.tv_frist_tips.setText(currentWord);
                hodler.checkbox.setChecked(jsonBean.isUsed());
            }
        } else {
            //第一个
            hodler.rl_tips.setVisibility(View.VISIBLE);
            hodler.tv_frist_tips.setVisibility(View.VISIBLE);
            hodler.tv_frist_tips.setText(currentWord);
            hodler.checkbox.setChecked(jsonBean.isUsed());
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_contry, tv_frist_tips,tv_contry_code;
        RelativeLayout rl_tips;
        CheckBox checkbox;
    }
}