package com.example.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var lvDevices: ListView
    private lateinit var btnScan: Button
    private lateinit var btnConnect: Button
    private lateinit var btnSend: Button
    private lateinit var etMessage: EditText
    private lateinit var tvReceivedData: TextView

    private var selectedDevice: BluetoothDevice? = null

    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ giao diện
        lvDevices = findViewById(R.id.lv_devices)
        btnScan = findViewById(R.id.btn_scan)
        btnConnect = findViewById(R.id.btn_connect)
        btnSend = findViewById(R.id.btn_send)
        etMessage = findViewById(R.id.et_message)
        tvReceivedData = findViewById(R.id.tv_received_data)

        // Lấy BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // Quét thiết bị
        btnScan.setOnClickListener {
            if (checkBluetoothPermissions()) {
                try {
                    scanBluetoothDevices()
                } catch (e: SecurityException) {
                    Toast.makeText(this, "Không đủ quyền để quét thiết bị", Toast.LENGTH_SHORT).show()
                }
            } else {
                requestBluetoothPermissions()
            }
        }

        // Kết nối thiết bị
        btnConnect.setOnClickListener {
            if (checkBluetoothPermissions()) {
                selectedDevice?.let { device ->
                    try {
                        connectToDevice(device)
                    } catch (e: SecurityException) {
                        Toast.makeText(this, "Không đủ quyền để kết nối thiết bị", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                requestBluetoothPermissions()
            }
        }

        // Gửi dữ liệu
        btnSend.setOnClickListener {
            val message = etMessage.text.toString()
            if (message.isNotEmpty()) {
                try {
                    sendDataToDevice(message)
                } catch (e: Exception) {
                    Toast.makeText(this, "Không thể gửi dữ liệu", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập dữ liệu để gửi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Kiểm tra quyền Bluetooth
    private fun checkBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            permissions.all { perm ->
                ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Yêu cầu quyền Bluetooth
    private fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // Xử lý kết quả yêu cầu quyền
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Quyền đã được cấp", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Không đủ quyền để sử dụng Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Quét thiết bị Bluetooth
    private fun scanBluetoothDevices() {
        if (!checkBluetoothPermissions()) {
            Toast.makeText(this, "Bạn chưa cấp quyền Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pairedDevices = bluetoothAdapter.bondedDevices

            if (pairedDevices.isNotEmpty()) {
                val deviceList = pairedDevices.map { device ->
                    "${device.name} (${device.address})"
                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    deviceList
                )

                lvDevices.adapter = adapter

                lvDevices.setOnItemClickListener { _, _, position, _ ->
                    selectedDevice = pairedDevices.elementAt(position)
                    Toast.makeText(this, "Chọn: ${selectedDevice?.name}", Toast.LENGTH_SHORT).show()
                    btnConnect.isEnabled = true
                }
            } else {
                Toast.makeText(this, "Không tìm thấy thiết bị", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Không đủ quyền để quét thiết bị", Toast.LENGTH_SHORT).show()
        }
    }

    // Kết nối với thiết bị Bluetooth
    private fun connectToDevice(device: BluetoothDevice) {
        try {
            // Kết nối với thiết bị (giả sử đã có socket logic)
            Toast.makeText(this, "Kết nối đến ${device.name}", Toast.LENGTH_SHORT).show()
            btnSend.isEnabled = true
        } catch (e: SecurityException) {
            Toast.makeText(this, "Không đủ quyền để kết nối thiết bị", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Không thể kết nối với thiết bị", Toast.LENGTH_SHORT).show()
        }
    }

    // Gửi dữ liệu đến thiết bị
    private fun sendDataToDevice(message: String) {
        try {
            // Logic gửi dữ liệu qua socket
            Toast.makeText(this, "Gửi: $message", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Không thể gửi dữ liệu", Toast.LENGTH_SHORT).show()
        }
    }
}

