package com.gooboot.golib.widget.titlebar

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.gooboot.golib.R
import com.gooboot.golib.widget.titlebar.style.ITitleBarStyle
import com.gooboot.golib.widget.titlebar.style.TitleBarLightStyle
import com.gooboot.golib.widget.titlebar.style.TitleBarNightStyle
import com.gooboot.golib.widget.titlebar.style.TitleBarTransparentStyle
import com.orhanobut.logger.Logger

class TitleBar(context: Context,attrs: AttributeSet?,defStyleAttr:Int) :FrameLayout(context,attrs,defStyleAttr),Runnable,View.OnClickListener{
    var barListener:ITitleListener? = null
    var sDefaultStyle: ITitleBarStyle? = null
    var currentStyle: ITitleBarStyle? = null
    var leftView:TextView
    var rightView:TextView
    var titleView:TextView
    var mainLayout:LinearLayout
    var lineView:View
    private var isInited:Boolean = false

    init {
        this.leftView = ViewBuilder.leftView(context)
        this.titleView = ViewBuilder.titleView(context)
        this.rightView = ViewBuilder.rightView(context)
        this.mainLayout = ViewBuilder.mainLayout(context)
        this.lineView = ViewBuilder.lineView(context)

        this.initStyle(attrs)
        this.initBar()
    }

    constructor(context: Context):this(context,null,0)
    constructor(context: Context,attrs:AttributeSet?):this(context,attrs!!,0)

    fun initBar(){
        this.mainLayout.addView(this.leftView)
        this.mainLayout.addView(this.titleView)
        this.mainLayout.addView(this.rightView)

        this.addView(this.mainLayout,0)
        this.addView(this.lineView,1)

        this.isInited = true
        this.post(this)
    }

    /**
     * 初始化样式
     */
    fun initStyle(attrs:AttributeSet?){
        if (sDefaultStyle == null)sDefaultStyle = TitleBarLightStyle(context.applicationContext)
        val array = context.obtainStyledAttributes(attrs,R.styleable.TitleBarStyle)
        when(array.getInt(R.styleable.TitleBarStyle_barStyle,0)){
            0x10->this.currentStyle = TitleBarLightStyle(context)
            0x20->this.currentStyle = TitleBarNightStyle(context)
            0x30->this.currentStyle = TitleBarTransparentStyle(context)
            else->this.currentStyle = this.sDefaultStyle
        }

        setTitleGravity(sDefaultStyle!!.getTitleGravity())
        // 设置文字和图标之间的间距
        setDrawablePadding(sDefaultStyle!!.getDrawablePadding())
        // 设置子 View 内间距
        setChildPadding(sDefaultStyle!!.getChildPadding())

        //设置标题
        if (array.hasValue(R.styleable.TitleBarStyle_leftTitle))this.setLeftTitle(array.getString(R.styleable.TitleBarStyle_leftTitle)!!)
        if (array.hasValue(R.styleable.TitleBarStyle_rightTitle))this.setRightTitle(array.getString(R.styleable.TitleBarStyle_rightTitle)!!)
        if (array.hasValue(R.styleable.TitleBarStyle_title))
            this.setTitle(array.getString(R.styleable.TitleBarStyle_title)!!)
        else{
            if (context is Activity){
                // 获取清单文件中的 android:label 属性值
                val label = (context as Activity).title
                if (label != null && "" != label.toString()) {
                    try {
                        val packageManager = context.packageManager
                        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                        //如果当前 Activity 没有设置 android:label 属性，则默认会返回 APP 名称，则需要过滤掉
                        if (label.toString() != packageInfo.applicationInfo.loadLabel(packageManager).toString()) {
                            // 设置标题
                            setTitle(label)
                        }
                    } catch (ignored: PackageManager.NameNotFoundException) {
                        Logger.d(ignored.printStackTrace())
                    }
                }
            }
        }

        //设置Icon
        if (array.hasValue(R.styleable.TitleBarStyle_leftIcon))
            this.setLeftIcon(ViewBuilder.getDrawable(context,array.getResourceId(R.styleable.TitleBarStyle_leftIcon,0)))
        else{
            if (array.getBoolean(R.styleable.TitleBarStyle_showBackButton,currentStyle!!.getLeftIcon() != null)){
                this.setLeftIcon(this.currentStyle!!.getLeftIcon())
            }
        }
        if (array.hasValue(R.styleable.TitleBarStyle_rightIcon))this.setRightIcon(array.getResourceId(R.styleable.TitleBarStyle_rightIcon,0))

        //设置文本颜色
        this.setLeftColor(array.getColor(R.styleable.TitleBarStyle_leftTextColor,currentStyle!!.getLeftColor()))
        this.setRightColor(array.getColor(R.styleable.TitleBarStyle_rightTextColor,currentStyle!!.getRightColor()))
        this.setTitleColor(array.getColor(R.styleable.TitleBarStyle_titleColor,currentStyle!!.getTitleColor()))

        //设置文本大小
        this.setLeftSize(array.getDimensionPixelSize(R.styleable.TitleBarStyle_leftTextSize,currentStyle!!.getLeftSize().toInt()).toFloat())
        this.setRightSize(array.getDimensionPixelSize(R.styleable.TitleBarStyle_rightTextSize,currentStyle!!.getRightSize().toInt()).toFloat())
        this.setTitleSize(array.getDimensionPixelSize(R.styleable.TitleBarStyle_titleSize,currentStyle!!.getTitleSize().toInt()).toFloat())

        //设置背景图片
        if (array.hasValue(R.styleable.TitleBarStyle_leftBackground))
            this.setLeftBackground(array.getDrawable(R.styleable.TitleBarStyle_leftBackground)!!)
        else
            this.setLeftBackground(currentStyle!!.getLeftBackground())

        if (array.hasValue(R.styleable.TitleBarStyle_rightBackground))
            this.setRightBackground(array.getDrawable(R.styleable.TitleBarStyle_rightBackground)!!)
        else
            this.setRightBackground(currentStyle!!.getRightBackground())

        //设置分割线
        if(array.hasValue(R.styleable.TitleBarStyle_lineColor))
            this.setLineDrawable(array.getDrawable(R.styleable.TitleBarStyle_lineColor)!!)
        else
            this.setLineDrawable(currentStyle!!.getLineDrawable())

        this.setLineVisible(array.getBoolean(R.styleable.TitleBarStyle_lineVisible,currentStyle!!.isLineVisible()))
        this.setLineSize(array.getDimensionPixelSize(R.styleable.TitleBarStyle_lineSize,currentStyle!!.getLineSize()))

        array.recycle()

        //设置默认背景
        if (this.background == null)ViewBuilder.setBackground(this,currentStyle!!.getBackground())
    }

