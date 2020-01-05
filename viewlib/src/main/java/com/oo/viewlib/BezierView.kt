package com.oo.viewlib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class BezierView:View, ValueAnimator.AnimatorUpdateListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
     val path=Path()
     val path1=Path()
    val paint = Paint()
    val valueAnimator : ValueAnimator
    init {
        paint.color = Color.RED
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        valueAnimator=ValueAnimator.ofInt(0,100)


        valueAnimator.addUpdateListener(this)
        valueAnimator.duration = 1000
        valueAnimator.repeatMode=ValueAnimator.RESTART
        valueAnimator.interpolator = LinearInterpolator()
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }




    private fun calculatePath(offset:Int){
        path.reset()
        val position = width * offset / 100
        val position1 = position/2
        path.moveTo(0f- position,height.toFloat()/2f)
        path.quadTo((width.toFloat()/4f) - position,0f,(width.toFloat()/2) - position,height.toFloat()/2f)
        path.quadTo((3*width.toFloat()/4f) -   position,height.toFloat(),(width.toFloat()) - position,height.toFloat()/2f)

        path.quadTo(5*(width.toFloat()/4f) - position,0f,(3*width.toFloat()/2) - position,height.toFloat()/2f)
        path.quadTo((7*width.toFloat()/4f) -   position,height.toFloat(),(2*width.toFloat()) - position,height.toFloat()/2f)

        path1.reset()

        path1.moveTo(0f- position1,height.toFloat()/2f)
        path1.quadTo((width.toFloat()/4f) - position1,0f,(width.toFloat()/2) - position1,height.toFloat()/2f)
        path1.quadTo((3*width.toFloat()/4f) -   position1,height.toFloat(),(width.toFloat()) - position1,height.toFloat()/2f)

        path1.quadTo(5*(width.toFloat()/4f) - position1,0f,(3*width.toFloat()/2) - position1,height.toFloat()/2f)
        path1.quadTo((7*width.toFloat()/4f) -   position1,height.toFloat(),(2*width.toFloat()) - position1,height.toFloat()/2f)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run {
            if (!valueAnimator.isStarted) {
                calculatePath(0)
                drawPath(path,paint)
                drawPath(path1,paint)
                valueAnimator.start()
            }
            canvas.translate(width*offsetPersent.toFloat()/100,0f)
        }
    }
    private var offsetPersent = 0
    override fun onAnimationUpdate(animation: ValueAnimator?) {
        val value = animation?.animatedValue as Int
        offsetPersent=value
        postInvalidateOnAnimation()
    }
}