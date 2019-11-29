package com.zhuorui.commonwidget.common

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.BR
import com.zhuorui.commonwidget.R
import com.zhuorui.commonwidget.databinding.FragmentWebViewBinding
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import kotlinx.android.synthetic.main.fragment_web_view.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/26
 * Desc:公共web界面
 * */
class ZRWebViewFragment :
    AbsSwipeBackNetFragment<FragmentWebViewBinding, ZRWebViewModel, ZRWebView, ZRWebViewPresenter>(),
    ZRWebView {

    override val layout: Int
        get() = R.layout.fragment_web_view

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: ZRWebViewPresenter
        get() = ZRWebViewPresenter()

    override val createViewModel: ZRWebViewModel?
        get() = ViewModelProviders.of(this).get(ZRWebViewModel::class.java)

    override val getView: ZRWebView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        //重新加载 点击网页里面的链接还是在当前的webview里跳转。不跳到浏览器那边
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                empty_info_view.visibility = View.VISIBLE
            }

        }
        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progress.visibility = View.GONE//加载完网页进度条消失
                } else {
                    progress.visibility = View.VISIBLE//开始加载网页时显示进度条
                    progress.progress = newProgress//设置进度值
                }
            }
        }
        //支持js
        webview.settings.javaScriptEnabled = true
        // 解决图片不显示
        webview.settings.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        webview.loadUrl("http://192.168.1.34/#/adviser")
    }

    companion object {
        fun newInstance(type: Int?): ZRWebViewFragment {
            val fragment = ZRWebViewFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

}