package com.zhuorui.securities.openaccount.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.R2
import com.zhuorui.securities.openaccount.model.HelpCenterInfoData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/5
 * Desc:帮助中心信息适配
 */
class HelpCenterInfoAdapter(type:Int) : BaseListAdapter<HelpCenterInfoData>() {
    private val header = 0x00
    private val info = 0x01
    private val bottom = 0x02
    private var type =type
    var onResolveItemCallback: OnResolveItemCallback? = null
    var onUnResolveItemCallback: OnUnResolveItemCallback? = null
    override fun getLayout(viewType: Int): Int {
          return when(viewType){
              header->{
                  R.layout.item_head_helpcenter_info
              }
              bottom->{
                R.layout.item_bottom_helpcenter_info
              }
              else->{
                  R.layout.item_helpcenter_info
              }
          }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
         return when (viewType){
             header->{
                 ViewHolderHeader(v,false,false)
             }
             bottom->{
                 ViewHolderBottom(v,false,false)
             }
             else->{
                 ViewHolderInfo(v,false,false)
             }
         }
    }

    inner class ViewHolderHeader(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<HelpCenterInfoData>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_helpcenter_tips)
        lateinit var tv_helpcenter_tips: TextView
        override fun bind(item: HelpCenterInfoData?, position: Int) {
            when(type){
                1->{
                    tv_helpcenter_tips.text=ResUtil.getString(R.string.openaccount_by_license)
                }
                2->{
                    tv_helpcenter_tips.text=ResUtil.getString(R.string.openaccount_by_info)
                }
                3->{
                    tv_helpcenter_tips.text=ResUtil.getString(R.string.openaccount_by_time)
                }
                4->{
                    tv_helpcenter_tips.text=ResUtil.getString(R.string.openaccount_by_age)
                }
            }
        }

    }

    inner class ViewHolderInfo(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<HelpCenterInfoData>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_theme)
        lateinit var tv_theme: TextView
        @BindView(R2.id.tv_tips_info)
        lateinit var tv_tips_info: TextView
        override fun bind(item: HelpCenterInfoData?, position: Int) {
            when(type){
                1->{
                    tv_theme.text=item?.title
                    tv_tips_info.text=item?.tips
                }
                else->{
                    tv_theme.visibility=View.GONE
                    tv_tips_info.text=item?.tips
                }
            }
        }

    }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<HelpCenterInfoData>(v, needClick, needLongClick) {
        @BindView(R2.id.tv_resolved)
        lateinit var tv_resolved: TextView
        @BindView(R2.id.tv_unresolved)
        lateinit var tv_unresolved: TextView
        init {
            tv_resolved.setOnClickListener(this)
            tv_unresolved.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            when(v){
                tv_resolved->{
                    onResolveItemCallback?.onResolveItem(position, getItem(position), v)
                    tv_resolved.background=ResUtil.getDrawable(R.drawable.bg_helpcenter_info_resolve)
                    tv_resolved.setCompoundDrawablesWithIntrinsicBounds(ResUtil.getDrawable(R.mipmap.resolve_blue),null,null,null)
                    ResUtil.getColor(R.color.color_1A6ED2)?.let { tv_resolved.setTextColor(it) }
                }
                tv_unresolved->{
                    onUnResolveItemCallback?.onUnresolveItem(position, getItem(position), v)
                    tv_unresolved.background=ResUtil.getDrawable(R.drawable.bg_helpcenter_info_noresolve)
                    tv_unresolved.setCompoundDrawablesWithIntrinsicBounds(ResUtil.getDrawable(R.mipmap.unresolve_red),null,null,null)
                    ResUtil.getColor(R.color.color_ca0000)?.let { tv_unresolved.setTextColor(it) }
                }
                else ->{
                    super.onClick(v)
                }
            }
        }
        override fun bind(item: HelpCenterInfoData?, position: Int) {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->{
                header
            }
            itemCount-1->{
                bottom
            }
            else->{
                info
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnResolveItemCallback {
        fun onResolveItem(pos: Int, item: HelpCenterInfoData?, view: View)
    }
    interface OnUnResolveItemCallback{
        fun onUnresolveItem(pos: Int, item: HelpCenterInfoData?, view: View)
    }

}