package com.gooboot.golib.widget.titlebar.style

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build

class SelectorDrawable : StateListDrawable() {

    class Builder {

        /** 默认状态  */
        private var mDefault: Drawable? = null
        /** 焦点状态  */
        private var mFocused: Drawable? = null
        /** 按下状态  */
        private var mPressed: Drawable? = null
        /** 选中状态  */
        private var mChecked: Drawable? = null
        /** 启用状态  */
        private var mEnabled: Drawable? = null
        /** 选择状态  */
        private var mSelected: Drawable? = null
        /** 光标悬浮状态（4.0新特性）  */
        private var mHovered: Drawable? = null

        fun setDefault(drawable: Drawable): Builder {
            mDefault = drawable
            return this
        }

        fun setFocused(drawable: Drawable): Builder {
            mFocused = drawable
            return this
        }

        fun setPressed(drawable: Drawable): Builder {
            mPressed = drawable
            return this
        }

        fun setChecked(drawable: Drawable): Builder {
            mChecked = drawable
            return this
        }

        fun setEnabled(drawable: Drawable): Builder {
            mEnabled = drawable
            return this
        }

        fun setSelected(drawable: Drawable): Builder {
            mSelected = drawable
            return this
        }

        fun setHovered(drawable: Drawable): Builder {
            mHovered = drawable
            return this
        }

        fun builder(): SelectorDrawable {
            val selector = SelectorDrawable()
            if (mPressed != null) {
                selector.addState(intArrayOf(android.R.attr.state_pressed), mPressed)
            }
            if (mFocused != null) {
                selector.addState(intArrayOf(android.R.attr.state_focused), mFocused)
            }
            if (mChecked != null) {
                selector.addState(intArrayOf(android.R.attr.state_checked), mChecked)
            }
            if (mEnabled != null) {
                selector.addState(intArrayOf(android.R.attr.state_enabled), mEnabled)
            }
            if (mSelected != null) {
                selector.addState(intArrayOf(android.R.attr.state_selected), mSelected)
            }
            if (mHovered != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    selector.addState(intArrayOf(android.R.attr.state_hovered), mHovered)
                }
            }
            if (mDefault != null) {
                selector.addState(intArrayOf(), mDefault)
            }
            return selector
        }
    }
}