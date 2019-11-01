package com.gooboot.golib.widget.popup.dialog.base

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
import com.gooboot.golib.R
import com.gooboot.golib.utils.ScreenUtils

class DialogAttributes(dialog: IDialog) {
    var layoutRes: Int = 0
    var dialogWidth: Int = 0
    var dialogHeight: Int = 0
    var dimAmount = 0.2f
    var gravity = Gravity.CENTER
    var isCancelableOutside = true
    var cancelable: Boolean = false
    private var dialogView: View? = null
    var mPositiveButtonListener: IDialog.OnClickListener? = null
    var mNegativeButtonListener: IDialog.OnClickListener? = null
    val mDialog: IDialog
    var titleStr: String? = null//默认标题
    var contentStr: String? = null//默认内容
    var positiveStr: String? = null//右边按钮文字
    var negativeStr: String? = null//左边按钮文字
    var animRes = R.style.translate_style       //Dialog动画style

    var btnOK:Button? = null
    var btnCancel:Button? = null

    init {
        this.mDialog = dialog
    }

    fun setDialogView(view: View,useDefaultConfig:Boolean = true){
        this.dialogView = view
        if (useDefaultConfig)this.dealDefaultDialog(mPositiveButtonListener,mNegativeButtonListener,titleStr,contentStr,negativeStr,positiveStr)
    }

    private fun dealDefaultDialog(
        positiveBtnListener: IDialog.OnClickListener?,
        negativeBtnListener: IDialog.OnClickListener?,
        titleStr: String?,
        contentStr: String?,
        negativeStr: String?,
        positiveStr: String?
    ) {
        if (dialogView == null) return
        this.mNegativeButtonListener = negativeBtnListener
        this.mPositiveButtonListener = positiveBtnListener
        btnOK = dialogView?.findViewById(R.id.btnOK) as Button
        btnCancel = dialogView?.findViewById(R.id.btnCancel) as Button

        fun btnOK(){
            btnOK?.visibility = View.VISIBLE
            btnOK?.text = if (TextUtils.isEmpty(positiveStr)) "确定" else positiveStr
            btnOK?.setOnClickListener { mPositiveButtonListener?.onClick(mDialog) }
        }

        fun btnCancel(){
            btnCancel?.visibility = View.VISIBLE
            btnCancel?.text = if (TextUtils.isEmpty(negativeStr)) "取消" else negativeStr
            btnCancel?.setOnClickListener { mNegativeButtonListener?.onClick(mDialog) }
        }

        when{
            (!TextUtils.isEmpty(positiveStr) && !TextUtils.isEmpty(negativeStr)) -> {btnOK();btnCancel()}
            !TextUtils.isEmpty(positiveStr)                                      -> btnOK()
            !TextUtils.isEmpty(negativeStr)                                      -> btnCancel()
        }

        val tvTitle = dialogView?.findViewById(R.id.dialog_title) as TextView
        val tvContent = dialogView?.findViewById(R.id.dialog_content) as TextView

        if (tvTitle != null) {
            tvTitle.visibility = if (TextUtils.isEmpty(titleStr)) View.GONE else View.VISIBLE
            tvTitle.text = if (!TextUtils.isEmpty(titleStr)) titleStr else "Title"
            if (TextUtils.isEmpty(contentStr) && mDialog != null && mDialog.getContext() != null) {
                tvTitle.minHeight = ScreenUtils.dp2px(mDialog.getContext(), 100F)
                tvTitle.gravity = Gravity.CENTER
                tvTitle.setPadding(0, 10, 0, 0)
            }
        }
        if (tvContent != null) {
            tvContent.visibility = if (TextUtils.isEmpty(contentStr)) View.GONE else View.VISIBLE
            tvContent.text = contentStr
            tvContent.viewTreeObserver.addOnPreDrawListener(ViewTreeObserver.OnPreDrawListener {
                val lineCount = tvContent.lineCount
                if (lineCount >= 3) {
                    //超过三行居左显示
                    tvContent.gravity = Gravity.START
                } else {
                    //默认居中
                    tvContent.gravity = Gravity.CENTER_HORIZONTAL
                    if (TextUtils.isEmpty(titleStr)) {
                        tvContent.setPadding(0, 50, 0, 50)
                    }
                }

                if (TextUtils.isEmpty(titleStr)) {
                    //没有title，只有content
                    tvContent.textSize = 18f
                    if (mDialog.getContext().resources == null) return@OnPreDrawListener true
                }
                true
            })

        }
    }

    fun getDialogView():View?{
        return this.dialogView
    }
}