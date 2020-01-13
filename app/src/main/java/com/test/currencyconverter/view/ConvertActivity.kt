package com.test.currencyconverter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.currencyconverter.R
import com.test.currencyconverter.common.RateItemDecoration
import com.test.currencyconverter.view.adapter.RateRecyclerViewAdapter
import com.test.currencyconverter.viewmodel.CurrencyViewModel
import kotlinx.android.synthetic.main.activity_convert.*

class ConvertActivity : AppCompatActivity() {

    private var currencyViewModel: CurrencyViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: RateRecyclerViewAdapter? = null
    private var layoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        recyclerView = findViewById(R.id.recycler_view_rate)
        layoutManager = GridLayoutManager(this, 3)
        adapter = RateRecyclerViewAdapter(this, emptyList())
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(RateItemDecoration(3, 15))
        recyclerView?.adapter = adapter


        registerViewModel()

        edit_text_amount.setOnEditorActionListener { textView, actionId, keyEvent ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val input = try {edit_text_amount.text.toString().toFloat()} catch (e: Exception) {0f}
                    adapter?.updateInputAmount(input)
                    false
                }
                else -> {
                    false
                }
            }
        }

        edit_text_amount.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val input = try {edit_text_amount.text.toString().toFloat()} catch (e: Exception) {0f}
                adapter?.updateInputAmount(input)
            }
        }

        edit_text_amount.setOnTouchListener { view, motionEvent ->
            view.setFocusable(true)
            view.setFocusableInTouchMode(true)
            false
        }

        spinner_currency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currencyViewModel?.updateSelectedCurrency(spinner_currency.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btn_retry.setOnClickListener {
            currencyViewModel?.loadCurrency()
        }

    }

    /**
     * view modelを登録する
     */
    private fun registerViewModel() {
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        currencyViewModel?.let { model ->
            model.loadCurrency().observe(this, Observer { currencyLive ->
                adapter?.updateContents(currencyLive.getRateMap().toList())

                val currencySpinnerAdapter = ArrayAdapter(this, R.layout.spinner_item_black_text, currencyLive.getRateMap().keys.toTypedArray())
                currencySpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spinner_currency.adapter = currencySpinnerAdapter
                spinner_currency.setSelection(currencyLive.getRateMap().keys.indexOf("USD"))
            })
            model.getRateMapData().observe(this, Observer {
                adapter?.updateContents(it.toList())
            })
            model.getLoadingStatus().observe(this, Observer { loadingStatus ->
                when (loadingStatus) {
                    CurrencyViewModel.LoadingStatus.Loading -> {
                        layout_loading.visibility = View.VISIBLE
                        layout_error.visibility = View.GONE
                        layout_content.visibility = View.GONE
                    }
                    CurrencyViewModel.LoadingStatus.Success -> {
                        layout_loading.visibility = View.GONE
                        layout_error.visibility = View.GONE
                        layout_content.visibility = View.VISIBLE
                    }
                    CurrencyViewModel.LoadingStatus.Error -> {
                        layout_loading.visibility = View.GONE
                        layout_error.visibility = View.VISIBLE
                        layout_content.visibility = View.GONE
                    }
                    else -> {
                        layout_loading.visibility = View.GONE
                        layout_error.visibility = View.VISIBLE
                        layout_content.visibility = View.GONE
                    }
                }
            })
        }
    }
}
