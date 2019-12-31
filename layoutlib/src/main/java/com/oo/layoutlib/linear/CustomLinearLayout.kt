package com.oo.layoutlib.linear

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.oo.layoutlib.R

/**
* create by 朱晓龙 2019/12/31 9:11 PM
 * 基本线性布局
*/
class CustomLinearLayout:ViewGroup {
    companion object{
        const val ORIENTATION_VERTICAL=0//垂直方向 默认方向
        const val ORIENTATION_HORIZONTAL=1//水平方向
    }

    private var orientation = ORIENTATION_VERTICAL
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        val obtainStyledAttributes =
            context?.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout)

        orientation = obtainStyledAttributes?.getInt(R.styleable.CustomLinearLayout_orientation,
            ORIENTATION_VERTICAL)?: ORIENTATION_VERTICAL

        obtainStyledAttributes?.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (orientation == ORIENTATION_HORIZONTAL) {
            measureHorizontal(widthMeasureSpec,heightMeasureSpec)
        }else{
            measureVertical(widthMeasureSpec, heightMeasureSpec)
        }
    }

    private fun measureVertical(widthMeasureSpec: Int, heightMeasureSpec: Int){
        //要判断每个 child 设置的height 情况
        var weightSum = 0
        var remainHeight = MeasureSpec.getSize(heightMeasureSpec)

        for (child in children) {
            val lp = child.layoutParams as LinearLayoutParams
            weightSum+=lp.weight
        }
        if (weightSum==0) {
            for (child in children) {
                val lp = child.layoutParams as LinearLayoutParams
                when (lp.height) {
                    ViewGroup.LayoutParams.WRAP_CONTENT -> {//按照内容进行计算高度
                        val wrapHeightSpec = MeasureSpec.makeMeasureSpec(
                            remainHeight,
                            MeasureSpec.UNSPECIFIED
                        )
                        child.measure(widthMeasureSpec,wrapHeightSpec)
                        remainHeight -= child.measuredHeight
                    }
                    ViewGroup.LayoutParams.MATCH_PARENT ->{//充满父布局高度
                        val childHeight = MeasureSpec.getSize(heightMeasureSpec)
                        val heightSpec  =
                            MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)

                        child.measure(widthMeasureSpec,heightSpec)
                    }
                    else -> {//其他情况 设置了 准确的数值
                        val childHeight = lp.height
                        remainHeight-=childHeight
                        val heightSpec  =
                            MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
                        child.measure(widthMeasureSpec,heightSpec)
                    }
                }
            }
        }else{//当有 child 设置了比重
            for (child in children) {
                val lp = child.layoutParams as LinearLayoutParams
                if (lp.weight==0) {
                    //如果当前 child 没设置比重 按照普通方式进行计算
                    when (lp.height) {
                        ViewGroup.LayoutParams.WRAP_CONTENT -> {//按照内容进行计算高度
                            val wrapHeightSpec = MeasureSpec.makeMeasureSpec(
                                remainHeight,
                                MeasureSpec.UNSPECIFIED
                            )
                            child.measure(widthMeasureSpec,wrapHeightSpec)
                            remainHeight -= child.measuredHeight
                        }
                        ViewGroup.LayoutParams.MATCH_PARENT ->{//充满父布局高度
                            val childHeight = MeasureSpec.getSize(heightMeasureSpec)
                            val heightSpec  =
                                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)

                            child.measure(widthMeasureSpec,heightSpec)
                        }
                        else -> {//其他情况 设置了 准确的数值
                            val childHeight = lp.height
                            remainHeight-=childHeight
                            val heightSpec  =
                                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
                            child.measure(widthMeasureSpec,heightSpec)
                        }
                    }

                }else{
                    //如果 当前 child 设置了比重
                    var radio = lp.weight.toFloat()/weightSum.toFloat()
                    val childHeight = (MeasureSpec.getSize(heightMeasureSpec)*radio).toInt()
                    remainHeight-=childHeight
                    val heightSpec  =
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
                    child.measure(widthMeasureSpec,heightSpec)
                }
            }
        }

    }
    private fun measureHorizontal(widthMeasureSpec: Int, heightMeasureSpec: Int){

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentTop = 0

        for (child in children) {
            child.layout(l,currentTop,l+child.measuredWidth,currentTop+child.measuredHeight)
            currentTop+=child.measuredHeight
        }
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is LinearLayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LinearLayoutParams(context,attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return LinearLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return LinearLayoutParams(layoutParams)
    }


    open class LinearLayoutParams:ViewGroup.MarginLayoutParams{
        var weight = 0
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs){
            val obtainStyledAttributes =
                c?.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout_Layout)

            weight = obtainStyledAttributes?.getInt(R.styleable.CustomLinearLayout_Layout_weight,0)?:0

            obtainStyledAttributes?.recycle()
        }
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)

    }
}