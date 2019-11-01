package com.gooboot.golib.widget.popup.dialog

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.gooboot.golib.Go
import com.gooboot.golib.R
import com.gooboot.golib.utils.ScreenUtils.getScreenHeight
import com.gooboot.golib.utils.ScreenUtils.getScreenWidth
import com.gooboot.golib.widget.popup.dialog.base.BaseDialogFragment
import com.gooboot.golib.widget.popup.dialog.base.DialogAttributes
import com.gooboot.golib.widget.popup.dialog.base.IDialog

class GoDialog private constructor(context: Context):BaseDialogFragment(),IDialog {
    private var context:Context
    private lateinit var attributes: DialogAttributes
    var dismissListener: IDialog.OnDismissListener? = null

    init {
        this.context = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.attributes.setDialogView(view)
    }

    override fun getLayoutView(): View?  = this.attributes.getDialogView()

    override fun getLayoutResId(): Int = this.attributes.layoutRes

    override fun getContext(): Context {
        return this.context
    }

    override fun getAnimResId(): Int {
        return this.attributes.animRes
    }

    override fun isCancelableOutside(): Boolean {
        return this.attributes.isCancelableOutside
    }

    /**
     * 返回 OK 按钮
     */
    fun getButtonOK():Button?{
        return this.attributes.btnOK
    }

    /**
     * 返回 Cancel 按钮
     */
    fun getButtonCancel():Button?{
        return this.attributes.btnCancel
    }

    /**
     * 解决 Can not perform this action after onSaveInstanceState问题
     *
     * @param manager FragmentManager
     * @param tag     tag
     */
    fun showAllowingLoss(manager: FragmentManager, tag: String) {
        try {
            val cls = DialogFragment::class.java
            val mDismissed = cls.getDeclaredField("mDismissed")
            mDismissed.isAccessible = true
            mDismissed.set(this, false)
            val mShownByMe = cls.getDeclaredField("mShownByMe")
            mShownByMe.isAccessible = true
            mShownByMe.set(this, true)
        } catch (e: Exception) {
            //调系统的show()方法
            show(manager, tag)
            return
        }

        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        this.dismissListener?.onDismiss(this)
        super.onDestroy()
    }

    override fun dismiss() {
        //防止横竖屏切换时 getFragmentManager置空引起的问题：
        //Attempt to invoke virtual method 'android.app.FragmentTransaction
        //android.app.FragmentManager.beginTransaction()' on a null object reference
        if (fragmentManager == null) return
        super.dismissAllowingStateLoss()
    }

    fun getAttributes(): DialogAttributes {
        return this.attributes
    }

    /**
     * GoDialog 构造器
     */
    class Builder(context: Context?) {
        val params: DialogAttributes
        private var dismissListener: IDialog.OnDismissListener? = null
        private var context:Context
        private var dialog:GoDialog
        private val FTag = "dialogFragment"

        init {
            requireNotNull(context) { "Context can't be null" }
            require(context is Activity) { "Context must be Activity" }
            this.context = context
            this.dialog = create()
            params = DialogAttributes(dialog)
            params.layoutRes = R.layout.layout_dialog_default
            this.dialog.attributes = params
        }

        fun getDialog():GoDialog{
            return this.dialog
        }

        /**
         * 设置DialogView
         *
         * @param layoutRes 布局文件
         * @return Builder
         */
        fun setDialogView( layoutRes: Int,useDefaultConfig:Boolean = true): Builder {
            params.layoutRes = layoutRes
            this.params.setDialogView(LayoutInflater.from(context).inflate(layoutRes,null),useDefaultConfig)
            return this
        }

        /**
         * 设置DialogView
         *
         * @param dialogView View
         * @return Builder
         */
        fun setDialogView(dialogView: View): Builder {
            params.setDialogView(dialogView)
            return this
        }

        /**
         * 返回dialog的layout view
         */
        fun getDialogView():View{
            return this.params.getDialogView()!!
        }

        /**
         * 设置屏幕宽度百分比
         *
         * @param percentage 0.0f~1.0f
         * @return Builder
         */
        fun setScreenWidthP(percentage: Float): Builder {
            params.dialogWidth = (getScreenWidth(context as Activity) * percentage).toInt()
            return this
        }

        /**
         * 设置屏幕高度百分比
         *
         * @param percentage 0.0f~1.0f
         * @return Builder
         */
        fun setScreenHeightP(percentage: Float): Builder {
            params.dialogHeight = (getScreenHeight(context as Activity) * percentage).toInt()
            return this
        }

