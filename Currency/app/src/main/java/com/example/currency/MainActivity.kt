package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var sourceCurrencyEditText: EditText
    private lateinit var targetCurrencyEditText: EditText
    private lateinit var sourceCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner

    private val apiKey = "YOUR_FIXER_API_KEY" // Thay bằng API key của bạn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các view
        sourceCurrencyEditText = findViewById(R.id.sourceCurrencyEditText)
        targetCurrencyEditText = findViewById(R.id.targetCurrencyEditText)
        sourceCurrencySpinner = findViewById(R.id.sourceCurrencySpinner)
        targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner)

        setupSpinners()

        // Lắng nghe thay đổi từ EditText
        sourceCurrencyEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertCurrency()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Lắng nghe thay đổi từ Spinner
        sourceCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        targetCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSpinners() {
        val currencies = arrayOf(
            "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD",
            "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "INR", "RUB", "BRL", "ZAR",
            "PLN", "PHP", "IDR", "HUF", "CZK", "ILS", "CLP", "THB", "MYR", "RON"
        ) // Danh sách các mã tiền tệ phổ biến

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter
    }

    private fun convertCurrency() {
        val sourceCurrency = sourceCurrencySpinner.selectedItem.toString()
        val targetCurrency = targetCurrencySpinner.selectedItem.toString()
        val amount = sourceCurrencyEditText.text.toString().toDoubleOrNull()

        if (amount != null) {
            fetchExchangeRate(sourceCurrency, targetCurrency, amount)
        }
    }

    private fun fetchExchangeRate(baseCurrency: String, targetCurrency: String, amount: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<currencyResponse> = retrofitClient.instance.getRates(apiKey, baseCurrency)
            if (response.isSuccessful) {
                val rates = response.body()?.rates
                val exchangeRate = rates?.get(targetCurrency)

                if (exchangeRate != null) {
                    withContext(Dispatchers.Main) {
                        val convertedAmount = amount * exchangeRate
                        targetCurrencyEditText.setText(convertedAmount.toString())
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Không thể lấy tỷ giá", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Không thể kết nối đến API", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}