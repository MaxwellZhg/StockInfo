package com.zhuorui.commonwidget.common

import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.BR
import com.zhuorui.commonwidget.R
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import kotlinx.android.synthetic.main.fragment_my_web_view.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/26
 * Desc:
 */
class ZRMyWebViewFragment :AbsSwipeBackNetFragment<com.zhuorui.commonwidget.databinding.FragmentMyWebViewBinding,ZRMyWebViewModel,ZRMyWebView,ZRMyWebViewPresenter>(),ZRMyWebView{
    override val layout: Int
        get() = R.layout.fragment_my_web_view
    override val viewModelId: Int
        get() = BR.viewModel
   override val createPresenter: ZRMyWebViewPresenter
       get() = ZRMyWebViewPresenter()
    override val createViewModel: ZRMyWebViewModel?
        get() =  ViewModelProviders.of(this).get(ZRMyWebViewModel::class.java)
    override val getView: ZRMyWebView
        get() = this


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        //重新加载 点击网页里面的链接还是在当前的webview里跳转。不跳到浏览器那边
        webview.webViewClient =  object :WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                 return true
            }

        }
        //支持js
        webview.settings.javaScriptEnabled = true
        // 解决图片不显示
        webview.settings.blockNetworkImage = false
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        //自适应屏幕
        webview.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webview.settings.loadWithOverviewMode = true
        //设置可以支持缩放
        webview.settings.setSupportZoom(false)
        //扩大比例的缩放
        webview.settings.useWideViewPort = false
        //设置是否出现缩放工具
        webview.settings.builtInZoomControls = false
        webview.loadUrl("https://www.baidu.com/?tn=18029102_2_dg")
    }

    companion object{
        fun newInstance(type: Int?): ZRMyWebViewFragment {
            val fragment = ZRMyWebViewFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

}