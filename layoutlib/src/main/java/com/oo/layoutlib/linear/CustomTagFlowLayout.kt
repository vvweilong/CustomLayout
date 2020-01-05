package com.oo.layoutlib.linear

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.oo.layoutlib.R

/**
 * create by 朱晓龙 2019/12/31 10:50 PM
 * 伪流式布局
 *
 *
 */
class CustomTagFlowLayout : ViewGroup {
    private var maxLines = 2


    private val soldChilds = ArrayList<View>()
    private val normalChilds = ArrayList<View>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        soldChilds.clear()
        normalChilds.clear()
        for (child in children) {
            val lp = child.layoutParams as LayoutParams
            when(lp.itemTag){
                LayoutParams.SOLD->{
                    soldChilds.add(child)
                }
                LayoutParams.NORMALL->{
                    normalChilds.add(child)
                }
            }
        }

        var totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        //当前 view 的宽度模式
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> {//依赖 child
                //todo
            }
            MeasureSpec.AT_MOST -> {//限定最大

            }
            MeasureSpec.EXACTLY -> {//确定值
                //首先 测量 设置为固定的 view
                val soldMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                for (soldChild in soldChilds) {
                    soldChild.measure(soldMeasureSpec,heightMeasureSpec)
                    totalWidth -= soldChild.measuredWidth
                }
                //用剩余的宽度测量 非固定的 view
                val normallMeasureSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST)
                for (normalChild in normalChilds) {
                    normalChild.measure(normallMeasureSpec,heightMeasureSpec)
                    totalWidth-=normalChild.measuredWidth
                }
            }
            else -> {
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //先放固定的 view
        soldChilds.reverse()
        var currentRight = r
        for (soldChild in soldChilds) {
            soldChild.layout(currentRight-soldChild.measuredWidth,t,currentRight,t+soldChild.measuredHeight)
            currentRight -=soldChild.measuredWidth
        }
        for (normalChild in normalChilds) {
            normalChild.layout(l,t,l+normalChild.measuredWidth,t+normalChild.measuredHeight)
        }
    }


    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context,attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        val marginLayoutParams = MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return LayoutParams(marginLayoutParams)
    }

    open class LayoutParams : MarginLayoutParams {
        companion object {
            const val SOLD = 1//固定的
            const val NORMALL = 0//
        }
        var itemTag = NORMALL

        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs) {
            c?.run {
                val obtainStyledAttributes =
                    obtainStyledAttributes(attrs, R.styleable.CustomTagFlowLayout_Layout)
               itemTag= obtainStyledAttributes.getInt(R.styleable.CustomTagFlowLayout_Layout_item_tag,
                    NORMALL)
                obtainStyledAttributes.recycle()
            }
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)

    }
}