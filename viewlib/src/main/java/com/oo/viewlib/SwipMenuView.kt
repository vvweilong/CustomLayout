package com.oo.viewlib

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.children

/**
 * create by 朱晓龙 2019/12/31 11:02 PM
 * 侧滑view
 */
class SwipMenuView : ViewGroup {

    private var dataView: View? = null
    private var menuView: View? = null

    private var currentOffset = 0
    private var mMaxOffsetPersent=0.5f
    private var mMaxOffset=0

    private var mTouchSlop = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (child in children) {
            val lp = child.layoutParams as SwipLayoutParams
            if (lp.viewType == SwipLayoutParams.VIEW_TYPE_DATA) {
                dataView = child
            }
            if (lp.viewType == SwipLayoutParams.VIEW_TYPE_MENU) {
                menuView = child
            }
        }
        val lp = menuView?.layoutParams as SwipLayoutParams
        menuView?.measure(MeasureSpec.makeMeasureSpec(lp.width,MeasureSpec.EXACTLY), heightMeasureSpec)
        dataView?.measure(widthMeasureSpec, heightMeasureSpec)
        mMaxOffset = menuView?.measuredWidth?:0
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        dataView?.layout(l, t, r, b)
        menuView?.layout(r-(menuView?.measuredWidth?:0),t,r,b)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        return super.onInterceptTouchEvent(ev)
    }

    private var downX = 0.0f
    private var downY = 0.0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        var handled = false
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return true
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is SwipLayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return SwipLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return SwipLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return SwipLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    open class SwipLayoutParams : ViewGroup.MarginLayoutParams {
        companion object {
            const val VIEW_TYPE_MENU = 1
            const val VIEW_TYPE_DATA = 0
        }

        var viewType = VIEW_TYPE_DATA

        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs) {
            val obtainStyledAttributes =
                c?.obtainStyledAttributes(attrs, R.styleable.SwipMenuView_Layout)

            viewType = obtainStyledAttributes?.getInt(
                R.styleable.SwipMenuView_Layout_type,
                VIEW_TYPE_DATA
            ) ?: VIEW_TYPE_DATA
            obtainStyledAttributes?.recycle()

        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: LayoutParams?) : super(source)
        constructor(source: MarginLayoutParams?) : super(source)

    }

}