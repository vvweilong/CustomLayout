package com.oo.layoutlib.linear

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
/**
* create by 朱晓龙 2019/12/31 10:50 PM
 * 伪流式布局
 *
*/
class CustomTagFlowLayout:ViewGroup {
    private var maxLines = 2


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }
}