    /**
     * 设置标题重心
     */
    fun setTitleGravity(gravity: Int): TitleBar {
        var gravity = gravity
        // 适配 Android 4.2 新特性，布局反方向（开发者选项 - 强制使用从右到左的布局方向）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            gravity = Gravity.getAbsoluteGravity(gravity, resources.configuration.layoutDirection)
        }
        titleView.gravity = gravity
        return this
    }

    /**
     * 设置文字和图标的间距
     */
    fun setDrawablePadding(px: Int): TitleBar {
        leftView.compoundDrawablePadding = px
        titleView.compoundDrawablePadding = px
        rightView.compoundDrawablePadding = px
        post(this)
        return this
    }

    /**
     * 设置子 View 内间距
     */
    fun setChildPadding(px: Int): TitleBar {
        leftView.setPadding(px, 0, px, 0)
        titleView.setPadding(px, 0, px, 0)
        rightView.setPadding(px, 0, px, 0)
        post(this)
        return this
    }

    /**
     * 设置监听事件
     */
    fun setOnTitleBarListener(l:OnTitleBarListener){
        this.barListener = l
        this.leftView.setOnClickListener(this)
        this.titleView.setOnClickListener(this)
        this.rightView.setOnClickListener(this)
    }

    /**
     * 设置文本标题
     */
    private fun setText(tv:TextView,title:CharSequence){
        tv.text = title
        post(this)
    }

    fun setLeftTitle(title:CharSequence):TitleBar{
        this.setText(this.leftView,title)
        return this
    }

    fun setRightTitle(title:CharSequence):TitleBar{
        this.setText(this.rightView,title)
        return this
    }

    fun setTitle(title:CharSequence):TitleBar{
        this.setText(this.titleView,title)
        return this
    }

    /**
     * 设置图标 Icon
     */
    fun setLeftIcon(drawable: Drawable?): TitleBar {
        leftView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        post(this)
        return this
    }

    fun setLeftIcon(id: Int): TitleBar {
        return setLeftIcon(ViewBuilder.getDrawable(context, id))
    }

    fun setRightIcon(id: Int): TitleBar {
        return setRightIcon(ViewBuilder.getDrawable(context, id))
    }

    fun setRightIcon(drawable: Drawable?): TitleBar {
        rightView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        post(this)
        return this
    }

    /**
     * 设置文本颜色 color
     */
    fun setLeftColor(color:Int):TitleBar{
        this.leftView.setTextColor(color)
        return this
    }

    fun setRightColor(color:Int):TitleBar{
        this.rightView.setTextColor(color)
        return this
    }

    fun setTitleColor(color:Int):TitleBar{
        this.titleView.setTextColor(color)
        return this
    }

    /**
     * 设置文本大小 size
     */
    private fun setTextSize(tv: TextView,size:Float){
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,size)
        post(this)
    }

    fun setLeftSize(size: Float):TitleBar{
        this.setTextSize(this.leftView,size)
        return this
    }

    fun setRightSize(size:Float):TitleBar{
        this.setTextSize(this.rightView,size)
        return this
    }

    fun setTitleSize(size:Float):TitleBar{
        this.setTextSize(this.titleView,size)
        return this
    }

    /**
     * 设置背景
     */
    fun setLeftBackground(drawable: Drawable): TitleBar {
        ViewBuilder.setBackground(leftView, drawable)
        post(this)
        return this
    }

    fun setRightBackground(drawable: Drawable):TitleBar{
        ViewBuilder.setBackground(rightView, drawable)
        post(this)
        return this
    }

    /**
     * 设置分割线背景
     */
    fun setLineDrawable(drawable: Drawable): TitleBar {
        ViewBuilder.setBackground(lineView, drawable)
        return this
    }

    fun setLineVisible(visible: Boolean): TitleBar {
        lineView.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setLineSize(px: Int): TitleBar {
        val layoutParams = lineView.layoutParams
        layoutParams.height = px
        lineView.layoutParams = layoutParams
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int,heightMeasureSpec: Int) {
        var widthMeasure:Int = widthMeasureSpec
        var heightMeasure:Int = heightMeasureSpec

        // 设置 TitleBar 的宽度
        when (View.MeasureSpec.getMode(widthMeasure)) {
            View.MeasureSpec.AT_MOST,
            View.MeasureSpec.UNSPECIFIED -> widthMeasure =
                View.MeasureSpec.makeMeasureSpec(
                    View.MeasureSpec.getSize(widthMeasure),
                    View.MeasureSpec.EXACTLY
                )
            View.MeasureSpec.EXACTLY -> {
            }
            else -> {
            }
        }

        // 设置 TitleBar 的高度
        when (View.MeasureSpec.getMode(heightMeasure)) {
            View.MeasureSpec.AT_MOST,
            View.MeasureSpec.UNSPECIFIED -> {
                val background = background
                // 如果当前背景是一张图片的话
                if (background is BitmapDrawable) {
                    mainLayout.layoutParams.height = sDefaultStyle!!.getTitleBarHeight()
                    // 算出标题栏的宽度和图片的宽度之比例
                    val ratio =
                        View.MeasureSpec.getSize(widthMeasure).toDouble() / background.getIntrinsicWidth().toDouble()
                    heightMeasure = View.MeasureSpec.makeMeasureSpec(
                        (ratio * background.getIntrinsicHeight()).toInt(),
                        View.MeasureSpec.EXACTLY
                    )
                } else {
                    // 获取 TitleBar 默认高度
                    heightMeasure = View.MeasureSpec.makeMeasureSpec(
                        sDefaultStyle!!.getTitleBarHeight(),
                        View.MeasureSpec.EXACTLY
                    )
                }
            }
            View.MeasureSpec.EXACTLY -> {
            }
            else -> {
            }
        }

        super.onMeasure(widthMeasure, heightMeasure)
    }

    override fun run() {
        if (!this.isInited)return
        // 当前标题 View 的重心必须是水平居中的
        if (titleView.getGravity() and Gravity.CENTER_HORIZONTAL != 0) {
            // 更新中间标题的内边距，避免向左或者向右偏移
            val leftSize = leftView.getWidth()
            val rightSize = rightView.getWidth()
            if (leftSize != rightSize) {
                if (leftSize > rightSize) {
                    titleView.setPadding(0, 0, leftSize - rightSize, 0)
                } else {
                    titleView.setPadding(rightSize - leftSize, 0, 0, 0)
                }
            }
        }

        // 更新 View 状态
        leftView.isEnabled = ViewBuilder.hasTextViewContent(leftView)
        titleView.isEnabled = ViewBuilder.hasTextViewContent(titleView)
        rightView.isEnabled = ViewBuilder.hasTextViewContent(rightView)
    }

    override fun onClick(v: View?) {
        when{
            v == leftView   ->this.barListener?.onClickLeftButon(v,context)
            v == titleView  ->this.barListener?.onClickTitle(v)
            v == rightView  ->this.barListener?.onClickRightButton(v)
        }
    }
}