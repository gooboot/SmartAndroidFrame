package com.gooboot.golib.widget.popup.window

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import com.gooboot.golib.utils.ViewUtils

class GoWindow private constructor(context: Context):PopupWindow(){
    private val popupController:PopupController

    init {
        this.popupController = PopupController(context,this)
    }

    override fun getWidth(): Int {
        return this.popupController.popupView!!.measuredWidth
    }

    override fun getHeight(): Int {
        return this.popupController.popupView!!.height
    }

    override fun dismiss() {
        super.dismiss()
        this.popupController.setBackGroundLevel(1.0f)
    }

    interface ViewInterface {
        fun getChildView(view: View, layoutResId: Int)
    }

    class Builder(context: Context) {
        private val params: PopupController.PopupParams
        private var listener: ViewInterface? = null

        init {
            params = PopupController.PopupParams(context)
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        fun setView(layoutResId: Int): Builder {
            params.mView = null
            params.layoutResId = layoutResId
            return this
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        fun setView(view: View): Builder {
            params.mView = view
            params.layoutResId = 0
            return this
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        fun setViewOnclickListener(listener: ViewInterface): Builder {
            this.listener = listener
            return this
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        fun setWidthAndHeight(width: Int, height: Int): Builder {
            params.mWidth = width
            params.mHeight = height
            return this
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        fun setBackGroundLevel(level: Float): Builder {
            params.isShowBg = true
            params.bg_level = level
            return this
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        fun setOutsideTouchable(touchable: Boolean): Builder {
            params.isTouchable = touchable
            return this
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        fun setAnimationStyle(animationStyle: Int): Builder {
            params.isShowAnim = true
            params.animationStyle = animationStyle
            return this
        }

        fun create(): GoWindow {
            val popupWindow = GoWindow(params.mContext)
            params.apply(popupWindow.popupController)
            if (listener != null && params.layoutResId !== 0) {
                listener!!.getChildView(popupWindow.popupController.popupView!!, params.layoutResId)
            }
            ViewUtils.measureWidthAndHeight(popupWindow.popupController.popupView!!)
            return popupWindow
        }
    }
}