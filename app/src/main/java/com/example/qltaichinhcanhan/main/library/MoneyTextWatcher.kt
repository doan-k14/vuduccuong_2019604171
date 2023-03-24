package com.example.qltaichinhcanhan.main.library

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class MoneyTextWatcher(editText: EditText) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText>
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val editText = editTextWeakReference.get()
        if (editText == null || editText.text.toString().isEmpty()) {
            return
        }
        editText.removeTextChangedListener(this)
        val parsed = parseCurrencyValue(editText.text.toString())
        val formatted = formatter.format(parsed)
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    companion object {
        private val formatter = DecimalFormat("#,###")
        fun parseCurrencyValue(value: String): BigDecimal {
            try {
                val replaceRegex = String.format(
                    "[%s,.\\s]", Objects.requireNonNull(
                        formatter.currency
                    )
                )
                val currencyValue = value.replace(replaceRegex.toRegex(), "")
                return BigDecimal(currencyValue)
            } catch (e: Exception) {
                Log.e("App", e.message, e)
            }
            return BigDecimal.ZERO
        }
    }

    init {
        editTextWeakReference = WeakReference(editText)
        formatter.maximumFractionDigits = 0
        formatter.roundingMode = RoundingMode.FLOOR
    }
}
