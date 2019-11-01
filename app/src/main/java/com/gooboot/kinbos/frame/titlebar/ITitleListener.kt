package com.gooboot.golib.widget.titlebar

import android.content.Context
import android.view.View

interface ITitleListener {
    /**
     * 左边按钮点击事件
     */
    fun onClickLeftButon(view:View,context:Context)

    /**
     * 标题点击事件
     */
    fun onClickTitle(view:View)

    /**
     * 右转点击事件
     */
    fun onClickRightButton(view:View)
}