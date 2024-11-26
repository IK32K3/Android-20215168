package com.example.studentman

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditStudent : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_edit_student)

        val editName: EditText = findViewById(R.id.edit_student_name)
        val editId: EditText = findViewById(R.id.edit_student_id)
        val btnUpdate: Button = findViewById(R.id.btn_update)

        val name = intent.getStringExtra("student_name")
        val id = intent.getStringExtra("student_id")
        val position = intent.getIntExtra("position", -1)

        editName.setText(name)
        editId.setText(id)

        btnUpdate.setOnClickListener {
            val updatedName = editName.text.toString()
            val updatedId = editId.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("student_name", updatedName)
            resultIntent.putExtra("student_id", updatedId)
            resultIntent.putExtra("position", position)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}