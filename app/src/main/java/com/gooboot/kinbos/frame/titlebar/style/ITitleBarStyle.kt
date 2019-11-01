package com.gooboot.golib.widget.titlebar.style

import android.graphics.drawable.Drawable

interface ITitleBarStyle {

    fun getTitleGravity(): Int  // 获取标题重心
    fun getDrawablePadding(): Int  // 文字和图标的间距
    fun getChildPadding(): Int // 子 View 内间距
    fun getTitleBarHeight(): Int  // 标题栏高度（默认为ActionBar的高度）
    fun getBackground(): Drawable  // 背景颜色
    fun getLeftIcon(): Drawable  // 返回按钮图标

    fun getLeftColor(): Int  // 左边文本颜色
    fun getTitleColor(): Int  // 标题文本颜色
    fun getRightColor(): Int  // 右边文本颜色

    fun getLeftSize(): Float  // 左边文本大小
    fun getTitleSize(): Float  // 标题文本大小
    fun getRightSize(): Float  // 右边文本大小

    fun isLineVisible(): Boolean  // 分割线是否可见
    fun getLineDrawable(): Drawable  // 分割线背景颜色
    fun getLineSize(): Int  // 分割线的大小

    fun getLeftBackground(): Drawable  // 左边背景资源
    fun getRightBackground(): Drawable  // 右边背景资源
}