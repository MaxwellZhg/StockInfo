package com.zhuorui.securities.personal.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.R2
import com.zhuorui.securities.personal.ui.model.SettingData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc: 设置中心adapter
 */
class SettingDataAdapter: BaseListAdapter<SettingData>() {
    override fun getLayout(viewType: Int): Int {
       return R.layout.item_setting_info
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
          return ViewHolderInfo(v,false,false);
      }
    inner class ViewHolderInfo(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SettingData>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_info)
        lateinit var tv_info: TextView
        @BindView(R2.id.view_cent)
        lateinit var view_cent: View
        override fun bind(item: SettingData?, position: Int) {
            tv_info.text=item?.title
            if(item?.choose!!){
                tv_info.setCompoundDrawablesWithIntrinsicBounds(null,null,ResUtil.getDrawable(R.mipmap.icon_ensure),null)
            }else{
                tv_info.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
            }
            when(position){
                items.size-1->{
                  view_cent.visibility=View.INVISIBLE
                }
                else ->{
                    view_cent.visibility=View.VISIBLE
                }
            }
            tv_info.setOnClickListener{
                for (item in items){
                    item.choose=false
                }
                item.choose=true
                notifyDataSetChanged()
            }
        }

    }

    fun getTips():String?{
        for (item in items){
            if (item.choose){
                return item.title
            }
        }
        return items[0].title
    }

}