        /**
         * 设置Dialog的宽度
         *
         * @param width 宽度
         * @return Builder
         */
        fun setWidth(width: Int): Builder {
            params.dialogWidth = width
            return this
        }

        /**
         * 设置Dialog的高度
         *
         * @param height 高度
         * @return Builder
         */
        fun setHeight(height: Int): Builder {
            params.dialogHeight = height
            return this
        }

        /**
         * 设置背景色色值
         *
         * @param percentage 0.0f~1.0f 1.0f为完全不透明
         * @return Builder
         */
        fun setWindowBackgroundP(percentage: Float): Builder {
            params.dimAmount = percentage
            return this
        }

        /**
         * 设置Gravity
         *
         * @param gravity Gravity
         * @return Builder
         */
        fun setGravity(gravity: Int): Builder {
            params.gravity = gravity
            return this
        }

        /**
         * 设置dialog外点击是否可以让dialog消失
         *
         * @param cancelableOutSide true 则在dialog屏幕外点击可以使dialog消失
         * @return Builder
         */
        fun setCancelableOutSide(cancelableOutSide: Boolean): Builder {
            params.isCancelableOutside = cancelableOutSide
            return this
        }

        /**
         * 设置是否屏蔽物理返回键
         *
         * @param cancelable true 点击物理返回键可以让dialog消失；反之不消失
         * @return Builder
         */
        fun setCancelable(cancelable: Boolean): Builder {
            params.cancelable = cancelable
            this.dialog.isCancelable = cancelable
            return this
        }


        /**
         * 监听dialog的dismiss
         *
         * @param dismissListener IDialog.OnDismissListener
         * @return Builder
         */
        fun setOnDismissListener(dismissListener: IDialog.OnDismissListener): Builder {
            this.dismissListener = dismissListener
            return this
        }

        /**
         * 设置dialog的动画效果
         *
         * @param animStyle 动画资源文件
         * @return Builder
         */
        fun setAnimStyle(animStyle: Int): Builder {
            params.animRes = animStyle
            return this
        }

        /**
         * 设置默认右侧点击按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setPositiveButton(onclickListener: IDialog.OnClickListener): Builder {
            return setPositiveButton("确定", onclickListener)
        }

        /**
         * 设置默认右侧点击按钮及文字
         *
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setPositiveButton(btnStr: String, onclickListener: IDialog.OnClickListener): Builder {
            params.mPositiveButtonListener = onclickListener
            params.positiveStr = btnStr
            return this
        }

        /**
         * 设置左侧按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setNegativeButton(onclickListener: IDialog.OnClickListener): Builder {
            return setNegativeButton("取消", onclickListener)
        }

        /**
         * 设置左侧文字及按钮
         *
         * @param btnStr          文字
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setNegativeButton(btnStr: String, onclickListener: IDialog.OnClickListener): Builder {
            params.mNegativeButtonListener = onclickListener
            params.negativeStr = btnStr
            return this
        }

        /**
         * 设置默认dialog的title
         *
         * @param title 标题
         * @return Builder
         */
        fun setTitle(title: String): Builder {
            params.titleStr = title
            return this
        }

        /**
         * 设置默认dialog的内容
         *
         * @param content 内容
         * @return Builder
         */
        fun setContent(content: String): Builder {
            params.contentStr = content
            return this
        }

        private fun create(): GoDialog {
            val dialog = GoDialog(context)
            dialog.dismissListener = dismissListener
            return dialog
        }

        /**
         * 展示Dialog
         *
         * @return SYDialog
         */
        fun show(): GoDialog {
            if (params.layoutRes <= 0 && params.getDialogView() == null) {
                //如果没有设置布局 提供默认设置
                setDefaultOption()
            }
            //判空
            if (context == null) return dialog
            val activity = context as FragmentActivity
            //如果Activity正在关闭或者已经销毁 直接返回
            val isRefuse = if (Build.VERSION.SDK_INT >= 17)
                activity.isFinishing || activity.isDestroyed
            else
                activity.isFinishing

            if (isRefuse) return dialog
            dialog.showAllowingLoss(activity.supportFragmentManager,FTag)
            return dialog
        }

        /**
         * 设置默认Dialog的配置
         */
        private fun setDefaultOption() {
            params.cancelable = false
            params.isCancelableOutside = false
            params.gravity = Gravity.CENTER
            params.layoutRes = R.layout.layout_dialog_default
            params.dimAmount = 0.5f
            params.dialogWidth = (getScreenWidth(context as Activity) * 0.85f).toInt()
            params.dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT
        }

    }

}