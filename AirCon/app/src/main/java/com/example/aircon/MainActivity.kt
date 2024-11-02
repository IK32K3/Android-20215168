package com.example.aircon

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val esp32Ip = "http://<ESP32_IP_ADDRESS>"  // Thay địa chỉ IP của ESP32

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOn: Button = findViewById(R.id.btnOn)
        val btnOff: Button = findViewById(R.id.btnOff)

        btnOn.setOnClickListener {
            sendCommand("/on")
        }

        btnOff.setOnClickListener {
            sendCommand("/off")
        }
    }

    private fun sendCommand(endpoint: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("$esp32Ip$endpoint")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Lệnh đã gửi thành công", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Không thể gửi lệnh", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}