package com.gooboot.golib.widget.popup.dialog.base

import android.content.Context

interface IDialog {
    fun dismiss()

    /**
     * Dialog 的按钮点击事件
     */
    interface OnClickListener{
        fun onClick(dialog: IDialog)
    }

    /**
     * 当dialog消失时回调
     */
    interface OnDismissListener{
        fun onDismiss(dialog: IDialog)
    }

    fun getContext():Context
}