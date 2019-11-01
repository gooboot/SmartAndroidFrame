package com.gooboot.golib.widget.popup.dialog

import android.content.Context
import android.text.TextUtils
import com.gooboot.golib.widget.popup.dialog.base.IDialog

/**
 * @param context               Context
 * @param title                 标题
 * @param content               内容
 * @param btn1Str               左边按钮
 * @param negativeClickListener 左边点击事件
 * @param btn2Str               右边按钮
 * @param positiveClickListener 右边点击事件
 */
object DialogUtil {
    fun createDefaultDialog(
        context: Context,
        title: String?,
        content: String?,
        btn1Str: String?,
        positiveClickListener: IDialog.OnClickListener?,
        btn2Str: String?,
        negativeClickListener: IDialog.OnClickListener?,
        isCancelable:Boolean = true,
        isCancelableOutside:Boolean = true
    ) {
        val builder = GoDialog.Builder(context)
        if (!TextUtils.isEmpty(title)) builder.setTitle(title!!)
        if (!TextUtils.isEmpty(content)) builder.setContent(content!!)
        if (!TextUtils.isEmpty(btn1Str) && positiveClickListener != null)builder.setPositiveButton(btn1Str!!, positiveClickListener)
        if (!TextUtils.isEmpty(btn2Str) && negativeClickListener != null)builder.setNegativeButton(btn2Str!!,negativeClickListener)

        builder.setCancelable(isCancelable).setCancelableOutSide(isCancelableOutside)
        builder.show()
    }
}