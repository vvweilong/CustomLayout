package com.oo.layoutlib.linear

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.children

class CustomScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    init {
        initCustomScrollView()
    }

    private var mScroller :Scroller?=null
    private var mTouchSlop :Int?=null
    private var scaledMinimumFlingVelocity=0
    private var scaledMaximumFlingVelocity=0
    private var scaledOverflingDistance=0
    private var scaledOverscrollDistance=0
    private var scaledVerticalScrollFactor=0

    private var canScrollVertical = false

    private fun initCustomScrollView(){
        mScroller = Scroller(context)
        setWillNotDraw(false)
        val configuration = ViewConfiguration.get(context)
        mTouchSlop= configuration.scaledPagingTouchSlop
        scaledMinimumFlingVelocity = configuration.scaledMinimumFlingVelocity
        scaledMaximumFlingVelocity = configuration.scaledMaximumFlingVelocity
        scaledOverflingDistance = configuration.scaledOverflingDistance
        scaledOverscrollDistance = configuration.scaledOverscrollDistance
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (child in children) {
            child.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (child in children) {
            child.layout(l, t, r, b)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) {
            return false
        }
        //是否要拦截事件
        //如果是 move 事件
        when(ev.action and MotionEvent.ACTION_MASK){
            MotionEvent.ACTION_MOVE->{}
            MotionEvent.ACTION_DOWN->{}
            MotionEvent.ACTION_UP->{}
        }



        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}