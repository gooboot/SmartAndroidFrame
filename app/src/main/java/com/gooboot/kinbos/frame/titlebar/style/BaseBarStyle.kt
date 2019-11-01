package com.gooboot.golib.widget.titlebar.style

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import com.gooboot.golib.utils.ScreenUtils

abstract class BaseBarStyle(context: Context) :
    ITitleBarStyle {
    private var context:Context

    init {
        this.context = context
    }

    override fun getTitleGravity(): Int {
        return Gravity.CENTER
    }


    override fun getDrawablePadding(): Int {
        return ScreenUtils.dp2px(context,2f)
    }

    override fun getChildPadding(): Int {
        return ScreenUtils.dp2px(context,14f)
    }

    override fun getTitleBarHeight(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // 获取 ActionBar 的高度
            val ta = context.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            val actionBarSize = ta.getDimension(0, 0f).toInt()
            ta.recycle()
            return actionBarSize
        }
        // ActionBar 的高度为 154 px，计算得出为 56 dp
        return ScreenUtils.dp2px(context,56f)
    }

    override fun getLeftSize(): Float {
        return ScreenUtils.sp2px(context,14f).toFloat()
    }

    override fun getTitleSize(): Float {
        return ScreenUtils.sp2px(context,16f).toFloat()
    }

    override fun getRightSize(): Float {
        return ScreenUtils.sp2px(context,14f).toFloat()
    }

    override fun getLineSize(): Int {
        return 1
    }

    protected fun getDrawable(id:Int): Drawable {
        return context.getDrawable(id)!!
    }

}