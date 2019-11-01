package com.gooboot.golib.widget.popup.window

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gooboot.golib.R
import com.gooboot.golib.utils.ViewUtils

class PopManager(context: Context):GoWindow.ViewInterface{
    private var context:Context
    private var viewListener:((view:View,layoutResId:Int)->Unit)? = null

    init {
        this.context = context
    }

    override fun getChildView(view: View, layoutResId: Int) {
        viewListener!!(view,layoutResId)
    }

    private fun buildPopWindow(anchor:View,layoutResId: Int,viewListener:(view: View,layoutResId:Int)->Unit):GoWindow{
        this.viewListener = viewListener
        return GoWindow.Builder(context)
            .setView(layoutResId)
            .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            .setViewOnclickListener(this)
            .create()
    }

    fun popRight(anchor:View,layoutResId: Int,viewListener:(view: View,layoutResId:Int)->Unit){
        val popWindow = this.buildPopWindow(anchor,layoutResId,viewListener)
        popWindow.animationStyle = R.style.AnimPopRight
        popWindow.showAsDropDown(anchor,anchor.width,-anchor.height)
    }

    fun popLeft(anchor:View,layoutResId: Int,viewListener:(view: View,layoutResId:Int)->Unit){
        val popWindow = this.buildPopWindow(anchor,layoutResId,viewListener)
            popWindow.animationStyle = R.style.AnimPopLeft
            popWindow.showAsDropDown(anchor,-anchor.width, -anchor.height)
    }

    fun popUp(anchor:View,layoutResId: Int,viewListener:(view: View,layoutResId:Int)->Unit){
        val popWindow = this.buildPopWindow(anchor,layoutResId,viewListener)
        popWindow.showAsDropDown(anchor, 0, -(popWindow.height + anchor.measuredHeight))
    }

    fun popDown(anchor:View,layoutResId: Int,viewListener:(view: View,layoutResId:Int)->Unit){
        val popWindow = this.buildPopWindow(anchor,layoutResId,viewListener)
        popWindow.animationStyle = R.style.AnimPopDown
        popWindow.showAsDropDown(anchor)
    }

    fun popUpFullScreen(anchor:View,layoutResId: Int,viewListener:(view: View,layoutResId:Int)->Unit){
        val contentView = LayoutInflater.from(context).inflate(layoutResId,null)
        ViewUtils.measureWidthAndHeight(contentView)
        val popWindow = GoWindow.Builder(context)
            .setView(layoutResId)
            .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, contentView.measuredHeight)
            .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
            .setAnimationStyle(R.style.AnimPopUp)
            .setViewOnclickListener(this)
            .create()
        popWindow.showAtLocation((context as Activity).findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0)
    }

}