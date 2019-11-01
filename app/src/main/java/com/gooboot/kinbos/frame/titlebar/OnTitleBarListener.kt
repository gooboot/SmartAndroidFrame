package com.gooboot.golib.widget.titlebar

import android.app.Activity
import android.content.Context
import android.view.View

abstract class OnTitleBarListener :ITitleListener{
    override fun onClickLeftButon(view: View, context: Context) {
        (context as Activity).finish()
    }

    override fun onClickTitle(view: View) {
        //TODO 标题点击事件
    }

    override fun onClickRightButton(view: View) {
        //TODO 右边点击事件
    }
}