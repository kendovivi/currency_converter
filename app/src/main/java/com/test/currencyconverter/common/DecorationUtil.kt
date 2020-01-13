package com.test.currencyconverter.common

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RateItemDecoration(val spanCount: Int, var space: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var padding = space
        parent?.context?.resources?.let {
            val metrics = it.displayMetrics
            padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, space.toFloat(), metrics).toInt()
        }

        val position = parent?.getChildAdapterPosition(view)
        parent.getChildAt(0)
        position?.let {
            val column = position % spanCount
            outRect?.let {
                it.left = padding - column * padding / spanCount
                it.right = (column + 1) * padding / spanCount
                if (position < spanCount) {
                    it.top = padding / 2 * 3
                }
                it.bottom = padding
            }
        }
    }
}