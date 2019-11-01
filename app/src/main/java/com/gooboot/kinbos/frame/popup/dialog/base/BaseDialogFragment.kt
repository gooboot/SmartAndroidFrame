package com.gooboot.golib.widget.popup.dialog.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment :DialogFragment(){
    private var dialogView:View? = null

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        if (this.getLayoutResId() > 0){
            this.dialogView = inflater.inflate(this.getLayoutResId(),container,false)
        }else if (this.getLayoutView() != null){
            this.dialogView = this.getLayoutView()
        }
        return this.dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (this.dialog != null){
            this.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            this.dialog?.setCancelable(this.isCancelable)
            this.dialog?.setCanceledOnTouchOutside(this.isCancelableOutside())
            this.dialog?.setOnKeyListener{_, keyCode, event ->
                keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN && !isCancelable
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (this.dialog == null)return
        val window = this.dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setWindowAnimations(this.getAnimResId())
        val params = window?.attributes
        params?.width = if (getDialogWidth() > 0)getDialogWidth() else WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = if (getDialogHeight() > 0)getDialogHeight() else WindowManager.LayoutParams.WRAP_CONTENT
        params?.dimAmount = this.getDimAmount()
        params?.gravity = this.getGravity()
        window?.attributes = params
    }

    /** 获取Dialog的布局View **/
    abstract fun getLayoutView():View?

    /** 获取Dialog的布局资源ID **/
    abstract fun getLayoutResId():Int

    open protected fun isCancelableOutside():Boolean{
        return true
    }

     open fun getAnimResId():Int{
        return 0
    }

    protected fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    //设置屏幕透明度 0.0f~1.0f(完全透明~完全不透明)
    fun getDimAmount(): Float {
        return 0.2f
    }

    protected fun getGravity(): Int {
        return Gravity.CENTER
    }
}