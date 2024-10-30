package com.example.number

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editTextNumber = findViewById<EditText>(R.id.editTextNumber)
        val buttonShow = findViewById<Button>(R.id.buttonShow)
        val listView = findViewById<ListView>(R.id.listView)
        val textView = findViewById<TextView>(R.id.textView)
        val radioButtonEven = findViewById<RadioButton>(R.id.radioButtonEven)
        val radioButtonOdd = findViewById<RadioButton>(R.id.radioButtonOdd)
        val radioButtonSquare = findViewById<RadioButton>(R.id.radioButtonSquare)

        buttonShow.setOnClickListener {
            val n = editTextNumber.text.toString().toIntOrNull()
            if (n != null && n > 0) {
                val list: MutableList<Int> = mutableListOf()
                // Tính toán và thêm các số vào list dựa trên lựa chọn

                if (radioButtonEven.isChecked) {
                    for (i in 0..n step 2) {
                        list.add(i)
                    }
                } else if (radioButtonOdd.isChecked) {
                    for(i in 1..n step 2) list.add(i)
                } else if (radioButtonSquare.isChecked) {
                    for(i in 1 ..sqrt(n.toDouble()).toInt()) list.add(i*i)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
                listView.adapter = adapter
                textView.text = "" // Xóa thông báo lỗi
            } else {
                textView.text = "Vui lòng nhập số nguyên dương"
            }
        }
    }
}