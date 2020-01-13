package com.test.currencyconverter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.currencyconverter.R
import kotlinx.android.synthetic.main.item_currency_rate.view.*

class RateRecyclerViewAdapter(private val context: Context, private var currencyList: List<Pair<String, Float>>): RecyclerView.Adapter<RateRecyclerViewAdapter.ViewHolder>() {

    private var inputAmount: Float = 0f

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_currency_rate, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setup(currencyList[position])
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setup(currency: Pair<String, Float>) {
            itemView.tv_currency.text = currency.first
            itemView.tv_rate.text = currency.second.toString()
            itemView.tv_amount.text = (currency.second * inputAmount).toString()
        }
    }

    fun updateContents(newCurrencyList: List<Pair<String, Float>>?) {
        currencyList = newCurrencyList?: emptyList()
        notifyDataSetChanged()
    }

    fun updateInputAmount(newInputAmount: Float) {
        if (newInputAmount != inputAmount) {
            inputAmount = newInputAmount
            notifyDataSetChanged()
        }
    }
}