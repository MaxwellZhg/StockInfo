package com.zhuorui.securities.openaccount.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Desc:
 */
interface OAOhterNotesView : AbsView {
    fun toNext()
    fun getSwitchStatus(): List<Boolean>
}