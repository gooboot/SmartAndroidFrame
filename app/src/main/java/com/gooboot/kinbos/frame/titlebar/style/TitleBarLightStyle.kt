package com.gooboot.golib.widget.titlebar.style

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.gooboot.golib.R

class TitleBarLightStyle(context:Context) : BaseBarStyle(context){
    override fun getBackground(): Drawable {
        return ColorDrawable(-0x1)
    }

    override fun getLeftIcon(): Drawable {
        return getDrawable(R.mipmap.bar_icon_back_black)
    }

    override fun getLeftColor(): Int {
        return -0x99999a
    }

    override fun getTitleColor(): Int {
        return -0xddddde
    }

    override fun getRightColor(): Int {
        return -0x5b5b5c
    }

    override fun isLineVisible(): Boolean {
        return true
    }

    override fun getLineDrawable(): Drawable {
        return ColorDrawable(-0x131314)
    }

    override fun getLeftBackground(): Drawable {
        return SelectorDrawable.Builder()
            .setDefault(ColorDrawable(0x00000000))
            .setFocused(ColorDrawable(0x0C000000))
            .setPressed(ColorDrawable(0x0C000000))
            .builder()
    }

    override fun getRightBackground(): Drawable {
        return getLeftBackground()
    }
}