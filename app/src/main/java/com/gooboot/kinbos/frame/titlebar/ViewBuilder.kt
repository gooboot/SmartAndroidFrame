package com.gooboot.golib.widget.titlebar

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

object ViewBuilder {
    fun initTextView(context: Context):TextView{
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        textView.isClickable = true
        textView.isSingleLine = true
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.isEnabled = false
        return textView
    }

    fun leftView(context: Context):TextView{
        return initTextView(context)
    }

    fun rightView(context: Context):TextView{
        return initTextView(context)
    }

    fun titleView(context: Context):TextView{
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        textView.gravity = Gravity.CENTER
        textView.setSingleLine()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE_1_1) {
            // 给标题设置跑马灯效果（仅在标题过长的时候才会显示）
            textView.ellipsize = TextUtils.TruncateAt.MARQUEE
            // 设置跑马灯之后需要设置选中才能有效果
            textView.isSelected = true
        } else {
            textView.ellipsize = TextUtils.TruncateAt.END
        }

        return textView
    }

    fun mainLayout(context: Context):LinearLayout{
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)

        return linearLayout
    }

    fun lineView(context: Context):View{
        val view = View(context)
        view.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1,Gravity.BOTTOM)

        return view
    }

    /**
     * 获取 Drawable 对象
     */
    fun getDrawable(context: Context,id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(id)
        } else {
            context.getDrawable(id)
        }
    }

    /**
     * 设置 View 的背景
     */
    fun setBackground(view: View, background: Drawable) {
        view.background = background
    }

    /**
     * 检查 TextView 的内容是否为空
     */
    fun hasTextViewContent(view: TextView): Boolean {
        if ("" != view.text.toString()) {
            return true
        }
        val drawables = view.compoundDrawables
        for (drawable in drawables) {
            if (drawable != null) {
                return true
            }
        }
        return false
    }
}