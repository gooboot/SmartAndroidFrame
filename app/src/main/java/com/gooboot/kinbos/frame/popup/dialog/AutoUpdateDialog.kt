package com.gooboot.golib.widget.popup.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.format.Formatter
import android.view.View
import android.widget.Button
import androidx.core.content.FileProvider
import com.github.lzyzsd.circleprogress.CircleProgress
import com.gooboot.golib.Go
import com.gooboot.golib.R
import com.gooboot.golib.net.http.callback.FileCallback
import com.gooboot.golib.net.http.model.Progress
import com.gooboot.golib.net.http.model.Response
import com.gooboot.golib.net.http.request.GetRequest
import com.gooboot.golib.utils.ApkUtils
import com.gooboot.golib.widget.popup.dialog.base.IDialog
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import java.io.File
import java.text.NumberFormat

/**
  *@功能描述  app自动更新提示框
  *@作者 gooboot
  *@版权 http://www.gooboot.com
  *@创建日期 2019/10/18 0018 11:17
  *@version 1.0
 **/
class AutoUpdateDialog(context: Context,theme:Theme = Theme.ORANGE){
    enum class Theme{ORANGE,GREEN}
    val RQ_INTSALL_APK = 266
    var themeColor:Theme = Theme.ORANGE
    var context:Context
    private lateinit var apkUrl: String
    private var builder:GoDialog.Builder = GoDialog.Builder(context)
    private var numberFormat:NumberFormat
    companion object{
        const val apkName:String = "Go.apk"
        val apkPath:String = "${Go.instance.getDownloadFileDir()}${apkName}"
    }

    init {
        this.context = context
        this.themeColor = theme
        this.numberFormat = NumberFormat.getNumberInstance()
        this.numberFormat.minimumFractionDigits = 2
    }

    fun build(content:String,version:String,apkUrl:String,isForceUpdate:Boolean = false):AutoUpdateDialog{
        val builder = GoDialog.Builder(context)
        builder.setDialogView(if (themeColor == Theme.ORANGE) R.layout.activity_update_type_orange else R.layout.activity_update_type_green,false)
            .setPositiveButton("立即更新",this.okButtonListener)
            .setTitle(version)
            .setContent(content)
        if (!isForceUpdate){
            builder.setNegativeButton(" ",this.cancelButtonListener)
        }else{
            //强制更新下载
            builder.setCancelable(false)
            builder.setCancelableOutSide(false)
        }
        this.apkUrl = apkUrl
        this.builder = builder
        return this
    }

    fun show(){
        this.builder.show()
    }

    /**
     * 确定按钮 事件监听
     */
    private val okButtonListener = object :IDialog.OnClickListener{
        override fun onClick(dialog: IDialog) {
            val view = (dialog as GoDialog).getLayoutView()
            val pg = view!!.findViewById<CircleProgress>(R.id.circle_progress)
            view!!.findViewById<Button>(R.id.btnOK).visibility = View.GONE
            pg.progress = 0
            pg.visibility = View.VISIBLE
            Go.get<File>(apkUrl)
                .tag(apkPath)
                .execute(object : FileCallback(apkName){
                    override fun onSuccess(response: Response<File>) {
                        this@AutoUpdateDialog.builder.getDialog().dismiss()
                        Toasty.success(context!!,"下载更新成功！").show()
                        installAPK(File(apkPath))
                    }

                    override fun downloadProgress(progress: Progress) {
                        pg.max = 100
                        pg.progress = (progress.fraction * 100).toInt()
                    }
                })
        }
    }

    /**
     * 取消 按钮事件监听
     */
    private val cancelButtonListener = object :IDialog.OnClickListener{
        override fun onClick(dialog: IDialog) {
            dialog.dismiss()
            Go.cancel(apkPath)
        }
    }

    fun installAPK(file:File){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT  > Build.VERSION_CODES.N){
            val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.FileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        }else{
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        (context as Activity).startActivityForResult(intent,RQ_INTSALL_APK)
    }
}