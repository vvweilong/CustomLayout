package com.oo.layoutlib.linear

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import android.widget.Scroller
import androidx.core.view.children

class CustomScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    init {
        initCustomScrollView()
    }

    private var mVelocityTracker = VelocityTracker.obtain()
    private var mActivePointerId: Int = 0
    private var mLastMotionY: Int=0
    private var mScroller :OverScroller?=null
    private var mTouchSlop :Int=0
    private var scaledMinimumFlingVelocity=0
    private var scaledMaximumFlingVelocity=0
    private var scaledOverflingDistance=0
    private var scaledOverscrollDistance=0
    private var scaledVerticalScrollFactor=0

    private var canScrollVertical = false

    private fun initCustomScrollView(){
        mScroller = OverScroller(context)
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
            val makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec),
                MeasureSpec.UNSPECIFIED
            )
            child.measure(widthMeasureSpec, makeMeasureSpec)
            Log.i("measure","${child.measuredHeight}")
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (child in children) {
            child.layout(l, t, r, t+child.measuredHeight)
        }

    }
    private var mIsBeingDragged=false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker.addMovement(event)
        val actionMasked = event?.actionMasked
        mActivePointerId = event?.getPointerId(0)?:-1
        when(actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = event.y.toInt()
                return true

            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = mLastMotionY-event.y.toInt()//增量
                mScroller?.startScroll(0,mScroller?.finalY?:0,0,deltaY)

                invalidate()
                mLastMotionY = event.y.toInt()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val childAt = getChildAt(0)
                childAt.scrollY
                mVelocityTracker.computeCurrentVelocity(1000, scaledMaximumFlingVelocity.toFloat())
                val initialVelocity = mVelocityTracker.getYVelocity(mActivePointerId).toInt()
                val height: Int = height
                val bottom = childAt.height

                mScroller?.fling(
                    0, childAt.scrollY, 0, -initialVelocity, 0, 0, 0,
                    Math.max(0, bottom - height),0,height/2)

                postInvalidateOnAnimation()
                mVelocityTracker.clear()
            }
        }


        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if(mScroller?.computeScrollOffset()==true){
            getChildAt(0).scrollTo(0,mScroller?.currY?:0)
            postInvalidateOnAnimation()
        }

    }
}