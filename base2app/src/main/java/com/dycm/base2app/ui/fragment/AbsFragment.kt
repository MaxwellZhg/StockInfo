package com.dycm.base2app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.dycm.base2app.util.StatusBarUtil
import com.dycm.base2app.util.ToastUtil
import me.yokeyword.fragmentation.SupportFragment

/**
 *    author : Pengxianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019-05-20 14:13
 *    desc   : 定义基础Fragment
 */
abstract class AbsFragment() : SupportFragment() {

    protected var TAG: String? = null

    private var unbinder: Unbinder? = null
    /**
     * 是否DestroyView()被调用
     *
     * @return isDestroyView
     */
    var isDestroyView: Boolean = false
        private set
    /**
     * 是否Destroy()被调用
     *
     * @return isDestroy
     */
    var isDestroy: Boolean = false
        private set
    /**
     * 是否已解除View的绑定
     *
     * @return isBindView
     */
    var isBindView: Boolean = false
        private set
    /**
     * Fragment的ContentView布局
     *
     * @return layout
     */
    protected abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this.javaClass.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isDestroyView = false
        unbindButterKnife()
        val layout = inflater.inflate(layout, container, false)
        if (rootViewFitsSystemWindowsPadding()){
            layout.setPadding(0, getRootViewFitsSystemWindowsPadding(), 0, 0)
        }
        /*绑定ButterKnife*/
        unbinder = ButterKnife.bind(this, layout)
        isBindView = true
        return layout
    }

    /**
     * 视图是否减去状态栏的高度
     */
    open fun rootViewFitsSystemWindowsPadding(): Boolean {
        return false
    }

    /**
     * 设置视图减去状态栏的高度
     */
    open fun getRootViewFitsSystemWindowsPadding(): Int{
        return StatusBarUtil.getStatusBarHeight(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    protected abstract fun init()

    override fun onDestroyView() {
        isDestroyView = true
        super.onDestroyView()
        unbindButterKnife()
    }

    override fun onDestroy() {
        isDestroy = true
        super.onDestroy()
    }

    /**
     * 解除绑定
     */
    protected fun unbindButterKnife() {
        isBindView = false
        if (unbinder != null) unbinder!!.unbind()
        unbinder = null
    }

    protected fun toast(@StringRes res: Int) {
        ToastUtil.instance.toast(res)
    }

    protected fun toast(str: String?) {
        str?.let { ToastUtil.instance.toast(it) }
    }
}