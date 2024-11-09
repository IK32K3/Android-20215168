package com.example.airconditoner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AcListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ac_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val acList = listOf("Mitsubishi", "Daikin", "Panasonic")
        val adapter = AcListAdapter(acList) { selectedAc ->
            val intent = Intent(this, AcControlActivity::class.java)
            intent.putExtra("selectedAc", selectedAc)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}