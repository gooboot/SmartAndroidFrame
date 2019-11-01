package com.gooboot.golib.widget.titlebar.style

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.gooboot.golib.R

class TitleBarTransparentStyle (context: Context):BaseBarStyle(context){
    override fun getBackground(): Drawable {
        return ColorDrawable(0x00000000)
    }

    override fun getLeftIcon(): Drawable {
        return getDrawable(R.mipmap.bar_icon_back_black)
    }

    override fun getLeftColor(): Int {
        return -0x1
    }

    override fun getTitleColor(): Int {
        return -0x1
    }

    override fun getRightColor(): Int {
        return -0x1
    }

    override fun isLineVisible(): Boolean {
        return false
    }

    override fun getLineDrawable(): Drawable {
        return ColorDrawable(0x00000000)
    }

    override fun getLeftBackground(): Drawable {
        return SelectorDrawable.Builder()
            .setDefault(ColorDrawable(0x00000000))
            .setFocused(ColorDrawable(0x22000000))
            .setPressed(ColorDrawable(0x22000000))
            .builder()
    }

    override fun getRightBackground(): Drawable {
        return getLeftBackground()
    }
}