package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.market.R
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 横屏股票K线图
 */
class KlineLandFragment : KlineFragment(), CustomAdapt,
    AbsActivity.OnOrientationChangedListener {


    init {
        LogInfra.Log.d("KlineLandFragment", this.toString())
    }

    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int, land: Boolean): KlineLandFragment {
            val fragment = KlineLandFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_kline, null)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        // 全屏
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        (activity as AbsActivity).addOrientationChangedListener(this)
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 375f
    }

    override fun onChange(landscape: Boolean) {
        if (!landscape) {
            pop()
        }
    }

    override fun onBackPressedSupport(): Boolean {
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AbsActivity).removeOrientationChangedListener(this)

        // 取消全屏
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}