package com.gooboot.golib.widget.popup.window

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow

class PopupController(context: Context,popupWindow: PopupWindow) {
    private var layoutResId: Int = 0    //布局id
    private var context: Context
    private var popupWindow: PopupWindow
    var popupView: View? = null    //弹窗布局View
    private var layoutView: View? = null
    private var window: Window? = null

    init {
        this.context = context
        this.popupWindow = popupWindow
    }

    fun setView(view:View){
        this.layoutView = view
        this.layoutResId = 0
        this.installContent()
    }

    fun setView(layoutId:Int){
        this.layoutResId = layoutId
        this.layoutView = null
        this.installContent()
    }

    private fun installContent(){
        when{
            this.layoutView != null ->this.popupView = this.layoutView
            this.layoutResId != 0   ->this.popupView = LayoutInflater.from(context).inflate(this.layoutResId,null)
        }
        this.popupWindow.contentView = this.popupView
    }

    private fun setWidthAndHeight(width:Int,height:Int){
        if (width == 0 || height == 0){
            this.popupWindow.width  = ViewGroup.LayoutParams.WRAP_CONTENT
            this.popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }else{
            this.popupWindow.width  = width
            this.popupWindow.height = height
        }
    }

    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    fun setBackGroundLevel(level: Float) {
        this.window = (context as Activity).window
        val params = this.window?.attributes
        params?.alpha = level
        this.window?.attributes = params
    }

    /**
     * 设置动画
     */
    private fun setAnimationStyle(animationStyle: Int) {
        popupWindow.animationStyle = animationStyle
    }

    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private fun setOutsideTouchable(touchable: Boolean) {
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))//设置透明背景
        popupWindow.isOutsideTouchable = touchable//设置outside可点击
        popupWindow.isFocusable = touchable
    }


    internal class PopupParams(var mContext: Context) {
        var layoutResId: Int = 0//布局id
        var mWidth: Int = 0
        var mHeight: Int = 0//弹窗的宽和高
        var isShowBg: Boolean = false
        var isShowAnim: Boolean = false
        var bg_level: Float = 0.toFloat()//屏幕背景灰色程度
        var animationStyle: Int = 0//动画Id
        var mView: View? = null
        var isTouchable = true

        fun apply(controller: PopupController) {
            when{
                mView != null    ->controller.setView(mView!!)
                layoutResId != 0 ->controller.setView(layoutResId)
                else->throw IllegalArgumentException("PopupView's contentView is null")
            }
            controller.setWidthAndHeight(mWidth, mHeight)
            controller.setOutsideTouchable(isTouchable)//设置outside可点击
            if (isShowBg) {
                //设置背景
                controller.setBackGroundLevel(bg_level)
            }
            if (isShowAnim) {
                controller.setAnimationStyle(animationStyle)
            }
        }
    }
}