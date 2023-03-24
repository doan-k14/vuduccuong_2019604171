package com.example.qltaichinhcanhan.main.library

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DecimalTextWatcher(private val editText: EditText) : TextWatcher {

    private var previousText = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val newText = s.toString()
        val formattedText = formatValue(newText)
        if (newText != formattedText && newText != previousText) {
            editText.setText(formattedText)
            editText.setSelection(formattedText.length)
        }
        previousText = formattedText
    }

    override fun afterTextChanged(s: Editable?) {}

    private fun formatValue(value: String): String {
        val unformattedValue = value.replace(",", "")
        val number = unformattedValue.toLongOrNull() ?: return ""
        return String.format("%,d", number)
    }
}
