package com.example.airconditoner

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class AcControlActivity : AppCompatActivity() {
    private lateinit var selectedAc: String
    private var currentTemperature = 24  // Nhiệt độ mặc định
    private lateinit var esp32Ip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ac_control)

        val preferencesHelper = PreferencesHelper(this)
        esp32Ip = preferencesHelper.getEsp32Ip() ?: run {
            finish()  
            return
        }

        selectedAc = intent.getStringExtra("selectedAc") ?: "Unknown"
        findViewById<TextView>(R.id.tvSelectedAc).text = "Điều khiển điều hòa $selectedAc"
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)

        // Nút "Quay lại"
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        // Cập nhật nhiệt độ hiển thị
        tvTemperature.text = "Nhiệt độ: $currentTemperature°C"

        // Nút tăng/giảm nhiệt độ
        findViewById<Button>(R.id.btnIncreaseTemp).setOnClickListener {
            if (currentTemperature < 30) {
                currentTemperature++
                updateTemperature(tvTemperature)
            }
        }
        findViewById<Button>(R.id.btnDecreaseTemp).setOnClickListener {
            if (currentTemperature > 17) {
                currentTemperature--
                updateTemperature(tvTemperature)
            }
        }

        // Nút bật/tắt điều hòa
        findViewById<Button>(R.id.btnPowerOn).setOnClickListener { sendAcCommand("on") }
        findViewById<Button>(R.id.btnPowerOff).setOnClickListener { sendAcCommand("off") }

        sendAcTypeToESP(selectedAc) // Gửi loại điều hòa khi vào màn hình
    }

    private fun updateTemperature(tvTemperature: TextView) {
        tvTemperature.text = "Nhiệt độ: $currentTemperature°C"
        sendAcCommand("set_temp", currentTemperature)
    }

    private fun sendAcTypeToESP(acType: String) {
        val url = "http://$esp32Ip/set_ac_type"
        val client = OkHttpClient()
        val requestBody = FormBody.Builder().add("ac_type", acType).build()

        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("ESP32 đã nhận thông tin loại điều hòa: $acType")
                }
            }
        })
    }

    private fun sendAcCommand(command: String, temperature: Int? = null) {
        val url = if (command == "set_temp") {
            "http://$esp32Ip/set_temp?temp=$temperature"
        } else {
            "http://$esp32Ip/$command"
        }

        val client = OkHttpClient()
        val request = Request.Builder().url(url).get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Đã gửi lệnh $command thành công")
                }
            }
        })
    }
}
